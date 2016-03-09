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

public class MyTextEditor extends JFrame implements ActionListener
{
   

	private JPanel panel = new JPanel(new BorderLayout());
	private JTextArea textArea = new JTextArea(0,0);
	private JScrollPane scrollPane = new JScrollPane(textArea, 
		          JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private MenuBar menuBar = new MenuBar(); 
	private Menu file = new Menu();
	private Menu edit = new Menu();
	private Menu find = new Menu();
   private Menu goTo = new Menu();
	//File 
	private MenuItem openFile = new MenuItem(); 
	private MenuItem saveFile = new MenuItem(); 
	private MenuItem closeFile = new MenuItem();
	private MenuItem newFile = new MenuItem(); 
	//Edit
	//private MenuItem copy = new MenuItem(new DefaultEditorKit.CopyAction());
	private MenuItem paste = new MenuItem();
	private MenuItem cut = new MenuItem();
	private MenuItem search = new MenuItem();
	private MenuItem replace = new MenuItem();
   //GoTo
	private MenuItem goToLine = new MenuItem();



	public MyTextEditor()
	{

     


		//Set the basis for the text editor
		this.setSize(750,800);
		this.setTitle("Zenith");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		textArea.setFont(new Font("Consolas", Font.BOLD, 14));
		textArea.setForeground(Color.GREEN);
		textArea.setBackground(Color.BLACK);
		//scrollPane.setBounds(20, 30, 100, 40);
		textArea.getCaret().setVisible(true);
		scrollPane.setVisible(true);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		//scrollPane.getHorizontalScrollBar().isVisible();
		//scrollPane.getVerticalScrollBar().isVisible();

		//textArea.add(scrollPane,BorderLayout.EAST);
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
		// Line numbers
		lineNTA.setBackground(Color.BLACK);
		lineNTA.setForeground(Color.WHITE);
		lineNTA.setFont(new Font("Consolas", Font.BOLD, 13));
		lineNTA.setEditable(false);
		lineNTA.setVisible(true);
		//getContentPane().setLayout(new BorderLayout());
		//scrollPane.setRowHeaderView(lineNTA);
		scrollPane.setVisible(true);
		getContentPane().add(textArea);
		getContentPane().add(lineNTA,BorderLayout.WEST);
		//panel.add(lineNumber,BorderLayout.EAST);
		//Numbers along the side

		setMenuBar(this.menuBar);
		menuBar.add(this.file);
		menuBar.add(this.edit); 
		menuBar.add(this.find);
      menuBar.add(this.goTo);
		file.setLabel("File");
		find.setLabel("Find");
		edit.setLabel("Edit");
      goTo.setLabel("Goto");
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
		//copy.setLabel("Copy");
		//copy.setMnemonic(KeyEvent.VK_C);
		//edit.add(this.copy);


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
	}
	public void openFile()
	{
		JFileChooser open = new JFileChooser();
		int option = open.showOpenDialog(this);
		String filename = open.getSelectedFile().getName();
		String[] filenameAndExt = filename.split(".");
		if(option == JFileChooser.APPROVE_OPTION)
		{
			this.textArea.setText("");
			try
			{
				Scanner scanner = new Scanner(new FileReader(open.getSelectedFile().getPath()));
				while(scanner.hasNext())
				{
					this.textArea.append(scanner.nextLine() + "\n");
				}
			}
			catch(Exception ex)
			{
				System.out.println(ex.getMessage());
			}
			highlightKeyWords(filenameAndExt[1]);
		}
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
	public void highlightKeyWords(String ext)
	{
		if(ext == "java")
		{
			try
      	{
        	 File myFile = new File("JavaKeyWords.txt");
        	 Scanner scan = new Scanner(myFile);
        	 while(scan.hasNext())
        	 {
            	String temp = scan.next();
                /*
            	if(temp == )
            	{
            		
            	}
            	*/
         }
     	 }
     	 catch(IOException ex)
      	{
      	   ex.printStackTrace();
      	}
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
      for (String line : textArea.getText().split("\\n")) 
      {
      	m = line.indexOf(wordToSearch);
      	if(m == -1)
      	{
      		total += line.length() + 1;
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
      	total += line.length() + 1;
      }
	}
	public void replace()
	{
		String wordToSearch = JOptionPane.showInputDialog(null, "Word to replace:");
		String wordToReplace = JOptionPane.showInputDialog(null, "Replacement word:");

      int m;
      int total = 0;
      int wordLength = wordToSearch.length();
      for (String line : textArea.getText().split("\\n")) 
      {
      	m = line.indexOf(wordToSearch);
      	if(m == -1)
      	{
      		total += line.length() + 1; 
      		continue;
      	}
      	
         String newLine = line.replaceAll(wordToSearch, wordToReplace);
         textArea.replaceRange(newLine, total, total + line.length());
      	total += newLine.length() + 1;
      }
	}
	public void goToLine()
	{
		 String lineToSearch = JOptionPane.showInputDialog(null, "Line Number: ");
		 int total = 0;
		 int lineNum = 0;
       for (String line : textArea.getText().split("\\n"))
       {
       	lineNum +=1;
       	String lineNumStr = lineNum + "";
       	if (lineNumStr.compareTo(lineToSearch) == 0)
       	{
       		if( line == null)
       		{
       			textArea.setCaretPosition(total-2);
       		}
       		textArea.setCaretPosition(total);
       		break;
       	}
       	total += line.length() + 1;

       }
 

	}

	public static void main(String args[])
	{
		MyTextEditor textEditor = new MyTextEditor();
		textEditor.setVisible(true);
	}
}