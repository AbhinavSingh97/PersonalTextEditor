import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.util.Scanner;
import java.io.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.*;
import java.util.*;

/**
 * This class will create the text editor and open up the window in which you can
 * open and edit files to your hearts desire.
 *
 * @author Abhinav Singh and Dmitriy Timokhin
 *
 * @version 1
 *
 */
public class MyTextEditor extends JFrame implements ActionListener
{
   private JPanel panel = new JPanel(new BorderLayout());
   private JTextPane textArea = new JTextPane();
   private Functions functionChooser = new Functions();
   // private TextLineNumber tln = new TextLineNumber(textArea);
   private static final Color TA_BKGRD_CL = Color.BLACK;
   private static final Color TA_FRGRD_CL = Color.GREEN;
   private static final Color TA_CARET_CL = Color.WHITE;

   //private String extOfFile;
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


   /**
    * This constructor will create the JTextPane for you to use to edit and 
    * write your code, it will create a MyTextEditor Object. 
    */
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
      textArea.setDocument(new TabsToSpaces());

      final LineNumberingTextArea lineNTA = new LineNumberingTextArea(textArea);
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
      lineNTA.setFont(new Font("Consolas", Font.BOLD, 14));
      lineNTA.setEditable(false);
      lineNTA.setVisible(true);
      textArea.add(lineNTA);
      scrollPane = new JScrollPane(textArea);
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
      replace.setShortcut(new MenuShortcut(KeyEvent.VK_R, false));
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
   
   /**
    * This method will do a sepcific action based on what the user does/clicks.
    *
    * @param event This parameter is the users actions which can be either a click
    * on  a label item or a command using keys.
    *
    */
   public void actionPerformed(ActionEvent event)
   {
      if(event.getSource() == this.closeFile)
      {
         this.dispose();
         System.out.println("Thank you for using Zenith");
      }
      else if(event.getSource() == this.openFile)
      {
         functionChooser.openFile(textArea);
      }
      else if(event.getSource() == this.saveFile)
      {
         functionChooser.saveFile(textArea);
      }
      else if(event.getSource() == this.newFile)
      {
         functionChooser.newFile(textArea);
      }
      else if(event.getSource() == this.search)
      {
         functionChooser.search(textArea);
      }
      else if(event.getSource() == this.replace)
      {
         functionChooser.replace(textArea);
         try{
         functionChooser.findKeyWords(functionChooser.extOftheFile(), textArea);
         throw new FileNotFoundException();
         }catch(FileNotFoundException ex)
         {

         }
      }
      else if(event.getSource() == this.goToLine)
      {
         functionChooser.goToLine(textArea);
      }
      else if(event.getSource() == this.copy)
      {
         functionChooser.copyArea(textArea);
      }
      else if(event.getSource() == this.paste)
      {
         functionChooser.pasteArea(textArea);
      }
      else if(event.getSource() == this.cut)
      {
         functionChooser.cutArea(textArea);
      }
      else if(event.getSource() == this.documentation)
      {
         functionChooser.loadJavaDocs();
      }
      else if(event.getSource() == this.delete)
      {
         System.out.println("Lmao, you thought I deleted your line, more like your file");
         dispose();
      }
   }
   
   static class TabsToSpaces extends DefaultStyledDocument
      {
         public void insertString(int offset, String str, AttributeSet att)
         {
            try
            {
               str = str.replaceAll("\t", "   ");
               super.insertString(offset,str,att);
            }
            catch(BadLocationException ex)
            {

            }
         }
      }
   public static void main(String[] args)
   {
      MyTextEditor textEditor = new MyTextEditor();
      textEditor.setVisible(true);
   }
}