package com.ssgss.common.service.pipeline.impl;

import com.ssgss.common.configration.ThreadPollConfigrature;
import com.ssgss.common.constant.CommonConstant;
import com.ssgss.common.entity.SraDTO;
import com.ssgss.common.service.task.DownloadTask;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.*;

@Service
@Slf4j
public class Performer {
    private final BlockingDeque<Object> inputQueue;
    private int num;
    private Executor executor;
    private String step;
    public Performer(BlockingDeque<Object> inputQueue, Executor executor, String step){
        this.executor = executor;
        this.inputQueue = inputQueue;
        this.step = step;
        num = 0;
    }
    public void consume() {
        while (num < CommonConstant.NUM) {
            try {
                Object sra = inputQueue.take();
                executor.execute(new DownloadTask(sra));
                num ++;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        log.info(String.format(step, "步骤结束"));
    }
}
