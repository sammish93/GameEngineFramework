package no.hiof.samuelcd.tbage.interfaces;

import java.io.File;
import java.io.IOException;

public interface JsonExternalisable {

    public void writeToJson(File file) throws IOException;

    public void readFromJson(File file) throws IOException;
}
