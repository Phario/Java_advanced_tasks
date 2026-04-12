package pl.pwr.ite.dynak.processor;

import pl.pwr.ite.dynak.lib.Processor;
import pl.pwr.ite.dynak.lib.Status;
import pl.pwr.ite.dynak.lib.StatusListener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CsvRowCloner implements Processor {

    private static int taskId = 0;
    private String result;
    private String input;

    @Override
    public boolean submitTask(String task, StatusListener sl) {
        taskId++;
        AtomicInteger ai = new AtomicInteger(0);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() -> {
            sl.statusChanged(new Status(taskId, ai.getAndIncrement()));
        }, 10, 10, TimeUnit.MILLISECONDS);



        return !task.isEmpty();
    }

    @Override
    public String getInfo() {
        return "Clones specified rows, added at the end of the file" + System.lineSeparator() +
                "Format: 1|13|42|80";
    }

    @Override
    public String getResult() {
        try {
            return result;
        } finally {
            result = null;
        }
    }
}
