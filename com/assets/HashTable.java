/*
*  File Name: HashTable.java
*  
*  Hash Table a data structure that implements an associative array abstract data type, a structure that can map keys to indexs.
*
*  Unique Feature:
*  - Uses separate chaining to handle collisions.
*
*  @author  Francis Nathanael De Villena | BSCS2A | fnodevillena@usep.edu.ph
*  @version 1.5 2020/11/20
*/

package com.assets;

/*A simple Hash Table. */
public class HashTable {
   class Node {
      Node next;
      char character;

      public Node(char character) {
         this.character = character;
         this.next = null;
      }
   }
   private Node[] table;
   private int count;

   /**
   *  Constructs an empty {@code HashTable} with specified table count.
   *  The table count is the inputted parameter's next prime number.
   *
   *  @param length the specified table size
   */
   public HashTable(int length) {
      this.table = new Node[length];
      this.count = 0;
   }

   /**
   *  Tests whether the hash table is empty or not.
   *
   *  @return {@code true} if the hash table is indeed empty, otherwise {@code false}
   */
   public boolean isEmpty() {
      return count == 0;
   }

   public boolean isRowEmpty(int index) {
      return (table[index] == null);
   }
   /**
   *  Resets the entire hash table.
   *  Note that this cannot be undone.
   */
   public void clear() {
      int length = table.length;
      table = new Node[length];
      count = 0;
   }

   /**
   *  Returns the current count of the hash table.
   *
   *  @return count the current count
   */
   public int count() {
      return count;
   }

   /**
   *  Adds a certain character in the table.
   *
   *  @param index a certain index to be added
   */
   public void insert(int key, char character) {
      count++;
      int index = hash(key);
      Node current, latest = new Node(character);
      if(table[index] == null)
         table[index] = latest;
      else {
         current = table[index];
         while (current.next != null) {
            current = current.next;
         }
         current.next = latest;
      }
   }

   public char pop(int key) {
      char character;
      int index = hash(key);
      Node top = table[index];
      character = top.character;
      table[index] = top.next;
      count--;
      return character;
   }

   /**
   *  Generates a hash based on a certain index.
   *
   *  @param index a certain index to be deleted
   *  @return hash the hash of a certain index
   */
   private int hash(Integer index) {
      int hash = index.hashCode();
      if(hash < 0)
         hash += table.length;
      hash %= table.length;
      return hash;
   }

   /**
   *  Enumerates the entire hash table.
   *  For testing purposes only.
   */
   public void display() {
      System.out.println();
      for(int i = 0; i < table.length; i++) {
         System.out.print(i + "\t|\t");
         Node start = table[i];
         while(start != null) {
            System.out.print(start.character + "\t");
            start = start.next;
         }
         System.out.println();
      }
   }
}