package no.hiof.samuelcd.tbage.models.items;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.hiof.samuelcd.tbage.interfaces.JsonExternalisable;
import no.hiof.samuelcd.tbage.interfaces.Useable;

import java.io.*;

public class Item implements Useable, JsonExternalisable {

    private String name;
    private int value;
    private double dropChance;

    @JsonCreator
    private Item(@JsonProperty("name") String name,
                 @JsonProperty("value") int value,
                 @JsonProperty("dropChance") double dropChance) {
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

    public double getDropChance() {
        return dropChance;
    }

    public void setDropChance(double dropChance) {
        this.dropChance = dropChance;
    }

    @Override
    public void writeToJson(File file) throws IOException {
        ObjectMapper om = new ObjectMapper();
        boolean fileExists = file.exists();

        if (!fileExists) {
            fileExists = file.createNewFile();
        }

        if (fileExists) {
            om.writeValue(file, this);
        }

        // Jackson auto-closes the stream and mapper.
    }

    @Override
    public void readFromJson(File file) throws IOException{
        ObjectMapper om = new ObjectMapper();
        Item item = om.readValue(file, Item.class);

        this.name = item.name;
        this.value = item.value;
        this.dropChance = item.dropChance;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
