package com.ssgss.common.service.task;

import com.ssgss.SraToolKit.constant.SraToolKitFileConstant;
import com.ssgss.SraToolKit.entity.SraDownloadDTO;
import com.ssgss.SraToolKit.service.SraToolKitService;
import com.ssgss.common.aop.annotation.ProcessTimer;
import com.ssgss.common.constant.BlockQueueConstant;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.concurrent.BlockingDeque;

@Slf4j
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
        log.info("SraToolKit:doFastDump 步骤准备, Sra:{}, 处理线程: {}",
                sra.getSra().getSraId(), Thread.currentThread().getName());
        if (SraToolKitService.doFastqDump(sra)) {
            log.info("SraToolKit:doFastDump 完成, Sra:{}, 处理线程: {}",
                    sra.getSra().getSraId(), Thread.currentThread().getName());
            if(SraToolKitService.isPaired(sra)){
                sra.getSra().setPaired(true);
                sra.getSra().setLeftPath(new File(SraToolKitFileConstant.DOWNLOAD_DIRECTORY,
                        String.format("%s%s%s_1.fastq", sra.getSra().getSraId(),
                                File.separator, sra.getSra().getSraId())));
                sra.getSra().setRightPath(new File(SraToolKitFileConstant.DOWNLOAD_DIRECTORY,
                        String.format("%s%s%s_2.fastq", sra.getSra().getSraId(),
                                File.separator, sra.getSra().getSraId())));
                log.info("{} 的路径1: {}, 路径2: {}", sra.getSra().getSraId(),
                        sra.getSra().getLeftPath(), sra.getSra().getRightPath());
            }else{
                sra.getSra().setPaired(false);
                sra.getSra().setLeftPath(new File(SraToolKitFileConstant.DOWNLOAD_DIRECTORY,
                        String.format("%s%s%s.fastq", sra.getSra().getSraId(),
                                File.separator, sra.getSra().getSraId())));
                log.info("{} 的路径: {}", sra.getSra().getSraId(),
                        sra.getSra().getLeftPath());
            }
            try {
                log.info("分割了 SraID: {}, 该样品为{}",
                        sra.getSra().getSraId(),sra.getSra().isPaired()?"双端队列":"单端序列");
                outputQueue.put(sra.getSra());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
