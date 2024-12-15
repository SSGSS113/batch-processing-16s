package com.ssgss.common.constant;

import com.ssgss.common.configration.ThreadPollConfigrature;
import com.ssgss.common.factory.threadFactory.MyThreadFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Getter
@Slf4j
@DependsOn("threadPollConfigrature")
public class ThreadPoolControl {
    ThreadPollConfigrature threadPollConfigrature;
    private final ExecutorService downloadPoll;
    private final ExecutorService dumpPool;
    private final ExecutorService fastqcPool;
    private final ExecutorService importPool;
    private final ExecutorService denoisePool;
    private final ExecutorService taxonomyPool;
    private final ExecutorService alphaPool;
    @Autowired
    public ThreadPoolControl(ThreadPollConfigrature threadPollConfigrature){
        log.info("ThreadPoolControl 进行初始化, downloadCores = {}, denoiseCores = {}, alphaCores = {}," +
                        "fastqcCores = {}, importCores = {}, fastqdumpCores = {}, taxonomyCores = {}, keepAliveTime = {}",
                threadPollConfigrature.getDownloadCores(), threadPollConfigrature.getDenoiseCores(),
                threadPollConfigrature.getAlphaCores() , threadPollConfigrature.getFastqcCores(),
                threadPollConfigrature.getImportCores(), threadPollConfigrature.getFastqdumpCores(),
                threadPollConfigrature.getTaxonomyCores(), threadPollConfigrature.getKeepAliveTime());
        downloadPoll = new ThreadPoolExecutor(threadPollConfigrature.getDownloadCores(),
                threadPollConfigrature.getDownloadCores(),threadPollConfigrature.getKeepAliveTime(),
                TimeUnit.SECONDS,new LinkedBlockingDeque<>(), new MyThreadFactory("DownloadPool"));
        log.info("downloadPoll 创建成功");
        dumpPool = Executors.newFixedThreadPool(threadPollConfigrature.getFastqdumpCores(),
                new MyThreadFactory("fastqdumpPool"));
        log.info("dumpPool 创建成功");
        fastqcPool = Executors.newFixedThreadPool(threadPollConfigrature.getFastqcCores(),
                new MyThreadFactory("fastqcPool"));
        log.info("fastqcPool 创建成功");
        importPool = Executors.newFixedThreadPool(threadPollConfigrature.getImportCores(),
                new MyThreadFactory("importPool"));
        log.info("importPool 创建成功");
        alphaPool = Executors.newFixedThreadPool(threadPollConfigrature.getAlphaCores(),
                new MyThreadFactory("alpha"));
        log.info("alphaPool 创建成功");
        taxonomyPool = Executors.newFixedThreadPool(threadPollConfigrature.getTaxonomyCores(),
                new MyThreadFactory("taxonomyPool"));
        log.info("taxonomyPool 创建成功");
        denoisePool = Executors.newFixedThreadPool(threadPollConfigrature.getDenoiseCores(),
                new MyThreadFactory("denoisePool"));
        log.info("denoisePool 创建成功");
    }
}
