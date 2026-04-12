package pl.pwr.ite.dynak.processor;

import pl.pwr.ite.dynak.lib.Processor;
import pl.pwr.ite.dynak.lib.StatusListener;

public class CsvRowCloner implements Processor {

    private static int taskId = 0;
    private String result;

    @Override
    public boolean submitTask(String task, StatusListener sl) {
        return !task.isEmpty();
    }

    @Override
    public String getInfo() {
        return "Clones specified rows, added at the end of the file" + System.lineSeparator() +
                "Format: 1|13|42|80";
    }

    @Override
    public String getResult() {
        return "Task finished (csv row cloned)";
    }
}
