import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.util.Scanner;
import java.io.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.JTextComponent;
import javax.swing.text.*;
import java.net.URI;
import java.util.*;

/**
 * This class has all of the functions that our text editor can do such as search
 * replace -- etc.
 */
public class Functions
{
   private static String OS = System.getProperty("os.name").toLowerCase();
   private String extOfFile;
   

   /**
    * This methid is your standard copy function that will copy certain 
    * parts of the textPane.
    *
    * @param text This parameter is the textPane which is where the user 
    * write their code.
    */
   public void copyArea(JTextPane text)
   {
      text.copy();
   }
   /**
    * This is the standard paste method which will paste certain text that
    * you have already copied onto your textPane.
    *
    * @param text This parameter is the textpane which is where the user write 
    * their code.
    *
    */
   public void pasteArea(JTextPane text)
   {
      text.paste();
   }
   /**
    * This is the standard cut method which will cut certain text that
    * you have already in your textPane.
    *
    * @param text This parameter is the textpane which is where the user write 
    * their code.
    *
    */
   public void cutArea(JTextPane text)
   {
      text.cut();
   }
   /**
    * This method is what our open file uses to find the index of the period 
    * for which to find the extention of the file with.
    * 
    * @param string This imput will be the filename we get from when we try to open
    * a file.
    *
    * @return This method will return the index value of the period in the filename.
    */
   private int findIndexOfWord(String string)
   {
      char[] myChar = string.toCharArray();
      char[] myChar2 = new char[myChar.length];
      int m = 0;
      for(int i = myChar.length - 1; i >= 0; i--)
      {
         myChar2[m] = myChar[i];
         m++;
      }
      int z = -1;
      for(char k : myChar2)
      {

         if(k == '.')
         {
            return myChar.length - z;
         }
         z++;
      }
      return -1;
   }
   /**
    * This method will open the file you want to see in our JTextPane.
    *
    * @param text This parameter is the textpane which is where the user write 
    * their code.
    *
    */
   public void openFile(JTextPane text)
   {
      JFileChooser open = new JFileChooser();
      int option = open.showOpenDialog(text);
      String filename = open.getSelectedFile().getName();
      int indexOfWord = findIndexOfWord(filename);
      String ext = filename.substring(indexOfWord - 1,filename.length());
      extOfFile = ext;
      int total = 0;
      if(option == JFileChooser.APPROVE_OPTION)
      {
         text.setText("");
         try
         {
            StyledDocument doc = text.getStyledDocument();
            File file = new File(filename);
            Scanner scanner = new Scanner(new FileReader(open.getSelectedFile().getPath()));
            while(scanner.hasNext())
            {
               String m = scanner.nextLine();
               doc.insertString(total,m + "\n",null);
               total += m.length() + 1;
            }
         }
         catch(Exception ex)
         {
            System.out.println(ex.getMessage());
         }
      }
     
      try
         {
         findKeyWords(ext,text);
         throw new FileNotFoundException();
         }catch(FileNotFoundException ex)
         {

         }
     
   }
   /**
    * This method will return the extension of the file we are opening.
    * 
    * @return This method will return the extension of the file. 
    */
   public String extOftheFile()
   {
      return extOfFile;
   }
   /**
    * This method will save the file you are currently using to wherever you please.
    *
    * @param text This parameter is the textpane which is where the user write 
    * their code.
    *
    */
   public void saveFile(JTextPane text)
   {
      JFileChooser save = new JFileChooser();
      int option = save.showSaveDialog(text);
      String filename = save.getSelectedFile().getName();
      int indexOfWord = findIndexOfWord(filename);
      String ext = filename.substring(indexOfWord - 1,filename.length());
      extOfFile = ext;
      if(option == JFileChooser.APPROVE_OPTION)
      {
         try
            {
            BufferedWriter output = new BufferedWriter(new FileWriter(save.getSelectedFile().getPath()));
            output.write(text.getText());
            output.close();
         }
         catch(Exception ex)
         {
            System.out.println(ex.getMessage());
         }
      }
       try
         {
         findKeyWords(ext,text);
         throw new FileNotFoundException();
         }catch(FileNotFoundException ex)
         {

         }
   }
   /**
    * This method will create a newFile that you can edit in the JTextPane.
    *
    * @param text This parameter is the textpane which is where the user write 
    * their code.
    *
    */
   public void newFile(JTextPane text)
   {
      JFileChooser newF = new JFileChooser();
      int option = newF.showSaveDialog(text);
      if(option == JFileChooser.APPROVE_OPTION)
      {
         try
         {
            BufferedWriter output = new BufferedWriter(new FileWriter(newF.getSelectedFile().getPath()));
            output.write(text.getText());
            output.close();
            text.setText("");
         }
         catch(Exception ex)
         {
            System.out.println(ex.getMessage());
         }
      }  
   }
   /**
    * This is the search function that will find the word you are looking for
    * and will highlight it. 
    *
    * @param text This parameter is the textpane which is where the user write 
    * their code.
    *
    */
    public void search(JTextPane text) 
   { 
      String wordToSearch = JOptionPane.showInputDialog(null, "Word to search for:");
      Highlighter highlighter = text.getHighlighter();
      highlighter.removeAllHighlights();
      HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
     
      int m;
      int total = 0;
      for (String line : text.getText().split("\n")) 
      {
         m = line.indexOf(wordToSearch);
         if(m == -1)
         {
            if(isUnix())
            {
            total += line.length() + 1;
            }
            else if(isWindows())
            {
               total += line.length();
            }
            else if(isMac())
            {
               total += line.length() + 1;
            }
            else
            {
               total += line.length() + 1;
            }
            continue;
         }
         try{
         highlighter.addHighlight(total + m, total + m + wordToSearch.length(), painter);
         }catch(BadLocationException ex)
         {}
         while(true)
         {
            m = line.indexOf(wordToSearch, m + 1);

            if (m == -1)
            {
               
               break;
            }
            try{
            highlighter.addHighlight(total + m, total + m + wordToSearch.length(), painter);
            }catch(BadLocationException e)
            {
            }
         }
         if(isUnix())
            {
            total += line.length() + 1;
            }
            else if(isWindows())
            {
               total += line.length();
            }
            else if(isMac())
            {
               total += line.length() + 1;
            }
            else
            {
               total += line.length() + 1;
            }
            continue;
      }
   }
   /**
    * This is the replace function which will replace the word or charecter you want
    * with another word or charecter.
    *
    * @param text This parameter is the textpane which is where the user write 
    * their code.
    *
    */
   public void replace(JTextPane text)
   {
      String wordToSearch = JOptionPane.showInputDialog(null, "Word to replace:");
      String wordToReplace = JOptionPane.showInputDialog(null, "Replacement word:");

      int m;
      int total = 0;
      int wordLength = wordToSearch.length();
      for (String line : text.getText().split("\n")) 
      {
         m = line.indexOf(wordToSearch);
         if(m == -1)
         {
            total += line.length() + 1; 
            continue;
         }
         
         String newLine = line.replaceAll(wordToSearch, wordToReplace);
         Document myText = text.getDocument();
         try
         {
         myText.remove(total, line.length());
         myText.insertString(total, newLine, null);
         }
         catch(BadLocationException ex)
         {

         }
         total += newLine.length() + 1;
      }
   }
   /**
    * This method will put your cursor on the line that you specify. 
    *
    * @param text This parameter is the textpane which is where the user write 
    * their code.
    *
    */
   public void goToLine(JTextPane text)
   {
       String lineToSearch = JOptionPane.showInputDialog(null, "Line Number: ");
       int total = 0;
       int lineNum = 0;
       for (String line : text.getText().split("\n"))
       {
         lineNum +=1;
         String lineNumStr = lineNum + "";
         if (lineNumStr.compareTo(lineToSearch) == 0)
         {
            if( line == null)
            {
               text.setCaretPosition(total);
            }
            text.setCaretPosition(total);
            break;
         }
         total += line.length() + 1;

       }
   }
   /**
    * This method will find all of the KeywWords, Operators, and types in your
    * file and color them accordingly. 
    *
    *
    * @param directory This is the extension of the file that you are opening.
    *
    * @param text This parameter is the textpane which is where the user write 
    * their code.
    * 
    * @throws FileNotFoundException If there the file specified is not found.
    *
    */
    public void findKeyWords(String directory, JTextPane text) throws FileNotFoundException
   {
      final StyleContext cont = StyleContext.getDefaultStyleContext();
      final AttributeSet jKeyWord = cont.addAttribute(cont.getEmptySet(), 
         StyleConstants.Foreground,Color.RED);
      final AttributeSet jOperator = cont.addAttribute(cont.getEmptySet(), 
         StyleConstants.Foreground,Color.MAGENTA);
      final AttributeSet jtypes = cont.addAttribute(cont.getEmptySet(), 
         StyleConstants.Foreground,Color.CYAN);

         ArrayList<String> words = loadKeyWords(directory);
         for (String line : words)
         {
            searchJava(line,jKeyWord,text);
         }
         ArrayList<String> operators = loadOperators(directory);
         for (String line : operators)
         {
            searchJava(line, jOperator,text);
         }
         ArrayList<String> types1 = loadTypes(directory);
         for (String line : types1)
         {
            searchJava(line, jtypes,text);
         }
   }
   public void updateTextArea(JTextPane text) throws FileNotFoundException
   {
      final StyleContext cont = StyleContext.getDefaultStyleContext();
      final AttributeSet jKeyWord = cont.addAttribute(cont.getEmptySet(), 
         StyleConstants.Foreground,Color.RED);
      final AttributeSet jOperator = cont.addAttribute(cont.getEmptySet(), 
         StyleConstants.Foreground,Color.MAGENTA);
      final AttributeSet jtypes = cont.addAttribute(cont.getEmptySet(), 
         StyleConstants.Foreground,Color.CYAN);

         ArrayList<String> words = loadKeyWords(extOfFile);
         for (String line : words)
         {
            searchJava(line,jKeyWord,text);
         }
         ArrayList<String> operators = loadOperators(extOfFile);
         for (String line : operators)
         {
            searchJava(line, jOperator,text);
         }
         ArrayList<String> types1 = loadTypes(extOfFile);
         for (String line : types1)
         {
            searchJava(line, jtypes,text);
         }

   }
   /**
    * This function will put all of the keywords in your specific textfile into
    * an ArrayList<String> to be used later. 
    *
    * @param text This parameter is the textpane which is where the user write 
    * their code.
    *
    * @return This function will return an ArrayList of all of the words specified
    * in your textfile for KeyWords.
    *
    */
   private ArrayList<String> loadKeyWords(String directory) throws FileNotFoundException
   {
      ArrayList<String> javaWords = new ArrayList<String>();
      final String dir = System.getProperty("user.dir");
      File file = new File(dir + "/" + directory + "/keywords.txt");
      Scanner scan = new Scanner(file);
      while(scan.hasNext())
      {
         javaWords.add(scan.next());
      }
      scan.close();
      return javaWords;
   }
   /**
    * This function will put all of the Operators in your specific textfile into
    * an ArrayList<String> to be used later. 
    *
    * @param text This parameter is the textpane which is where the user write 
    * their code.
    *
    * @return This function will return an ArrayList of all of the words specified
    * in your textfile for Operators.
    */
   private ArrayList<String> loadOperators(String directory) throws FileNotFoundException
   {
      ArrayList<String> javaWords = new ArrayList<String>();
      final String dir = System.getProperty("user.dir");
      File file = new File(dir + "/" + directory + "/operators.txt");
      Scanner scan = new Scanner(file);
      while(scan.hasNext())
      {
         javaWords.add(scan.next());
      }
      scan.close();
      return javaWords;
   }
   /**
    * This function will put all of the Types in your specific textfile into
    * an ArrayList<String> to be used later. 
    *
    * @param text This parameter is the textpane which is where the user write 
    * their code.
    *
    * @return This function will return an ArrayList of all of the words specified
    * in your textfile for Types.
    */
   private ArrayList<String> loadTypes(String directory) throws FileNotFoundException
   {
      ArrayList<String> javaWords = new ArrayList<String>();
      final String dir = System.getProperty("user.dir");
      File file = new File(dir + "/" + directory + "/types.txt");
      Scanner scan = new Scanner(file);
      while(scan.hasNext())
      {
         javaWords.add(" " + scan.next());
      }
      scan.close();
      return javaWords;
   }
   /**
    * This function will search the file for the words specified and color them.  
    *
    * @param text This parameter is the textpane which is where the user write 
    * their code.
    *
    * @param javaAttr This is the attribute that you will add to the words in the textPane.
    * 
    * @param wordToSearch This is the word that we will apple the new color too in
    * findKeyWords.
    */
    public void searchJava(String wordToSearch, AttributeSet javaAttr, JTextPane text) 
   { 
      
      final AttributeSet attr = javaAttr;
      Document myText = text.getDocument();
      final StyleContext cont = StyleContext.getDefaultStyleContext();
      final AttributeSet reset = cont.addAttribute(cont.getEmptySet(), 
         StyleConstants.Foreground,Color.GREEN);

      int m;
      int t;
      int total = 0;
      for (String line : text.getText().split("\n")) 
      {
         m = line.indexOf(wordToSearch);
         if(m == -1)
         {
            if(isUnix())
            {
            total += line.length() + 1;
            }
            else if(isWindows())
            {
               total += line.length();
            }
            else if(isMac())
            {
               total += line.length() + 1;
            }
            else
            {
               total += line.length() + 1;
            }
            continue;
         }
         try{
         myText.remove(total + m, wordToSearch.length());

         myText.insertString(total + m, wordToSearch, attr);
         }catch(BadLocationException ex)
         {}
         while(true)
         {
            m = line.indexOf(wordToSearch, m + 1 );

            if (m == -1)
            {
               break;
            }
            try
            {
            myText.remove(total + m, wordToSearch.length());

            myText.insertString(total + m, wordToSearch, attr);
            }catch(BadLocationException e)
            {
            }
         }
         if(isUnix())
            {
               total += line.length() + 1;
               
            }
            else if(isWindows())
            {
               total += line.length();
            }
            else if(isMac())
            {
               total += line.length() + 1;
            }
            else
            {
               JOptionPane.showMessageDialog(null, "Eric You Troll" );
               total += line.length() + 1;
            }
            try
            {
             myText.insertString(total,"", reset);
            }
            catch(BadLocationException ex)
            {
              
            }

      }
   }
    private static boolean isWindows() 
   {

      return (OS.indexOf("win") >= 0);

   }
   private static boolean isLinux() 
   {

      return (OS.indexOf("Lin") >= 0);

   }
   private static boolean isUnix() {

      return (OS.indexOf("nix") >= 0 ||
       OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
      
   }
   private static boolean isMac() {

      return (OS.indexOf("mac") >= 0);

   }
   public void loadJavaDocs()
   {
      try
      {
         Desktop desktop = Desktop.getDesktop();
         URI uRL = new URI("http://users.csc.calpoly.edu/~dtimokhi/");
         desktop.browse(uRL);
      }
      catch(Exception ex)
      {
         ex.printStackTrace();
      }
   }
}