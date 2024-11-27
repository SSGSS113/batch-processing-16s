package com.ssgss.common.service.task;

import com.ssgss.SraToolKit.constant.SraToolKitFileConstant;
import com.ssgss.SraToolKit.entity.SraDownloadDTO;
import com.ssgss.SraToolKit.service.SraToolKitService;
import com.ssgss.common.constant.BlockQueueConstant;
import com.ssgss.common.constant.CommonConstant;
import com.ssgss.common.constant.SraException;
import com.ssgss.common.entity.SraDTO;
import com.ssgss.common.util.CSVUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    public void run() {
        log.info("{} 执行了", super.getName());
        log.info("SraToolKit:downloadSra 步骤准备, Sra:{}, 处理线程: {}",
                sra.getSraId(), Thread.currentThread().getName());
        SraDownloadDTO sraDownloadDTO = new SraDownloadDTO();
        File SraPath = new File(SraToolKitFileConstant.DOWNLOAD_DIRECTORY,
                String.format("%s%s%s.sra", sra.getSraId(), File.separator, sra.getSraId()));
        sraDownloadDTO.setSra(sra);
        sraDownloadDTO.setSraPath(SraPath);
        try {
            if (SraToolKitService.downPrefetch(sraDownloadDTO)) {
                log.info("SraToolKit:downloadSra 完成, Sra:{}, 处理线程: {}",
                        sra.getSraId(), Thread.currentThread().getName());
                outputQueue.put(sraDownloadDTO);
                CommonConstant.DOWNLOAD_SUCCEED.getAndIncrement();
            }else {
                CommonConstant.DOWNLOAD_FAILED.getAndIncrement();
                CommonConstant.FAILED_NUM.getAndIncrement();
                CommonConstant.putData(sra.getSraId(), type, "下载遇到未知错误");
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } catch (SraException e) {
            CommonConstant.DOWNLOAD_FAILED.getAndIncrement();
            CommonConstant.FAILED_NUM.getAndIncrement();
            CommonConstant.putData(sra.getSraId(), type, e.getMessage());
        } finally {
            log.info("===========================================DOWNLOAD============================================");
            log.info("一共有 {} 条数据，已经处理了 {} 条，其中 {} 成功，共 {} 失败，本步骤失败 {}", CommonConstant.NUM,
                    CommonConstant.DOWNLOAD_FAILED.get() + CommonConstant.FAILED_NUM.get(),
                    CommonConstant.DOWNLOAD_SUCCEED.get(),
                    CommonConstant.FAILED_NUM,
                    CommonConstant.DOWNLOAD_FAILED.get());
            log.info("===========================================DOWNLOAD============================================");
        }
    }
}
