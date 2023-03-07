package no.hiof.samuelcd.tbage.models.items;

import no.hiof.samuelcd.tbage.interfaces.Useable;

public class Item implements Useable {

    private String name;
    private int value;
    private double dropChance;


    private Item(String name, int value, double dropChance) {
        this.name = name;
        this.value = value;
        this.dropChance = dropChance;
    }

    public static Item create() {
        return new Item(null, 0, 0);
    }

    public static Item create(String name) {
        return new Item(name, 0, 0);
    }

    public static Item create(String name, int value) {
        return new Item(name, value, 0);
    }

    public static Item create(String name, int value, double dropChance) {
        return new Item(name, value, dropChance);
    }

    public void onUse() {
        // Item does this when it is used.
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
}
