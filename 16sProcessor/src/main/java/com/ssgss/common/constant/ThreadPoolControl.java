package com.ssgss.common.constant;

import com.ssgss.common.configration.ThreadPollConfigrature;
import com.ssgss.common.factory.threadFactory.MyThreadFactory;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Getter
@Slf4j
public class ThreadPoolControl {
    ThreadPollConfigrature threadPollConfigrature;
    private final ThreadPoolExecutor downloadPoll;
    private final ThreadPoolExecutor dumpPool;
    private final ThreadPoolExecutor fastqcPool;
    private final ThreadPoolExecutor importPool;
    private final ThreadPoolExecutor denoisePool;
    private final ThreadPoolExecutor taxonomyPool;
    private final ThreadPoolExecutor alphaPool;
    @Autowired
    public ThreadPoolControl(ThreadPollConfigrature threadPollConfigrature){
        downloadPoll = new ThreadPoolExecutor(threadPollConfigrature.getDownloadCores(),
                threadPollConfigrature.getDownloadCores(),threadPollConfigrature.getKeepAliveTime(),
                TimeUnit.SECONDS,new LinkedBlockingDeque<>(), new MyThreadFactory("DownloadPool"));
        log.info("downloadPoll 创建成功");
        dumpPool = new ThreadPoolExecutor(threadPollConfigrature.getFastqdumpCores(),
                threadPollConfigrature.getFastqdumpCores()*2,threadPollConfigrature.getKeepAliveTime(),
                TimeUnit.SECONDS,new ArrayBlockingQueue<>(5), new MyThreadFactory("fastqdumpPool"));
        log.info("dumpPool 创建成功");
        fastqcPool = new ThreadPoolExecutor(threadPollConfigrature.getFastqcCores(),
                threadPollConfigrature.getFastqcCores()*2,threadPollConfigrature.getKeepAliveTime(),
                TimeUnit.SECONDS,new ArrayBlockingQueue<>(5), new MyThreadFactory("fastqcPool"));
        log.info("fastqcPool 创建成功");
        importPool = new ThreadPoolExecutor(threadPollConfigrature.getImportCores(),
                threadPollConfigrature.getImportCores()*2,threadPollConfigrature.getKeepAliveTime(),
                TimeUnit.SECONDS,new ArrayBlockingQueue<>(5), new MyThreadFactory("importPool"));
        log.info("importPool 创建成功");
        alphaPool = new ThreadPoolExecutor(threadPollConfigrature.getAlphaCores(),
                threadPollConfigrature.getAlphaCores()*2,threadPollConfigrature.getKeepAliveTime(),
                TimeUnit.SECONDS,new ArrayBlockingQueue<>(5), new MyThreadFactory("alpha"));
        log.info("alphaPool 创建成功");
        taxonomyPool = new ThreadPoolExecutor(threadPollConfigrature.getTaxonomyCores(),
                threadPollConfigrature.getTaxonomyCores()*2,threadPollConfigrature.getKeepAliveTime(),
                TimeUnit.SECONDS,new ArrayBlockingQueue<>(5), new MyThreadFactory("taxonomyPool"));
        log.info("taxonomyPool 创建成功");
        denoisePool = new ThreadPoolExecutor(threadPollConfigrature.getDenoiseCores(),
                threadPollConfigrature.getDenoiseCores()*2,threadPollConfigrature.getKeepAliveTime(),
                TimeUnit.SECONDS,new ArrayBlockingQueue<>(5), new MyThreadFactory("denoisePool"));
        log.info("denoisePool 创建成功");
    }
}
