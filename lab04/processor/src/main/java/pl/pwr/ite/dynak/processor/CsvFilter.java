package pl.pwr.ite.dynak.processor;

import pl.pwr.ite.dynak.lib.Processor;
import pl.pwr.ite.dynak.lib.StatusListener;

public class CsvFilter implements Processor {
    @Override
    public boolean submitTask(String task, StatusListener sl) {
        return false;
    }

    @Override
    public String getInfo() {
        return "Filters rows based on value and range, removes the rest" + System.lineSeparator() +
                "Format: 3|13-42";
    }

    @Override
    public String getResult() {
        return "";
    }
}
