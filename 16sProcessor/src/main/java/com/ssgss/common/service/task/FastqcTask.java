package com.ssgss.common.service.task;

import com.ssgss.SraToolKit.constant.SraToolKitConstant;
import com.ssgss.SraToolKit.entity.SraDownloadDTO;
import com.ssgss.SraToolKit.service.SraToolKitService;
import com.ssgss.common.constant.CommonConstant;
import com.ssgss.common.entity.SraDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.BlockingDeque;

@Service
public class FastqDumpTask implements Runnable{
    private SraDownloadDTO sra;
    @Resource
    private SraToolKitService service;
    private static final BlockingDeque<SraDTO> outputQueue = CommonConstant.FASTQ_DUMP;

    public FastqDumpTask(Object sra) {
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
