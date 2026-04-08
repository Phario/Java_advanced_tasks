package pl.pwr.ite.dynak.processor;

import pl.pwr.ite.dynak.lib.Processor;
import pl.pwr.ite.dynak.lib.StatusListener;

public class CsvRowCloner implements Processor {

    @Override
    public boolean submitTask(String s, StatusListener statusListener) {
        return false;
    }

    @Override
    public String getInfo() {
        return "Clones specified rows, added at the end of the file" + System.lineSeparator() +
                "Format: 1|13|42|80";
    }

    @Override
    public String getResult() {
        return "";
    }
}
