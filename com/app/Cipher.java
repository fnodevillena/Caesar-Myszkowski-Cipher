/*
*  File Name: Cipher.java
*  Dependencies: Myszkowski.java and Caesar.java
*
*  Caesar-Myszkowski cipher is a combination of two cryptographic techniques.
*  The enhanced Caesar cipher, a type of substitution cipher in which a letter some number of positions
*  down the alphabet determined by modular arithmetic replaces each letter in the plaintext,
*  and Myszkowski transposition, a variant form of columnar transposition that requires a keyword with recurrent letters.
*
*  Unique Features:
*  - Uses Graphical User Interface.
*  - Allows the user to select a file to encipher and decipher.
*  - Creates a new file in each ciphering process, however, overwrites the old one from the previous process if it does exist
*     in the specified directory.     
*
*  Notes:
*  - Does not preserve punctuations, spaces, and digits in the ciphering process.
*  - Only works if all text is compatible with ASCII.
*
*  @author  Francis Nathanael De Villena | BSCS2A | fnodevillena@usep.edu.ph
*  @version 2.2 2020/11/26
*/

package com.app;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.BorderFactory;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.border.*; 
import javax.swing.text.DefaultCaret;
import javax.swing.ImageIcon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.BorderLayout;  
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Import local packages.
import com.ciphers.Caesar;
import com.ciphers.Myszkowski;

/** A hybrid cipher. */
public class Cipher extends JFrame {
   
   // Cipher
   private File file;
   private File selectedFile;
   private File newFile;
   private FileReader read;
   private FileWriter writer;
   private StringBuilder builder;
   private Caesar caesar;
   private Myszkowski myszkowski;

   private String path;
   private boolean isValid;
   private boolean isOpen;
   
   // Frame
   private JPanel backgroundPanel, foregroundPanel;
   private JLabel backgroundImage, keyInputStatus;
   private JButton select, encipher, decipher;
   private JTextField keyInput;
   private JTextArea statusBox;
   private JScrollPane scroll;

