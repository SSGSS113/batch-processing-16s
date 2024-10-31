package com.ssgss.common.service.task;

import com.ssgss.common.aop.annotation.ProcessTimer;
import com.ssgss.common.constant.BlockQueueConstant;
import com.ssgss.fastqc.entity.FastqcRequest;
import com.ssgss.fastqc.service.FastqcService;
import lombok.extern.slf4j.Slf4j;

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
        if(FastqcService.doFastqc(sra)){
            try {
                log.info("完成了 SraId: {} 的质量控制", sra.getSra().getSraId());
                outputQueue.put(sra);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
