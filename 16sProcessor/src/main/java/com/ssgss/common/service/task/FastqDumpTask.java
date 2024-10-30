package com.ssgss.common.service.task;

import com.ssgss.SraToolKit.entity.SraDownloadDTO;
import com.ssgss.SraToolKit.service.SraToolKitService;
import com.ssgss.common.aop.annotation.ProcessTimer;
import com.ssgss.common.constant.BlockQueueConstant;

import java.util.concurrent.BlockingDeque;

public class FastqDumpTask extends AbstractTask{
    private final SraDownloadDTO sra;
    private static final BlockingDeque<Object> outputQueue = BlockQueueConstant.FASTQ_DUMP;
    private static final String type = ":FastqDump";
    public FastqDumpTask(Object sra) {
        super(((SraDownloadDTO)sra).getSra().getSraId() + type);
        this.sra = (SraDownloadDTO) sra;
    }

    @Override
    @ProcessTimer("SraToolKit:doFastDump")
    public void run() {
        if (SraToolKitService.doFastqDump(sra)) {
            try {
                outputQueue.put(sra.getSra());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
