import javax.swing.*;
import javax.swing.text.Element;
import java.awt.*;

public class LineNumberingTextArea extends JTextArea
{
    private JTextPane textArea;

    public LineNumberingTextArea(JTextPane textArea)
    {
        this.textArea = textArea;
    }

    public void updateLineNumbers()
    {
        String lineNumbersText = getLineNumbersText();
        setText(lineNumbersText);
    }

    private String getLineNumbersText()
    {
        int counter = 0;
        int caretPosition = textArea.getDocument().getLength();
        Element root = textArea.getDocument().getDefaultRootElement();
        StringBuilder lineNumbersTextBuilder = new StringBuilder();

        for (int elementIndex = 1; elementIndex < root.getElementIndex(caretPosition) +2; 
            elementIndex++)
        {
            lineNumbersTextBuilder.append(elementIndex).append(System.lineSeparator());
        }
        return lineNumbersTextBuilder.toString();
    }
}