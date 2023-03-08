package no.hiof.samuelcd.tbage.models.feats;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.hiof.samuelcd.tbage.interfaces.JsonExternalisable;

import java.io.File;
import java.io.IOException;

public class Feat implements JsonExternalisable {

    private String name;


    private Feat(String name) {
        this.name = name;
    }

    public static Feat create() {
        return new Feat(null);
    }

    public static Feat create(String name) {
        return new Feat(name);
    }


    public void onUse() {
        // Feat behaviour.
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        Feat feat = om.readValue(file, Feat.class);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
