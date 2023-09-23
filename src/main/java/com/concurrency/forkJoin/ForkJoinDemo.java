package com.concurrency.forkJoin;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class ForkJoinDemo {

    public static void main(String[] args) throws InterruptedException {

        String fileName = ".\\data\\log.txt";
        try (FileWriter file = new FileWriter(fileName); PrintWriter pw = new PrintWriter(file);) {
            executeUpperCaseTask(pw);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void executeUpperCaseTask(PrintWriter pw) throws InterruptedException {

        List<String> itemList = generateList(100000);

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ItemProcessor task = new ItemProcessor(itemList, pw);
        forkJoinPool.invoke(task);

        do {
            System.out.printf("******************************************\n");
            System.out.printf("Main: Parallelism: %d\n",forkJoinPool.getParallelism());
            System.out.printf("Main: Active Threads: %d\n",forkJoinPool.getActiveThreadCount());
            System.out.printf("Main: Task Count: %d\n",forkJoinPool.getQueuedTaskCount());
            System.out.printf("Main: Steal Count: %d\n",forkJoinPool.getStealCount());
            System.out.printf("******************************************\n");

            TimeUnit.SECONDS.sleep(1);

        } while (!task.isDone());

        forkJoinPool.shutdown();
    }

    private static List<String> generateList(int size) {

        List<String> itemList = new ArrayList<>();

        for (int i=0; i < size; i++) {
            itemList.add("item-" + i);
        }

        return itemList;
    }
}
