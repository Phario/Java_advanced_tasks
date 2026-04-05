package pl.pwr.ite.dynak.processor;

import pl.pwr.ite.dynak.lib.Processor;
import pl.pwr.ite.dynak.lib.StatusListener;

public class CsvCloner implements Processor {

    @Override
    public boolean submitTask(String s, StatusListener statusListener) {
        return false;
    }

    @Override
    public String getInfo() {
        return "";
    }

    @Override
    public String getResult() {
        return "";
    }
}
