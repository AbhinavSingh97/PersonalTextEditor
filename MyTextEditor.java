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

public class MyTextEditor extends JFrame implements ActionListener
{
   private JPanel panel = new JPanel(new BorderLayout());
   private JTextPane textArea = new JTextPane();
   private JTextArea lineText = new JTextArea();
   // private TextLineNumber tln = new TextLineNumber(textArea);
   private static final Color TA_BKGRD_CL = Color.BLACK;
   private static final Color TA_FRGRD_CL = Color.GREEN;
   private static final Color TA_CARET_CL = Color.WHITE;

   private JScrollPane scrollPane;
   private MenuBar menuBar = new MenuBar(); 
   private Menu file = new Menu();
   private Menu edit = new Menu();
   private Menu find = new Menu();
   private Menu goTo = new Menu();
   private Menu help = new Menu();
   private Menu secrets = new Menu();
   //File 
   private MenuItem openFile = new MenuItem(); 
   private MenuItem saveFile = new MenuItem(); 
   private MenuItem closeFile = new MenuItem();
   private MenuItem newFile = new MenuItem(); 
   //Edit
   private MenuItem copy = new MenuItem();
   private MenuItem paste = new MenuItem();
   private MenuItem cut = new MenuItem();
   private MenuItem search = new MenuItem();
   private MenuItem replace = new MenuItem();
   //GoTo
   private MenuItem goToLine = new MenuItem();
   //Help
   private MenuItem documentation = new MenuItem();
   //Secret Commands 
   private MenuItem delete = new MenuItem();



   public MyTextEditor()
   {
      this.setSize(750,800);
      this.setTitle("Zenith");
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      textArea.setFont(new Font("Consolas", Font.BOLD, 14));
      textArea.setForeground(TA_FRGRD_CL);
      textArea.setBackground(TA_BKGRD_CL);
      textArea.setCaretColor(TA_CARET_CL);
      textArea.getCaret().setVisible(true);      
      final LineNumberingTextArea lineNTA = new LineNumberingTextArea(lineText);
      DocumentListener documentListen = new DocumentListener()
      {
         public void insertUpdate(DocumentEvent documentEvent)
         {
           lineNTA.updateLineNumbers();  
         }
         public void removeUpdate(DocumentEvent documentEvent)
         {
            lineNTA.updateLineNumbers();
         }
         public void changedUpdate(DocumentEvent documentEvent)
         {
            lineNTA.updateLineNumbers();
         }
      };
      textArea.getDocument().addDocumentListener(documentListen);
      //Line numbers
      lineNTA.setBackground(Color.BLACK);
      lineNTA.setForeground(Color.WHITE);
      lineNTA.setFont(new Font("Consolas", Font.BOLD, 13));
      lineNTA.setEditable(false);
      lineNTA.setVisible(true);
      textArea.add(lineNTA);
      scrollPane = new JScrollPane(textArea);
      scrollPane.add(lineNTA);
      
      scrollPane.setVisible(true);
      scrollPane.setRowHeaderView(lineNTA);

      getContentPane().add(scrollPane);

      setMenuBar(this.menuBar);
      menuBar.add(this.file);
      menuBar.add(this.edit); 
      menuBar.add(this.find);
      menuBar.add(this.goTo);
      menuBar.add(this.help);
      menuBar.add(secrets);

      file.setLabel("File");
      find.setLabel("Find");
      edit.setLabel("Edit");
      goTo.setLabel("Goto");
      help.setLabel("Help");
      //File option to open files in the text editor
      openFile.setLabel("Open");
      openFile.addActionListener(this);
      openFile.setShortcut(new MenuShortcut(KeyEvent.VK_O,false));
      file.add(this.openFile);

      //File option to save the files
      saveFile.setLabel("Save");
      saveFile.addActionListener(this);
      saveFile.setShortcut(new MenuShortcut(KeyEvent.VK_S,false));
      file.add(this.saveFile);

      //File option to close the file
      closeFile.setLabel("Close");
      closeFile.addActionListener(this);
      closeFile.setShortcut(new MenuShortcut(KeyEvent.VK_W,false));
      file.add(this.closeFile);

      //File option to create a new file
      newFile.setLabel("New File");
      newFile.addActionListener(this);
      newFile.setShortcut(new MenuShortcut(KeyEvent.VK_N, false));
      file.add(newFile);

      search.setLabel("Search");
      search.addActionListener(this);
      search.setShortcut(new MenuShortcut(KeyEvent.VK_F, false));
      find.add(search);

      replace.setLabel("Replace");
      replace.addActionListener(this);
      replace.setShortcut(new MenuShortcut(KeyEvent.VK_H, false));
      find.add(replace);

      goToLine.setLabel("Go To");
      goToLine.addActionListener(this);
      goToLine.setShortcut(new MenuShortcut(KeyEvent.VK_G, false));
      goTo.add(goToLine);


      //Edit opitions 
      copy.setLabel("Copy");
      copy.addActionListener(this);
      copy.setShortcut(new MenuShortcut(KeyEvent.VK_C,false));
      edit.add(copy);

      paste.setLabel("Paste");
      paste.addActionListener(this);
      paste.setShortcut(new MenuShortcut(KeyEvent.VK_V,false));
      edit.add(paste);

      cut.setLabel("Cut");
      cut.addActionListener(this);
      cut.setShortcut(new MenuShortcut(KeyEvent.VK_X,false));
      edit.add(cut);

      //Help opition
      documentation.setLabel("Documentation");
      documentation.addActionListener(this);
      help.add(documentation);

      //Secret Opitions
      delete.addActionListener(this);
      delete.setShortcut(new MenuShortcut(KeyEvent.VK_D,false));
      secrets.add(delete);


   }

