package com.ssgss.common.service.task;

import com.ssgss.common.aop.annotation.ProcessTimer;
import com.ssgss.common.constant.BlockQueueConstant;
import com.ssgss.fastqc.entity.FastqcRequest;
import com.ssgss.fastqc.service.FastqcService;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.concurrent.BlockingDeque;

@Slf4j
public class FastqcTask extends AbstractTask{
    private final FastqcRequest sra;
    private static final BlockingDeque<Object> outputQueue = BlockQueueConstant.FASTQC_LIST;
    private static final String type = ":Fastqc";
    public FastqcTask(Object sra) {
        super(((FastqcRequest)sra).getSra().getSraId() + type);
        this.sra = (FastqcRequest) sra;
    }

    @Override
    @ProcessTimer("Fastqc:doFastqc")
    public void run() {
        log.info("{} 执行了", super.getName());
        log.info("Fastqc:doFastqc 步骤准备, Sra:{}, 处理线程: {}",
                sra.getSra().getSraId(), Thread.currentThread().getName());
        if(FastqcService.doFastqc(sra)){
            log.info("Fastqc:doFastqc 完成, Sra:{}, 处理线程: {}",
                    sra.getSra().getSraId(), Thread.currentThread().getName());
            try {
                outputQueue.put(sra.getSra());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
