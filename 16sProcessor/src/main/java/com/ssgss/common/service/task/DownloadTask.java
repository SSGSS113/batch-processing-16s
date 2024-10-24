package com.ssgss.common.service.task;

import com.ssgss.SraToolKit.constant.SraToolKitConstant;
import com.ssgss.SraToolKit.entity.SraDownloadDTO;
import com.ssgss.SraToolKit.service.SraToolKitService;
import com.ssgss.common.constant.BlockQueueConstant;
import com.ssgss.common.constant.CommonConstant;
import com.ssgss.common.entity.SraDTO;
import com.ssgss.qiime2.entity.SraQiime2DTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.BlockingDeque;

public class DownloadTask extends AbstractTask{
    private final SraDTO sra;
    @Resource
    private SraToolKitService service;
    private static final BlockingDeque<Object> outputQueue = BlockQueueConstant.DOWNLOAD_LIST;
    private static final String type = ":Download";

    public DownloadTask(Object sra) {
        super(((SraDTO)sra).getSraId() + type);
        this.sra = (SraDTO) sra;
    }

    @Override
    public void run() {
        SraDownloadDTO sraDownloadDTO = new SraDownloadDTO();
        File SraPath = new File(SraToolKitConstant.DOWNLOAD_DIRECTORY, String.format("%s.sra", sra.getSraId()));
        sraDownloadDTO.setSra(sra);
        sraDownloadDTO.setSraPath(SraPath);
        if (service.downPrefetch(sraDownloadDTO)) {
            try {
                outputQueue.put(sraDownloadDTO);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
