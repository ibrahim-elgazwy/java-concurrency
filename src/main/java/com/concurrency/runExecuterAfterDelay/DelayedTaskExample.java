package com.concurrency.runExecuterAfterDelay;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DelayedTaskExample {
    public static void main(String[] args) {

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

        Runnable delayedTask = () -> {
            System.out.println("Delayed task executed!");
        };

        executor.schedule(delayedTask, 5, TimeUnit.SECONDS);

        executor.shutdown();
    }
}
