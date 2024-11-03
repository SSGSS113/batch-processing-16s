package com.ssgss.common.service.task;

import com.ssgss.SraToolKit.constant.SraToolKitConstant;
import com.ssgss.SraToolKit.constant.SraToolKitFileConstant;
import com.ssgss.SraToolKit.entity.SraDownloadDTO;
import com.ssgss.SraToolKit.service.SraToolKitService;
import com.ssgss.common.aop.annotation.ProcessTimer;
import com.ssgss.common.constant.BlockQueueConstant;
import com.ssgss.common.entity.SraDTO;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.concurrent.BlockingDeque;

@Slf4j
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
        log.info("{} 执行了", super.getName());
        log.info("SraToolKit:downloadSra 步骤准备, Sra:{}, 处理线程: {}",
                sra.getSraId(), Thread.currentThread().getName());
        SraDownloadDTO sraDownloadDTO = new SraDownloadDTO();
        File SraPath = new File(SraToolKitFileConstant.DOWNLOAD_DIRECTORY,
                String.format("%s%s%s.sra", sra.getSraId(), File.separator, sra.getSraId()));
        sraDownloadDTO.setSra(sra);
        sraDownloadDTO.setSraPath(SraPath);
        if (SraToolKitService.downPrefetch(sraDownloadDTO)) {
            log.info("SraToolKit:downloadSra 完成, Sra:{}, 处理线程: {}",
                    sra.getSraId(), Thread.currentThread().getName());
            try {
                outputQueue.put(sraDownloadDTO);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