   public void actionPerformed(ActionEvent event)
   {
      if(event.getSource() == this.closeFile)
      {
         this.dispose();
         System.out.println("Thank you for using Zenith");
      }
      else if(event.getSource() == this.openFile)
      {
         openFile();
         searchJava();
      }
      else if(event.getSource() == this.saveFile)
      {
         saveFile();
      }
      else if(event.getSource() == this.newFile)
      {
         newFile();
      }
      else if(event.getSource() == this.search)
      {
         search();
      }
      else if(event.getSource() == this.replace)
      {
         replace();
      }
      else if(event.getSource() == this.goToLine)
      {
         goToLine();
      }
      else if(event.getSource() == this.copy)
      {
         copyArea();
      }
      else if(event.getSource() == this.paste)
      {
         pasteArea();
      }
      else if(event.getSource() == this.cut)
      {
         cutArea();
      }
      else if(event.getSource() == this.documentation)
      {
         loadJavaDocs();
      }
      else if(event.getSource() == this.delete)
      {
         System.out.println("Lmao, you thought I deleted your line, more like your file");
         dispose();
      }
   }
   public void openFile()
   {
      JFileChooser open = new JFileChooser();
      int option = open.showOpenDialog(this);
      String filename = open.getSelectedFile().getName();
      String[] filenameAndExt = filename.split(".");
      int total = 0;
      if(option == JFileChooser.APPROVE_OPTION)
      {
         this.textArea.setText("");
         try
         {
            StyledDocument doc = textArea.getStyledDocument();
            File file = new File(filename);
            Scanner scanner = new Scanner(new FileReader(open.getSelectedFile().getPath()));
            while(scanner.hasNext())
            {
               String m = scanner.nextLine();
               doc.insertString(total,m + "\n",null);
               total += m.length() +1;
            }
         }
         catch(Exception ex)
         {
            System.out.println(ex.getMessage());
         }
      }
     // findKeyWords(filenameAndExt[1]);
   }
   public void saveFile()
   {
      JFileChooser save = new JFileChooser();
      int option = save.showSaveDialog(this);
      if(option == JFileChooser.APPROVE_OPTION)
      {
         try
            {
            BufferedWriter output = new BufferedWriter(new FileWriter(save.getSelectedFile().getPath()));
            output.write(this.textArea.getText());
            output.close();
         }
         catch(Exception ex)
         {
            System.out.println(ex.getMessage());
         }
      }
   }
   public void newFile()
   {
      JFileChooser newF = new JFileChooser();
      int option = newF.showSaveDialog(this);
      if(option == JFileChooser.APPROVE_OPTION)
      {
         try
         {
            BufferedWriter output = new BufferedWriter(new FileWriter(newF.getSelectedFile().getPath()));
            output.write(this.textArea.getText());
            output.close();
            this.textArea.setText("");
         }
         catch(Exception ex)
         {
            System.out.println(ex.getMessage());
         }
      }
   
   }
   public void findKeyWords()
   {
      /*
      ArrayList<String> wordsInTA = new ArrayList<String>();
      int index = 0;

      //if(ext == "java")
      //{
         for(String line : textArea.getText().split(" "))
         {
            wordsInTA.add(line);
            index++;
         }
         try
         {
          
          while(index>0)
          {
               String temp = wordsInTA.get(index);
               boolean isKeyWord = binarySearch(temp);
               if(isKeyWord == true)
               {
                 
               } 
               index--;    
         }
       }
       catch(IOException ex)
         {
            ex.printStackTrace();
         }
      //}
      */
      
         ArrayList<String> words = new ArrayList<String>();
         words.add("private");
         
        

         for (String line : words)
            searchJava();
      
   }
   private ArrayList<String> loadJavaWords() throws FileNotFoundException
   {
      ArrayList<String> javaWords = new ArrayList<String>();
      Scanner scan = new Scanner(new File("JavaKeyWords.txt"));
      while(scan.hasNext())
      {
         javaWords.add(scan.next());
      }
      scan.close();
      return javaWords;
   }
   private boolean binarySearch(String word) throws FileNotFoundException
   {
      ArrayList<String> javaWords = loadJavaWords();
      int min = 0;
      int max = javaWords.size()-1;
      while(min <= max)
      {
         int index = (max + min)/2;
         String guess = javaWords.get(index);
         int result = word.compareTo(guess);
         if(result == 0)
         {
            return true;
         }
         else if(result > 0)
         {
            min = index +1;
         }
         else if(result < 0)
         {
            max = index -1;
         }
      }
      return false;
   }
    public void searchJava() 
   { 
      String wordToSearch = "public";
      //String wordToSearch = JOptionPane.showInputDialog(null, "Word to search for:");
      final StyleContext cont = StyleContext.getDefaultStyleContext();
      final AttributeSet attr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,Color.RED);
      Document text = textArea.getDocument();

