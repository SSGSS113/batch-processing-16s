package com.ssgss.common.service.task;

import com.ssgss.SraToolKit.constant.SraToolKitConstant;
import com.ssgss.SraToolKit.constant.SraToolKitFileConstant;
import com.ssgss.SraToolKit.entity.SraDownloadDTO;
import com.ssgss.SraToolKit.service.SraToolKitService;
import com.ssgss.common.aop.annotation.ProcessTimer;
import com.ssgss.common.constant.BlockQueueConstant;
import com.ssgss.common.entity.SraDTO;

import java.io.File;
import java.util.concurrent.BlockingDeque;

public class DownloadTask extends AbstractTask{
    private final SraDTO sra;
    private static final BlockingDeque<Object> outputQueue = BlockQueueConstant.DOWNLOAD_LIST;
    private static final String type = ":Download";

    public DownloadTask(Object sra) {
        super(((SraDTO)sra).getSraId() + type);
        this.sra = (SraDTO) sra;
    }

    @Override
    @ProcessTimer("SraToolKit:downloadSra")
    public void run() {
        SraDownloadDTO sraDownloadDTO = new SraDownloadDTO();
        File SraPath = new File(SraToolKitFileConstant.DOWNLOAD_DIRECTORY, String.format("%s.sra", sra.getSraId()));
        sraDownloadDTO.setSra(sra);
        sraDownloadDTO.setSraPath(SraPath);
        if (SraToolKitService.downPrefetch(sraDownloadDTO)) {
            try {
                outputQueue.put(sraDownloadDTO);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
