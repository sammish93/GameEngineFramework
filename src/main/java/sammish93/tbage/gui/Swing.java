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
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        JFrame baseFrame = new JFrame(getGameSettings().getWindowTitle());

        JPanel mainFrame = new JPanel();
        JTextArea gameOutput = new JTextArea();
        JTextField userInput = new JTextField();
        JButton sendUserInputButton = new JButton();

        baseFrame.add(mainFrame);
        mainFrame.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 0.5;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        JPanel panelExample = new JPanel();
        panelExample.setBackground(Color.GREEN);
        mainFrame.add(panelExample, constraints);

        constraints.insets = new Insets(0, 5, 5, 5);
        constraints.gridy = 1;
        gameOutput.setWrapStyleWord(true);
        gameOutput.setLineWrap(true);
        gameOutput.setTabSize(2);
        gameOutput.setFont(getGameSettings().getFontOutput());
        gameOutput.setMargin(new Insets(5, 5, 5, 5));
        mainFrame.add(new JScrollPane(gameOutput,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), constraints);

        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.weighty = 0.0;
        constraints.weightx = 0.9;
        userInput.setFont(getGameSettings().getFontInput());
        userInput.setMargin(new Insets(5, 5, 5, 5));
        mainFrame.add(userInput, constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.1;
        sendUserInputButton.setText("Send");
        sendUserInputButton.setFont(getGameSettings().getFontGeneral());
        sendUserInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendMessageToGameOutput(userInput);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        mainFrame.add(sendUserInputButton, constraints);

        PrintStream printStream = new PrintStream(new SwingOutputStream(gameOutput));
        System.setOut(printStream);
        gameOutput.setEditable(false);

        baseFrame.pack();

        int[] resolution = getGameSettings().getWindowResolution();
        baseFrame.setSize(resolution[0],resolution[1]);
        baseFrame.setVisible(true);

        close(baseFrame);

        send(userInput);
        userInput.requestFocus();


        traverseEncounters(baseFrame);
    }

    private void traverseEncounters(JFrame baseFrame) throws InventoryFullException, InvalidValueException, InterruptedException {
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
                try {
                    sendMessageToGameOutput(textField);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    private void sendMessageToGameOutput(JTextField textField) throws InterruptedException {
        if (!textField.getText().isEmpty()) {
            if (!getGameSettings().isOutputSeparatedByNewLine()) {
                getGameEngine().printMessageFormatted("\n> " + textField.getText() + "\n\n");
            } else {
                getGameEngine().printMessageFormatted("> " + textField.getText() + "\n\n");
            }
            EncounterController.setInput(textField.getText());
        }
        textField.setText("");
    }
}

