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

public class CsvRowCloner implements Processor {

    private static int taskId = 0;
    private String result;
    //task to cały request z danymi i formatem zadania w pierwszej linijce, potem jest rozdzielany
    private String task;

    @Override
    public boolean submitTask(String task, StatusListener sl) {
        taskId++;
        AtomicInteger ai = new AtomicInteger(0);
        this.task = task;

        if (!checkTaskValidity(extractRequest(task)[0])) {
            return false;
        }

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
                    executor.shutdown();
                    scheduledExecutor.shutdown();
                    break;
                }
            }
        });

        return true;
    }

    @Override
    public String getInfo() {
        return "Clones specified rows" + System.lineSeparator() +
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
        String[] separatedInput = extractRequest(task);

        // tu jest żądanie w odpowiednim formacie
        String taskData = separatedInput[0];
        // a tu reszta csv
        String input = separatedInput[1];

        System.out.println(input);

        if (input == null || input.isEmpty()) {
            return "";
        }

        Set<Integer> rowsToClone = Arrays.stream(taskData.split("\\|"))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toSet());

        // logic

        String[] lines = input.split("\n");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            sb.append(lines[i]).append("\n");
            if (rowsToClone.contains(i)) {
                sb.append(lines[i]).append("\n");
            }
        }

        return sb.toString();
    }

    private String[] extractRequest(String task) {
        return task.split("\n", 2);
    }

    private boolean checkTaskValidity(String task) {
        return task.contains("|");
    }
}
