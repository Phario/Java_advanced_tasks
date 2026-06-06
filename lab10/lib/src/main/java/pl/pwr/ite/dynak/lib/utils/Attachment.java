package pl.pwr.ite.dynak.lib.utils;

import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import java.io.File;

public class Attachment {
    private final StringProperty timeStamp;
    private final StringProperty message;
    private File file;

    public Attachment(StringProperty timeStamp, StringProperty message, File file) {
        this.timeStamp = timeStamp;
        this.message = message;
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public StringProperty getTimeStamp() {
        return timeStamp;
    }

    public StringProperty getMessage() {
        return message;
    }
}
