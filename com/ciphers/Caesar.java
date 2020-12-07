/*
*  File Name: Caesar.java
*
*  The enhanced Caesar cipher, a type of substitution cipher in which a letter some number of positions
*  down the alphabet determined by modular arithmetic replaces each letter in the plaintext.
*
*  Notes:
*  - Does not preserve punctuations, spaces, and digits in the ciphering process as well as restoring to original text. 
*  - Only works if all text is compatible with ASCII.
*
*  @author  Francis Nathanael De Villena | BSCS2A | fnodevillena@usep.edu.ph
*  @version 2.2 2020/11/26
*/

package com.ciphers;

/** An enhanced version of the classical substitution cipher. */
public class Caesar {
   
   private StringBuilder builder;
   
   /** Constructs a {@code Caesar()} cipher. */
   public Caesar() {
      builder = new StringBuilder();
   }
   
   /**
   *  Tests if a character is a digit. 
   *
   *  @param character a certain character
   *  @return {@code true} if a character is a digit, otherwise return {@code false}
   */
   private boolean isDigit(char character) {
      return (character >= '0' && character <= '9');
   }
   
   /**
   *  Tests if a character is a letter. 
   *
   *  @param character a certain character
   *  @return {@code true} if a character is a letter, otherwise return {@code false}
   */
   private boolean isAlphabet(char character) {
      return (character >= 'a' && character <= 'z' ||
              character >= 'A' && character <= 'Z');
   }
   
   /**
   *  Tests if a letter is in lower case. 
   *
   *  @param character a certain character
   *  @return {@code true} if a letter is in lowercase, otherwise return {@code false}
   */
   private boolean isLowerCase(char character) {
      return (character >= 'a' && character <= 'z');
   }
   
   /**
   *  Tests if a letter is a vowel. 
   *
   *  @param character a certain character
   *  @return {@code true} if a letter is a vowel, otherwise return {@code false}
   */
   private boolean isVowel(char character) {
      return (character == 'A' ||
              character == 'E' ||
              character == 'I' ||
              character == 'O' ||
              character == 'U');
   }
   
   /**
   *  Determines the number of vowels in the keyword.
   *
   *  @param key a valid keyword
   *  @return count the number of vowels
   */
   private int vowel(String key) {
      int count = 0;
      for (char character : key.toCharArray()) {
         if(isVowel(character)) {
            count++;
         }
      }
      return count;
   }
   
   /**
   *  Determines the number of consonants in the keyword.
   *
   *  @param key a valid keyword
   *  @return count the number of consonants
   */
   private int consonant(String key) {
      int count = 0;
      for (char character : key.toCharArray()) {
         if(!isVowel(character)) {
            count++;
         }
      }
      return count;
   }
   
   /**
   *  Converts the lowercase letter into uppercase.
   *
   *  @param character a certain lowercase letter
   *  @return character a certain uppercase letter
   */
   private char convertUpperCase(char character) {
      character = (char)(character - 32);
      return character;
   }
   
   /**
   *  Encrypts the ciphertext into plaintext using Caesar substitution.
   *
   *  Note:
   *  - Cannot restore to the original plaintext before it was encrypted.
   *
   *  @param text a line of ciphertext
   *  @param key a valid keyword
   *  @return a line of plaintext
   */
   public String encipher(String text, String key) {
      int vowel = vowel(key);
      int consonant = consonant(key);
      for (int index = 0; index < text.length(); index++) {
         char character = text.charAt(index);
         if(isAlphabet(character)) {
            if(isLowerCase(character)) {
               character = convertUpperCase(character);
            }
            character = (char) (((int) character + (vowel * consonant) - 65) % 26 + 65);
            builder.append(character);
         }
      }
      String ciphertext = builder.toString();
      builder.setLength(0);
      return ciphertext;
   }
   
   /**
   *  Decrypts the ciphertext into plaintext using Caesar substitution.
   *
   *  Note:
   *  - Cannot restore to the original plaintext before it was encrypted.
   *
   *  @param text a line of ciphertext
   *  @param key a valid keyword
   *  @return a line of plaintext
   */
   public String decipher(String text, String key) {
      int vowel = vowel(key);
      int consonant = consonant(key);
      for (int index = 0; index < text.length(); index++) {
         char character = text.charAt(index);
         if(isAlphabet(character)) {
            if(isLowerCase(character)) {
               character = convertUpperCase(character);
            }
            character = (char) (((int) character - (vowel * consonant) - 65 + 26) % 26 + 65);
            builder.append(character);
         }
      }
      String ciphertext = builder.toString();
      builder.setLength(0);
      return ciphertext;
   }
}