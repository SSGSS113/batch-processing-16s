package com.ssgss.common.factory.threadFactory;

import lombok.AllArgsConstructor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public class MyThreadFactory implements ThreadFactory {
    private final AtomicInteger threadCount = new AtomicInteger(0);
    private final String namePrefix;
    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, namePrefix + '-' + threadCount.incrementAndGet());
    }
}
