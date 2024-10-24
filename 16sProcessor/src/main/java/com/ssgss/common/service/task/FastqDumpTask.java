package com.ssgss.common.service.task;

import com.ssgss.SraToolKit.constant.SraToolKitConstant;
import com.ssgss.SraToolKit.entity.SraDownloadDTO;
import com.ssgss.SraToolKit.service.SraToolKitService;
import com.ssgss.common.constant.CommonConstant;
import com.ssgss.common.entity.SraDTO;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.BlockingDeque;

@Service
public class DownloadTask implements Runnable{
    private SraDTO sra;
    @Resource
    private SraToolKitService service;
    private static final BlockingDeque<SraDownloadDTO> outputQueue = CommonConstant.DOWNLOAD_LIST;

    public DownloadTask(SraDTO sra) {
        this.sra = sra;
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
