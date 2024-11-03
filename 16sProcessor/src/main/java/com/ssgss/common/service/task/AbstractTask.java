package com.ssgss.common.service.task;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class AbstractTask implements Runnable{
    private String name;
    private static final AtomicLong serialId= new AtomicLong(0);
    private static String split = ":";
    AbstractTask(String name){
        long expected = serialId.incrementAndGet();
        this.name = name + split + expected;
    }
    @Override
    public void run() {
    }
    public String getName(){
        return name;
    }
}
