package no.hiof.samuelcd.tbage.gui;
import no.hiof.samuelcd.tbage.GameSettings;
import no.hiof.samuelcd.tbage.models.encounters.EncounterPool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class Swing {

    private static GameSettings gameSettings;
    private static EncounterPool encounterPool;
    JFrame exampleFrame;
    public Swing(GameSettings gameSettings, EncounterPool encounterPool) {
        Swing.gameSettings = gameSettings;
        Swing.encounterPool = encounterPool;

        exampleFrame =new JFrame();

        JTextArea exampleTextArea = new JTextArea(Swing.gameSettings.getButtonMessage());

        exampleFrame.add(exampleTextArea);
        exampleFrame.pack();

        exampleFrame.setSize(800,600);
        exampleFrame.setLayout(null);
        exampleFrame.setVisible(true);

        exampleFrame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        exampleFrame.getRootPane().getActionMap().put("Cancel", new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                exampleFrame.dispose();

                // Less elegant solution.
                //System.exit(0);
            }
        });

    }

    public static void main(String[] args) {
        new Swing(gameSettings, encounterPool);
    }
}

