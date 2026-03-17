package pl.pwr.ite.dynak.lib.parser;

import java.io.File;
import java.io.IOException;

public interface IFileParser<T> {
    T parse(File file) throws IOException;
}