   /** Constructs the {@code Cipher} with GUI. */
   public Cipher() {
      
      /// Border
      
      Border line = new LineBorder(Color.decode("#353535"));
      Border margin = new EmptyBorder(5, 15, 5, 15);
      Border compound = new CompoundBorder(line, margin);  
      
      /// Fonts
      
      Font leelawadeeee =  new Font("Leelawadee UI", Font.PLAIN, 11);   
      Font leelawadeee = new Font("Leelawadee UI", Font.PLAIN, 12);
      Font leelawadee = new Font("Leelawadee UI", Font.BOLD, 15);
      
      /// Text Fields
      
      keyInput = new JTextField(10);
      keyInput.setFont(leelawadeee);
      keyInput.setBorder(compound);
      keyInput.setSize(335,40);
      keyInput.setLocation(20, 150);
      keyInput.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void removeUpdate(DocumentEvent e) {
           validateInput();
         }
         @Override
         public void insertUpdate(DocumentEvent e) {
           statusBox.setText("Validating...");
           validateInput();
         }
         @Override
         public void changedUpdate(DocumentEvent e) {}
      });
      
      /// Text Areas
      
      statusBox = new JTextArea("Standing by...");
      statusBox.setFont(leelawadeee);
      statusBox.setBorder(compound);
      statusBox.setLineWrap(true);
      statusBox.setWrapStyleWord(false);
      statusBox.setEditable(false);
      statusBox.setSize(335,106);
      statusBox.setLocation(20, 20);
      
      /// Labels
      
      keyInputStatus = new JLabel("Kindly enter a keyword here.");
      keyInputStatus.setFont(leelawadeeee);
      keyInputStatus.setSize(335,30);
      keyInputStatus.setLocation(20, 125);
      
      /// Buttons
      
      /* Select Button */
      select = new JButton("Select");
      select.setFont(leelawadee);
      select.setForeground(Color.decode("#cacaca"));
      select.setBackground(Color.decode("#353535"));
      select.setBorder(compound);
      select.setSize(105,40);
      select.setLocation(20, 205);
      select.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae) {
            JFileChooser choose = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            choose.setDialogTitle("Select");
            choose.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Documents", "txt");
            choose.addChoosableFileFilter(filter);
            int response = choose.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
               selectedFile = choose.getSelectedFile();
               String fileName = selectedFile.toString();
               statusBox.setText("Currently selected:\n" + fileName);
               if (!selectedFile.isFile()) {
                  statusBox.setText("The selected file doesn't exist.\nPlease try again.");
                  clearFiles();
                  isOpen = false;
               } else if (isValid == true) {
                  enableButtons();
                  statusBox.setText("Ready.\nKindly select encipher or decipher to begin.");
               } else {
                  isOpen = true;
               }
            } else {
               statusBox.setText("File selection was cancelled.");
               disableButtons();
               clearFiles();
               isOpen = false;
            };
         }
      });
      
      /* Encipher Button */
      encipher = new JButton("Encipher");
      encipher.setFont(leelawadee);
      encipher.setForeground(Color.decode("#cacaca"));
      encipher.setBackground(Color.decode("#353535"));
      encipher.setBorder(compound);
      encipher.setSize(105,40);
      encipher.setLocation(135, 205);
      encipher.setEnabled(false);
      encipher.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae) {
            JFileChooser choose = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            choose.setDialogTitle("Encipher");
            choose.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Documents", "txt");
            choose.addChoosableFileFilter(filter);
            int response = choose.showSaveDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
               File select = choose.getSelectedFile();
               try {
                  if (!select.isFile()) {
                     String path = select.getAbsolutePath();
                     verifyFileExtension(path);
                  } else {
                     newFile = select;
                  }
                  if (newFile.equals(selectedFile)) {
                     statusBox.setText("Cannot overwrite the same file.\nPlease try again.");
                     disableButtons();
                     clearFiles();
                     return;
                  } 
                  String key = keyInput.getText();
                  encipher(key, newFile);
                  disableButtons();
                  clearFiles();
               } catch (IOException e) {
                  e.printStackTrace();
               }
            } else {
               statusBox.setText("Encipher was cancelled.\n");
               disableButtons();
            };
         }
      });

      /* Decipher Button */
      decipher = new JButton("Decipher");
      decipher.setFont(leelawadee);
      decipher.setForeground(Color.decode("#cacaca"));
      decipher.setBackground(Color.decode("#353535"));
      decipher.setBorder(compound);
      decipher.setSize(105,40);
      decipher.setLocation(250, 205);
      decipher.setEnabled(false);
      decipher.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae) {
            JFileChooser choose = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            choose.setDialogTitle("Decipher");
            choose.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Documents", "txt");
            choose.addChoosableFileFilter(filter);
            int response = choose.showSaveDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
               File select = choose.getSelectedFile();
               try {
                  if (!select.isFile()) {
                     String path = select.getAbsolutePath();
                     verifyFileExtension(path);
                  } else {
                     newFile = select;
                  }
                  if (newFile.equals(selectedFile)) {
                     statusBox.setText("Cannot overwrite the same file.");
                     disableButtons();
                     clearFiles();
                     return;
                  } 
                  String key = keyInput.getText();
                  decipher(key, newFile);
                  disableButtons();
                  clearFiles();
               } catch (IOException e) {
                  e.printStackTrace();
               }
            } else {
               statusBox.setText("Encipher was cancelled.\n");
               disableButtons();
            };
         }
      });
      
      // Background
      backgroundPanel = new JPanel() {  
         public void paintComponent(Graphics g) {  
         Image img = Toolkit.getDefaultToolkit().getImage(  
            Cipher.class.getResource("/com/resources/frame/background.jpg"));  
            g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);  
         }  
      };  
      backgroundPanel.setBorder(new EmptyBorder(5, 5, 5, 5));  
      backgroundPanel.setLayout(new BorderLayout(0, 0));  
      setContentPane(backgroundPanel);
      
      // Components
      backgroundPanel.setLayout(null);
      add(select);
      add(encipher);
      add(decipher);
      add(keyInput);
      add(keyInputStatus);
      add(statusBox);
      
      // Main Frame
      setTitle("Caesar–Myszkowski Cipher v2.2");
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setResizable(false);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
      setBounds(100, 100, 500, 300);
      
      // Icon
      Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/resources/frame/icon.png"));
      setIconImage(icon.getScaledInstance(75, 75, java.awt.Image.SCALE_SMOOTH));
      
   }// Cipher() end

   /** Disables the encipher and decipher buttons. */
   private void disableButtons() {
      encipher.setEnabled(false);
      decipher.setEnabled(false);
   }
   
   /** Enables the encipher and decipher buttons. */
   private void enableButtons() {
      encipher.setEnabled(true);
      decipher.setEnabled(true);
   }
   
   /** Clears all the files. */
   private void clearFiles() {
      selectedFile = null;
      newFile = null;
      isOpen = false;
      keyInput.setText("");
      keyInputStatus.setText("Kindly enter a keyword here.");
   }
   
   /**
   *  Tests whether a certain keyword is uppercase or not.
   *
   *  @param key a certain keyword
   *  @return {@code true} if the keyword is indeed uppercase, otherwise {@code false}
   */
   private boolean isAllUpper(String key) {
      for(char c : key.toCharArray()) {
         if(Character.isLetter(c) && Character.isLowerCase(c)) {
            return false;
         }
      }
      return true;
   }
   
   /**
   *  Tests whether every characters of a certain keyword are letters or not.
   *
   *  @param key a certain keyword
   *  @return {@code true} if the entire keyword comprises letters, otherwise {@code false}
   */
   private boolean isLetter(String key) {
      for(char c : key.toCharArray()) {
         if(Character.isDigit(c) || !Character.isLetter(c)) {
            return false;
         }
      }
      return true; 
   }
   
   /**
   *  Tests whether a certain keyword has recurring letters or not.
   *
   *  @param key a certain keyword
   *  @return {@code true} if the entire keyword has atleast one letter, otherwise {@code false}
   */
   private boolean hasRecurringLetter(String key) {
      char[] keys = key.toCharArray();
      int[] frequency = new int[26];
      for (int i = 0; i < keys.length; i++) {
         frequency[keys[i] - 65]++;
      }
      for (int i = 0; i < 26; i++) {
         if (frequency[i] > 1) {
            return true;
         }
      }
      return false;
   }
   
   /**
   *  Tests whether a certain letter in a keyword exceeds recurring count or not.
   *
   *  @param key a certain keyword
   *  @return {@code true} if the there are too much recurring letters, otherwise {@code false}
   */
   private boolean hasManyRecurringLetter(String key) {
      char[] keys = key.toCharArray();
      int[] frequency = new int[26];
      for (int i = 0; i < keys.length; i++) {
         frequency[keys[i] - 65]++;
      }
      for (int i = 0; i < 26; i++) {
         if (frequency[i] > 3 && frequency[i] <= keys.length) {
            return true;
         }
      }
      return false;
   }

   /** Tests whether the typed keyword is valid or not */
   private void validateInput() {
      String key = keyInput.getText();
      if (key.length() < 5 || key.length() > 10) {
         keyInputStatus.setText("The keyword must be five to ten letters.");
         isValid = false;
      }
      else if (!isAllUpper(key)) {
         keyInputStatus.setText("Every letter in the keyword must be in uppercase.");
         isValid = false;
      }
      else if (!isLetter(key)) {
         keyInputStatus.setText("The keyword must only have alphabetic letters.");
         isValid = false;
      }
      else if (!hasRecurringLetter(key)) {
         keyInputStatus.setText("The keyword must have at least one recurring letter.");
         isValid = false;
      }
      else if (hasManyRecurringLetter(key)) {
         keyInputStatus.setText("There are too much recurring letters in the keyword.");
         isValid = false;
      }
      else {
         keyInputStatus.setText("The keyword is valid.");
         statusBox.setText("Standing by...");
         isValid = true;
         if(isOpen == true) {
            enableButtons();
            statusBox.setText("Ready.\nKindly select encipher or decipher to begin.");
         }
      }
   }
   
   /**
   *  Verifies the file if it has correct file extension, otherwise it corrects the file directory.
   *
   *  @param path a certain file path
   */
   private void verifyFileExtension(String path) {
      if (path.lastIndexOf(".") == -1) {
         path = path.concat(".txt");
         newFile = new File(path);
      }
      else if (!path.matches("(?i).*\\.txt")) {
         path = path.substring(0, path.lastIndexOf(".") + 1).concat("txt");
         newFile = new File(path);
      }
   }
   
   /**
   *  Encrypts the selected text file and writes the ciphertext into a new file.
   *
   *  @param key a certain valid keyword
   *  @param fileName the newly created file's name
   *  @throws IOException if the file is not found
   */
   public void encipher(String key, File fileName) throws IOException {
      caesar = new Caesar();
      myszkowski = new Myszkowski();
      try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {   
         writer = new FileWriter(fileName);
         String line = reader.readLine();
         while(line != null) {
            line = caesar.encipher(line, key);
            line = myszkowski.encipher(line, key);
            writer.write(line + "\n");
            line = reader.readLine();
         }
         statusBox.setText("Encipher completed!\n\nThe enciphered text was written on\n" + fileName);
         disableButtons();
         clearFiles();
         writer.close();
      } catch (FileNotFoundException e){
         e.printStackTrace();
      }
   }
   
   /**
   *  Decrypts the selected text file and writes the deciphered text into a new file.
   *
   *  @param key a certain valid keyword
   *  @param fileName the newly created file's name
   *  @throws IOException if the file is not found
   */
   public void decipher(String key, File fileName) throws IOException {
      caesar = new Caesar();
      myszkowski = new Myszkowski();
      try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
         writer = new FileWriter(fileName);
         String line = reader.readLine();
         while(line != null) {
            line = caesar.decipher(line, key);
            line = myszkowski.decipher(line, key);
            writer.write(line + "\n");
            line = reader.readLine();
         }
         statusBox.setText("Decipher completed!\n\nThe deciphered text was written on\n" + fileName);
         disableButtons();
         clearFiles();
         writer.close();
      } catch (FileNotFoundException e){
         e.printStackTrace();
      }
   }
   
   /** Executes the program and runs it until its window is closed. */
   public static void main(String[] args) {  
      EventQueue.invokeLater(new Runnable() {  
         public void run() {  
            try {  
               Cipher window = new Cipher();  
               window.setLocationRelativeTo(null);
               window.setVisible(true);  
            } catch (Exception e) {  
               e.printStackTrace();  
            }  
         }  
      });  
   } 
}  