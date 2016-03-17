import javax.swing.*;
import javax.swing.text.Element;
import java.awt.*;

public class LineNumberingTextArea extends JTextArea
{
    private JTextPane textArea;

    public LineNumberingTextArea(JTextPane textArea)
    {
        this.textArea = textArea;
        setBackground(Color.BLACK);
        textArea.setFont(new Font("Consolas", Font.BOLD, 14));
        setEditable(false);
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
        lineNumbersTextBuilder.append("1").append(System.lineSeparator());

        for (int elementIndex = 2; elementIndex < root.getElementIndex(caretPosition) +2; 
            elementIndex++)
        {
            lineNumbersTextBuilder.append(elementIndex).append(System.lineSeparator());
        }
        return lineNumbersTextBuilder.toString();
    }
}