package no.hiof.samuelcd.tbage.models.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.hiof.samuelcd.tbage.interfaces.JsonExternalisable;

import java.io.File;
import java.io.IOException;

public class Player implements JsonExternalisable {
    // Class that will hold information about a player throughout the game.

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
        Player player = om.readValue(file, Player.class);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
