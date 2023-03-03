package no.hiof.samuelcd;

public class GameEngine {

    private String Gui;

    GameEngine(String ChosenGui) {
        Gui = ChosenGui;
    }

    public void run() {
        if (Gui.equalsIgnoreCase("TERMINAL")) {
            System.out.println("This game will run in a terminal.");
        } else if (Gui.equalsIgnoreCase("SWING")) {
            new SwingThing();
        }
    }
}
