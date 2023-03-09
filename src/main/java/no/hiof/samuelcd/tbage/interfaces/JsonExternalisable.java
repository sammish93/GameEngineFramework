package no.hiof.samuelcd.tbage.interfaces;

import java.io.File;
import java.io.IOException;

public interface JsonExternalisable {
    // Not sure whether to go forward with this interface at this point. Serialisation into bytes is more practical
    // because it also serialises custom onUser() and onInitiation() methods.

    void writeToJson(File file) throws IOException;

    void readFromJson(File file) throws IOException;
}
