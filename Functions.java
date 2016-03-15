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
public class Functions
{
   private static String OS = System.getProperty("os.name").toLowerCase();

   public void copyArea(JTextPane text)
   {
      text.copy();
   }
   public void pasteArea(JTextPane text)
   {
      text.paste();
   }
   public void cutArea(JTextPane text)
   {
      text.cut();
   }
   public int findIndexOfWord(String string)
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
   public void openFile(JTextPane text)
   {
      JFileChooser open = new JFileChooser();
      int option = open.showOpenDialog(text);
      String filename = open.getSelectedFile().getName();
      int indexOfWord = findIndexOfWord(filename);
      String ext = filename.substring(indexOfWord - 1,filename.length());
      //extOfFile = ext;
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
   public void saveFile(JTextPane text)
   {
      JFileChooser save = new JFileChooser();
      int option = save.showSaveDialog(text);
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
   }
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
   private ArrayList<String> loadKeyWords(String directory) throws FileNotFoundException
   {
      ArrayList<String> javaWords = new ArrayList<String>();
      final String dir = System.getProperty("user.dir");
      File file = new File(dir + "/" + directory + "/keywords.txt");
      Scanner scan = new Scanner(file);
      while(scan.hasNext())
      {
         javaWords.add(scan.next() + " ");
      }
      scan.close();
      return javaWords;
   }
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
    public void searchJava(String wordToSearch, AttributeSet javaAttr, JTextPane text) 
   { 
      
      final AttributeSet attr = javaAttr;
      Document myText = text.getDocument();

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
      }
   }
    public static boolean isWindows() 
   {

      return (OS.indexOf("win") >= 0);

   }
   public static boolean isLinux() 
   {

      return (OS.indexOf("Lin") >= 0);

   }
   public static boolean isUnix() {

      return (OS.indexOf("nix") >= 0 ||
       OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
      
   }
   public static boolean isMac() {

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