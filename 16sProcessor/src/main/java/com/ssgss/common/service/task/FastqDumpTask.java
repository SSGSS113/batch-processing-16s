package com.ssgss.common.service.task;

import com.ssgss.SraToolKit.entity.SraDownloadDTO;
import com.ssgss.SraToolKit.service.SraToolKitService;
import com.ssgss.common.constant.BlockQueueConstant;
import com.ssgss.common.constant.CommonConstant;
import com.ssgss.common.entity.SraDTO;
import com.ssgss.qiime2.entity.SraQiime2DTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingDeque;

public class FastqDumpTask extends AbstractTask{
    private final SraDownloadDTO sra;
    @Resource
    private SraToolKitService service;
    private static final BlockingDeque<Object> outputQueue = BlockQueueConstant.FASTQ_DUMP;
    private static final String type = ":FastqDump";
    public FastqDumpTask(Object sra) {
        super(((SraDownloadDTO)sra).getSra().getSraId() + type);
        this.sra = (SraDownloadDTO) sra;
    }

    @Override
    public void run() {
        if (service.doFastqDump(sra)) {
            try {
                outputQueue.put(sra.getSra());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
