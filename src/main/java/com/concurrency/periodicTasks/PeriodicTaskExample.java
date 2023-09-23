package com.concurrency.periodicTasks;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PeriodicTaskExample {
    public static void main(String[] args) throws InterruptedException {

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        AtomicInteger times = new AtomicInteger(1);

        Runnable periodicTask = () -> {
            System.out.println("Periodic task executed! times: " + times.get());
            times.getAndIncrement();
        };

        executor.scheduleAtFixedRate(periodicTask, 0, 1, TimeUnit.SECONDS);

        // Sleep for 10 seconds to allow the periodic task to run multiple times
        Thread.sleep(10000);

        executor.shutdown();
    }
}
