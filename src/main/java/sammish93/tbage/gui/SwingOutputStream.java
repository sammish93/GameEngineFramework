package sammish93.tbage.gui;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;

// Used a modified version of code found here
// https://www.codejava.net/java-se/swing/redirect-standard-output-streams-to-jtextarea on 03/05/2023 at 14:51.

class SwingOutputStream extends OutputStream{

    private JTextArea textArea;


    protected SwingOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }


    @Override
    public void write(int b) throws IOException {
        textArea.append(String.valueOf((char)b));

        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
