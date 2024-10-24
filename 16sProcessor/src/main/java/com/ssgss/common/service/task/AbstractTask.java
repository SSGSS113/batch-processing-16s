package com.ssgss.common.service.task;

import java.util.concurrent.atomic.AtomicLong;

public class AbstractTask implements Runnable{
    private String name;
    private static final AtomicLong serialId= new AtomicLong(0);
    private static String split = ":";
    AbstractTask(String name){
        long expected;
        long newLong;
        do {
            expected = serialId.get();
            newLong = expected + 1;
        }while(!serialId.compareAndSet(expected, newLong));
        this.name = name + split + expected;
    }
    @Override
    public void run() {
    }
}
