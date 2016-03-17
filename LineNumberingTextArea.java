import javax.swing.*;
import javax.swing.text.Element;
import java.awt.*;

/**
 * This class creates the line numbers in our TextPane.
 */
public class LineNumberingTextArea extends JTextArea
{
    private JTextPane textArea;


    /**
     * This is the contructor that creates the LinNumbering TextArea.
     *
     * @param textArea The textArea that we will be modifying to add the 
     * line numbers to it.
     */
    public LineNumberingTextArea(JTextPane textArea)
    {
        this.textArea = textArea;
        setBackground(Color.BLACK);
        textArea.setFont(new Font("Consolas", Font.BOLD, 14));
        setEditable(false);
    }

    /**
     * This method will update the line numbers.
     */
    public void updateLineNumbers()
    {
        String lineNumbersText = getLineNumbersText();
        setText(lineNumbersText);
    }


    /**
     * This method will set the line numbers to show up on the JTextPane.
     *
     * @return This method will return a String which will be added to the 
     * the lineNumbering area in the JTextPane.
     */
    private String getLineNumbersText()
    {
        int counter = 0;
        int caretPosition = textArea.getDocument().getLength();
        Element root = textArea.getDocument().getDefaultRootElement();
        StringBuilder lineNumbersTextBuilder = new StringBuilder();
        lineNumbersTextBuilder.append("1").append(System.lineSeparator());

        for (int elementIndex = 2; elementIndex < root.getElementIndex(caretPosition) +2; 
            elementIndex++)
        {
            lineNumbersTextBuilder.append(elementIndex).append(System.lineSeparator());
        }
        return lineNumbersTextBuilder.toString();
    }
}