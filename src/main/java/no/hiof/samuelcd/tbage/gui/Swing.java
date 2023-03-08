package no.hiof.samuelcd.tbage.gui;
import no.hiof.samuelcd.tbage.GameSettings;
import no.hiof.samuelcd.tbage.interfaces.Closeable;
import no.hiof.samuelcd.tbage.models.encounters.Encounters;
import no.hiof.samuelcd.tbage.models.player.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class Swing implements Closeable<JFrame> {

    private static GameSettings gameSettings;
    private static Player player;
    private static Encounters encounters;
    JFrame exampleFrame;
    public Swing(GameSettings gameSettings, Player player, Encounters encounters) {
        Swing.gameSettings = gameSettings;
        Swing.player = player;
        Swing.encounters = encounters;

        exampleFrame = new JFrame();

        JTextArea exampleTextArea = new JTextArea(Swing.gameSettings.getButtonMessage());

        exampleFrame.add(exampleTextArea);
        exampleFrame.pack();

        exampleFrame.setSize(800,600);
        exampleFrame.setLayout(null);
        exampleFrame.setVisible(true);

        close(exampleFrame);
    }

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


    public static void main(String[] args) {
        new Swing(gameSettings, player, encounters);
    }
}

