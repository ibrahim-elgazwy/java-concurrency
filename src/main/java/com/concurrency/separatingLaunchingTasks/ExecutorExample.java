package com.concurrency.separatingLaunchingTasks;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutionException;

public class ExecutorExample {
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newFixedThreadPool(5);
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(executor);

        // Submit tasks to the executor
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            completionService.submit(() -> {
                // Perform some task
                Thread.sleep(1000);
                return taskId;
            });
        }

        // Process the results as they become available
        for (int i = 1; i <= 10; i++) {
            Future<Integer> completedTask = completionService.take();
            int result = completedTask.get();
            System.out.println("Task " + result + " completed at: " + new Date());
        }

        // Shutdown the executor
        executor.shutdown();
    }
}
