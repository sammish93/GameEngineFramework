package sammish93.tbage.gui;

import sammish93.tbage.GameEngine;
import sammish93.tbage.enums.GamePlatform;
import sammish93.tbage.exceptions.InvalidValueException;
import sammish93.tbage.exceptions.InventoryFullException;
import sammish93.tbage.interfaces.Closeable;
import sammish93.tbage.models.Encounter;
import sammish93.tbage.models.FixedEncounters;
import sammish93.tbage.models.NonCombatEncounter;
import sammish93.tbage.models.RandomEncounters;
import sammish93.tbage.tools.EncounterController;
import sammish93.tbage.tools.EncounterTraversalController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

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
        constraints.gridwidth = 2;
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel imageLabel = new JLabel();
        mainFrame.add(imageLabel, constraints);

        constraints.insets = new Insets(0, 5, 5, 5);
        constraints.gridy = 1;
        constraints.weighty = 0.9;
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
        baseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameOutput.setEditable(false);

        if (getGameSettings().isFullscreen()) {
            baseFrame.setUndecorated(true);
        }

        baseFrame.pack();

        int[] resolution = getGameSettings().getWindowResolution();
        baseFrame.setSize(resolution[0],resolution[1]);
        baseFrame.setVisible(true);
        baseFrame.setResizable(false);

        if (getGameSettings().isFullscreen()) {
            baseFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

        close(baseFrame);

        send(userInput);
        userInput.requestFocus();


        traverseEncounters(baseFrame, imageLabel);
    }

    private void traverseEncounters(JFrame baseFrame, JLabel label) throws InventoryFullException, InvalidValueException, InterruptedException {
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
                try {
                    String imgPath = EncounterTraversalController.getCurrentEncounter().getImagePath();
                    if (imgPath != null) {
                        BufferedImage image = ImageIO.read(new File(imgPath));
                        Image imageScaled = image.getScaledInstance(baseFrame.getWidth(), baseFrame.getHeight()/3, Image.SCALE_DEFAULT);
                        label.setIcon(new ImageIcon(imageScaled));
                    } else {
                        String chosenRandomImage = chooseDefaultImage(
                                EncounterTraversalController.getCurrentEncounter());
                        try (InputStream in = getClass().getResourceAsStream(chosenRandomImage)) {
                            BufferedImage image = ImageIO.read(in);
                            Image imageScaled = image.getScaledInstance(baseFrame.getWidth(), baseFrame.getHeight()/3, Image.SCALE_DEFAULT);
                            label.setIcon(new ImageIcon(imageScaled));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                output = EncounterTraversalController.getCurrentEncounter().run(gameEngine);
            } else {
                if (gameEngine.getPlatform() == GamePlatform.TERMINAL) {
                    gameEngine.printMessage("Game has finished. Please type 'exit'.");
                } else {
                    gameEngine.printMessage("Game has finished. Please press the 'Escape' key.");
                }
                output = scanner.nextLine();
            }

            //close(output);

            if (controller.getCurrentEncounter() != null) {
                controller.progressToNextEncounter(output);
            }
        }
    }

    private String chooseDefaultImage(Encounter encounter) {
        String[] defaultNonCombatEncounterImages = new String[] {
                "/images/non_combat_environment_1.png",
                "/images/non_combat_environment_2.png",
                "/images/non_combat_environment_3.png",
                "/images/non_combat_environment_4.png",
                "/images/non_combat_environment_5.png"
        };

        String[] defaultCombatEncounterImages = new String[] {
                "/images/combat_environment_1.png",
                "/images/combat_environment_2.png",
                "/images/combat_environment_3.png",
                "/images/combat_environment_4.png",
                "/images/combat_environment_5.png"
        };

        Random random = new Random();

        if (encounter instanceof NonCombatEncounter) {
            int selectedIndex = random.nextInt(defaultNonCombatEncounterImages.length);
            return defaultNonCombatEncounterImages[selectedIndex];
        } else {
            int selectedIndex = random.nextInt(defaultCombatEncounterImages.length);
            return defaultCombatEncounterImages[selectedIndex];
        }
    }


    /**
     * Closes the main Java Swing application window.
     * Note: Uses System.exit() with exit code 0 as opposed to JFrame.dispose().
     * @param jFrameToClose The main jFrame element that is the base of the application window.
     */
    public void close(JFrame jFrameToClose) {
        jFrameToClose.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "CloseBaseFrame");
        jFrameToClose.getRootPane().getActionMap().put("CloseBaseFrame", new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                int answer = JOptionPane.showConfirmDialog(
                        jFrameToClose,
                        "Do you wish to exit the game?",
                        "Exit",
                        JOptionPane.YES_NO_OPTION);

                if (answer == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
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