      int m;
      int t;
      int total = 0;
      for (String line : textArea.getText().split("\n")) 
      {
         m = line.indexOf(wordToSearch);
         if(m == -1)
         {
            total += line.length() + 1;
            continue;
         }
         try{
         text.remove(total + m, wordToSearch.length());

         text.insertString(total + m, wordToSearch, attr);
         }catch(BadLocationException ex)
         {}
         while(true)
         {
            t = line.indexOf(wordToSearch, m + 1);

            if (t == -1)
            {
               
               break;
            }
            try
            {
            text.remove(total + t, wordToSearch.length());

            text.insertString(total + t, wordToSearch, attr);
            }catch(BadLocationException e)
            {
            }
         }
         total += line.length() + 1;
      }
   }
   public void search() 
   { 
      String wordToSearch = JOptionPane.showInputDialog(null, "Word to search for:");
      Highlighter highlighter = textArea.getHighlighter();
      highlighter.removeAllHighlights();
      HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
     
      int m;
      int total = 0;
      for (String line : textArea.getText().split("\n")) 
      {
         m = line.indexOf(wordToSearch);
         if(m == -1)
         {
            total += line.length();
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
         total += line.length();
      }
   }
   
   public void replace()
   {
      String wordToSearch = JOptionPane.showInputDialog(null, "Word to replace:");
      String wordToReplace = JOptionPane.showInputDialog(null, "Replacement word:");

      int m;
      int total = 0;
      int wordLength = wordToSearch.length();
      for (String line : textArea.getText().split("\n")) 
      {
         m = line.indexOf(wordToSearch);
         if(m == -1)
         {
            total += line.length(); 
            continue;
         }
         
         String newLine = line.replaceAll(wordToSearch, wordToReplace);
         Document text = textArea.getDocument();
         //textArea.replaceRange(newLine, total, total + line.length());
         try
         {
         text.remove(total, line.length());
         text.insertString(total, newLine, null);
         }
         catch(BadLocationException ex)
         {

         }
         total += newLine.length();
      }
   }
   public void goToLine()
   {
       String lineToSearch = JOptionPane.showInputDialog(null, "Line Number: ");
       int total = 0;
       int lineNum = 0;
       for (String line : textArea.getText().split("\n"))
       {
         lineNum +=1;
         String lineNumStr = lineNum + "";
         if (lineNumStr.compareTo(lineToSearch) == 0)
         {
            if( line == null)
            {
               textArea.setCaretPosition(total);
            }
            textArea.setCaretPosition(total);
            break;
         }
         total += line.length();

       }
   }
   public void copyArea()
   {
      textArea.copy();
   }
   public void pasteArea()
   {
      textArea.paste();
   }
   public void cutArea()
   {
      textArea.cut();
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
   public static void main(String args[])
   {
      MyTextEditor textEditor = new MyTextEditor();
      textEditor.setVisible(true);
   }
}