package pl.pwr.ite.dynak.loader;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ClassState {
    public StringProperty className;
    public StringProperty status;

    public ClassState(String className) {
        this.className = new SimpleStringProperty(className);
        this.status = new SimpleStringProperty("Unloaded");
    }

    public StringProperty statusProperty() {
        return status;
    }

    public StringProperty classNameProperty() {
        return className;
    }

    public void setStatus(String newStatus) {
        status.set(newStatus);
    }
}
