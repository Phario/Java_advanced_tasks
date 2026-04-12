package pl.pwr.ite.dynak.processor;

import pl.pwr.ite.dynak.lib.Processor;
import pl.pwr.ite.dynak.lib.Status;
import pl.pwr.ite.dynak.lib.StatusListener;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CsvTrimmer implements Processor {

    private static int taskId = 0;
    private String result;
    private String task;
    @Override
    public boolean submitTask(String task, StatusListener sl) {
        taskId++;
        AtomicInteger ai = new AtomicInteger(0);
        this.task = task;

        ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutor.scheduleAtFixedRate(() -> {
            sl.statusChanged(new Status(taskId, ai.getAndIncrement()));
        }, 10, 10, TimeUnit.MILLISECONDS);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {
            while (true) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (ai.get() >= 100) {
                    result = processData();
                }
            }
        });

        return true;
    }

    @Override
    public String getInfo() {
        return "Removes specified rows" + System.lineSeparator() +
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

    private String processData() {
        String input = extractRequest(task);

        System.out.println(input);

        if (input == null || input.isEmpty()) {
            return "";
        }

        Set<Integer> rowsToRemove = Arrays.stream(task.split("\\|"))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toSet());

        String[] lines = input.split("\n");
        StringBuilder sb = new StringBuilder();

        sb.append(lines[0]);

        for (int i = 1; i < lines.length; i++) {
            if (!rowsToRemove.contains(i)) {
                sb.append("\n").append(lines[i]);
            }
        }

        return sb.toString();
    }

    private String extractRequest(String task) {
        return task.split("\n", 2)[0];
    }
}
