package no.hiof.samuelcd.tbage.models.items;

import no.hiof.samuelcd.tbage.GameEngine;
import no.hiof.samuelcd.tbage.interfaces.Useable;

import java.io.Serializable;

public class Item implements Useable, Serializable {

    private String name = "defaultItemName";
    private int value;
    private double dropChance;
    private String imagePath;
    private Useable useable;
    private int numberOfUses;


    private Item(String name, int value, double dropChance, int numberOfUses) {
        this.name = name;
        this.value = value;
        this.dropChance = dropChance;
        this.numberOfUses = numberOfUses;
    }

    public static Item create() {
        return new Item(null, 0, 0, 0);
    }

    public static Item create(String name) {
        return new Item(name, 0, 0, 0);
    }

    public static Item create(String name, int value) {
        return new Item(name, value, 0, 0);
    }

    public static Item create(String name, int value, double dropChance) {
        return new Item(name, value, dropChance, 1);
    }
    public static Item create(String name, int value, double dropChance, int numberOfUses) {
        return new Item(name, value, dropChance, numberOfUses);
    }

    public void onUse(GameEngine gameEngine) {
        // Item does this when it is used.
        useable.onUse(gameEngine);
    }

    public void setOnUseBehaviour(Useable useable) {
        this.useable = useable;
    }

    public Useable getOnUseBehaviour() {
        return useable;
    }

    public void onReceive() {
        // Behaviour for when the player receives the item.
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public double getDropChance() {
        return dropChance;
    }

    public void setDropChance(double dropChance) {
        this.dropChance = dropChance;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getNumberOfUses() {
        return numberOfUses;
    }

    public void setNumberOfUses(int numberOfUses) {
        this.numberOfUses = numberOfUses;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
