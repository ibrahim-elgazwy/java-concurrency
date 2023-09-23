package com.concurrency.concurrentCollection;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentCollectionExample {
    public static void main(String[] args) throws InterruptedException {

        ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();

        // Add elements concurrently
        Runnable addElements = () -> {
            for (int i = 0; i < 1000; i++) {
                concurrentMap.put("Key" + i, i);
                System.out.println("add element: " + "Key" + i + " value: " + concurrentMap.get("Key" + i));
            }
        };

        // Remove elements concurrently
        Runnable removeElements = () -> {
            for (int i = 0; i < 1000; i++) {
                Integer element = concurrentMap.remove("Key" + i);
                System.out.println("remove element: " + "Key" + i + " value: " + element);
            }
        };

        // Create multiple threads to add and remove elements concurrently
        Thread addThread = new Thread(addElements);
        Thread removeThread = new Thread(removeElements);

        addThread.start();
        removeThread.start();

        addThread.join();
        removeThread.join();

        System.out.println("Size of concurrentMap: " + concurrentMap.size());
    }
}
