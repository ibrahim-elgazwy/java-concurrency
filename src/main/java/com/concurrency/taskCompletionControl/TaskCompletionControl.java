package com.concurrency.taskCompletionControl;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskCompletionControl {
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<String> future = executor.submit(() -> {
            Thread.sleep(5000); // Simulating task execution time of 5 seconds
            return "Task completed!";
        });

        if (!future.isDone()) {
            Thread.sleep(6000);
        }

        if (future.isDone()) {
            String result = future.get();
            System.out.println(result);
        } else {
            System.out.println("Task is still running........");
        }

        executor.shutdown();
    }
}
