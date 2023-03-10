package no.hiof.samuelcd.tbage.models.encounters;

import java.io.*;

public abstract class Encounters implements Serializable {
    // Will be used to implement the order the encounters appear.

    // One 'Encounters' to many 'Encounter'
    // One 'Encounter' to many 'NonPlayableCharacter'
    // One 'NonPlayableCharacter' to many 'Ability' and to many 'Item'

    public void save(String path) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        objectOutputStream.writeObject(this);
    }

    public static Encounters load(String path) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        return (Encounters)objectInputStream.readObject();
    }
}
