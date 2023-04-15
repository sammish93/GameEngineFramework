package no.hiof.samuelcd.tbage.gui;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.interfaces.Closeable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * An class intended to be used to render the game in a Java Swing window.
 */
public class Swing extends GameInterface implements Closeable<JFrame> {

    JFrame exampleFrame;


    public Swing(GameEngine gameEngine) {
        super(gameEngine);

        exampleFrame = new JFrame();

        JTextArea exampleTextArea = new JTextArea(getGameSettings().getButtonMessage());

        exampleFrame.add(exampleTextArea);
        exampleFrame.pack();

        exampleFrame.setSize(800,600);
        exampleFrame.setLayout(null);
        exampleFrame.setVisible(true);

        close(exampleFrame);
    }


    /**
     * Closes the main Java Swing application window.
     * @param jFrameToClose The main jFrame element that is the base of the application window.
     */
    public void close(JFrame jFrameToClose) {
        jFrameToClose.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        jFrameToClose.getRootPane().getActionMap().put("Cancel", new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                jFrameToClose.dispose();

                // Less elegant solution.
                //System.exit(0);
            }
        });
    }
}

