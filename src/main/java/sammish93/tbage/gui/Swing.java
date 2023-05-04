package sammish93.tbage.gui;

import sammish93.tbage.GameEngine;
import sammish93.tbage.exceptions.InvalidValueException;
import sammish93.tbage.exceptions.InventoryFullException;
import sammish93.tbage.interfaces.Closeable;
import sammish93.tbage.models.FixedEncounters;
import sammish93.tbage.models.NonCombatEncounter;
import sammish93.tbage.models.RandomEncounters;
import sammish93.tbage.tools.EncounterController;
import sammish93.tbage.tools.EncounterTraversalController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static sammish93.tbage.GameEngine.scanner;

/**
 * An class intended to be used to render the game in a Java Swing window.
 */
public class Swing extends GameInterface implements Closeable<JFrame> {




    public Swing(GameEngine gameEngine) throws InvalidValueException, InventoryFullException, InterruptedException {
        super(gameEngine);

        run();
    }

    private void run() throws InventoryFullException, InvalidValueException, InterruptedException {
        JFrame baseFrame;

        baseFrame = new JFrame(getGameSettings().getWindowTitle());

        JTextArea gameOutput = new JTextArea (5, 20);
        JTextField userInput = new JTextField();

        gameOutput.setEditable(false);

        Container contentPane = baseFrame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(gameOutput,
                                         JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                         JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
                                         BorderLayout.CENTER);
        contentPane.add(userInput, BorderLayout.SOUTH);

        PrintStream printStream = new PrintStream(new SwingOutputStream(gameOutput));
        System.setOut(printStream);

        baseFrame.pack();

        int[] resolution = getGameSettings().getWindowResolution();
        baseFrame.setSize(resolution[0],resolution[1]);
        baseFrame.setVisible(true);

        close(baseFrame);

        send(userInput);
        userInput.requestFocus();


        var controller = getEncounterController();
        var gameEngine = getGameEngine();
        var encounters = getEncounters();

        if (!controller.checkEncounterPaths(gameEngine)) {
            baseFrame.dispose();
        }

        if (encounters == null ||
                (encounters instanceof FixedEncounters) && ((FixedEncounters)encounters)
                        .getEncounters().isEmpty() ||
                (encounters instanceof RandomEncounters) && ((RandomEncounters) encounters)
                        .getEncounterOrder().isEmpty()) {
            gameEngine.printMessage("There are no encounters present.");
        }

        // Closes current Terminal process on user entering 'exit'.
        while (true) {
            String output;

            if (EncounterTraversalController.getCurrentEncounter() != null) {
                output = EncounterTraversalController.getCurrentEncounter().run(gameEngine);
            } else {
                gameEngine.printMessage("Game has finished. Please type 'exit'.");
                output = scanner.nextLine();
            }

            //close(output);

            if (controller.getCurrentEncounter() != null) {
                controller.progressToNextEncounter(output);
            }

        }
    }



    /**
     * Closes the main Java Swing application window.
     * @param jFrameToClose The main jFrame element that is the base of the application window.
     */
    public void close(JFrame jFrameToClose) {
        jFrameToClose.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "CloseBaseFrame");
        jFrameToClose.getRootPane().getActionMap().put("CloseBaseFrame", new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                jFrameToClose.dispose();
            }
        });
    }

    private void send(JTextField textField) {
        textField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "sendInput");
        textField.getActionMap().put("sendInput", new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (!textField.getText().isEmpty()) {
                    EncounterController.setInput(textField.getText());
                    getGameEngine().printMessage(textField.getText());
                }
                textField.setText("");
            }
        });

    }
}

