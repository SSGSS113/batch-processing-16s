package com.ssgss.common.constant;

import com.ssgss.common.configration.ThreadPollConfigrature;
import com.ssgss.common.factory.threadFactory.MyThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class ThreadPoolConstant {
    @Autowired
    ThreadPollConfigrature threadPollConfigrature;
    private final ThreadPoolExecutor downloadPoll = new ThreadPoolExecutor(threadPollConfigrature.getDownloadCores(),
            threadPollConfigrature.getDownloadCores(),threadPollConfigrature.getKeepAliveTime(),
            TimeUnit.SECONDS,new LinkedBlockingDeque<>(), new MyThreadFactory("DownloadPool"));
    private final ThreadPoolExecutor dumpPool = new ThreadPoolExecutor(threadPollConfigrature.getFastqdumpCores(),
            threadPollConfigrature.getFastqdumpCores()*2,threadPollConfigrature.getKeepAliveTime(),
            TimeUnit.SECONDS,new ArrayBlockingQueue<>(5), new MyThreadFactory("fastqdumpPool"));
    private final ThreadPoolExecutor fastqcPool = new ThreadPoolExecutor(threadPollConfigrature.getFastqcCores(),
            threadPollConfigrature.getFastqcCores()*2,threadPollConfigrature.getKeepAliveTime(),
            TimeUnit.SECONDS,new ArrayBlockingQueue<>(5), new MyThreadFactory("fastqcPool"));
    private final ThreadPoolExecutor importPool = new ThreadPoolExecutor(threadPollConfigrature.getImportCores(),
            threadPollConfigrature.getImportCores()*2,threadPollConfigrature.getKeepAliveTime(),
            TimeUnit.SECONDS,new ArrayBlockingQueue<>(5), new MyThreadFactory("importPool"));
    private final ThreadPoolExecutor denoisePool = new ThreadPoolExecutor(threadPollConfigrature.getDenoiseCores(),
            threadPollConfigrature.getDenoiseCores()*2,threadPollConfigrature.getKeepAliveTime(),
            TimeUnit.SECONDS,new ArrayBlockingQueue<>(5), new MyThreadFactory("denoisePool"));
    private final ThreadPoolExecutor taxonomy = new ThreadPoolExecutor(threadPollConfigrature.getTaxonomyCores(),
            threadPollConfigrature.getTaxonomyCores()*2,threadPollConfigrature.getKeepAliveTime(),
            TimeUnit.SECONDS,new ArrayBlockingQueue<>(5), new MyThreadFactory("taxonomyPool"));
    private final ThreadPoolExecutor alpha = new ThreadPoolExecutor(threadPollConfigrature.getAlphaCores(),
            threadPollConfigrature.getAlphaCores()*2,threadPollConfigrature.getKeepAliveTime(),
            TimeUnit.SECONDS,new ArrayBlockingQueue<>(5), new MyThreadFactory("alpha"));
}
