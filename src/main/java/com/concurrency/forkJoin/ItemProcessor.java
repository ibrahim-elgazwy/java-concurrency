package com.concurrency.forkJoin;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.RecursiveAction;

class ItemProcessor extends RecursiveAction {
    private final List<String> itemList;
    private int count = 0;
    private PrintWriter pw;

    public ItemProcessor(List<String> itemList, PrintWriter pw) {
        this.itemList = itemList;
        this.pw = pw;
    }

    @Override
    protected void compute() {
        count++;
        if (itemList.size() <= 1) {
            processItem(itemList.get(0));
        } else {
            int mid = itemList.size() / 2;
            List<String> leftList = itemList.subList(0, mid);
            List<String> rightList = itemList.subList(mid, itemList.size());

            invokeAll(new ItemProcessor(leftList, pw), new ItemProcessor(rightList, pw));
        }
    }
    private void processItem(String item) {
        item = item.toUpperCase();
        pw.printf("Processing item:  %s\n", item);
    }
}
