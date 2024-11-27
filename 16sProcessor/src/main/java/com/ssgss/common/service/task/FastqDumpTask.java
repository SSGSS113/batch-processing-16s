package com.ssgss.common.service.task;

import com.ssgss.SraToolKit.constant.SraToolKitFileConstant;
import com.ssgss.SraToolKit.entity.SraDownloadDTO;
import com.ssgss.SraToolKit.service.SraToolKitService;
import com.ssgss.common.constant.BlockQueueConstant;
import com.ssgss.common.constant.CommonConstant;
import com.ssgss.common.constant.SraException;
import com.ssgss.common.util.CSVUtil;
import com.ssgss.common.util.FileUtil;
import com.ssgss.fastqc.constant.FastqcFileConstant;
import com.ssgss.fastqc.entity.FastqcRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    public void run() {
        log.info("{} 执行了", super.getName());
        log.info("SraToolKit:doFastDump 步骤准备, Sra:{}, 处理线程: {}",
                sra.getSra().getSraId(), Thread.currentThread().getName());
        try {
            if (SraToolKitService.doFastqDump(sra)) {
                log.info("SraToolKit:doFastDump 完成, Sra:{}, 处理线程: {}",
                        sra.getSra().getSraId(), Thread.currentThread().getName());
                if (SraToolKitService.isPaired(sra)) {
                    sra.getSra().setPaired(true);
                    sra.getSra().setLeftPath(new File(SraToolKitFileConstant.DOWNLOAD_DIRECTORY,
                            String.format("%s%s%s_1.fastq", sra.getSra().getSraId(),
                                    File.separator, sra.getSra().getSraId())));
                    sra.getSra().setRightPath(new File(SraToolKitFileConstant.DOWNLOAD_DIRECTORY,
                            String.format("%s%s%s_2.fastq", sra.getSra().getSraId(),
                                    File.separator, sra.getSra().getSraId())));
                    log.info("{} 的路径1: {}, 路径2: {}", sra.getSra().getSraId(),
                            sra.getSra().getLeftPath(), sra.getSra().getRightPath());
                } else {
                    sra.getSra().setPaired(false);
                    sra.getSra().setLeftPath(new File(SraToolKitFileConstant.DOWNLOAD_DIRECTORY,
                            String.format("%s%s%s.fastq", sra.getSra().getSraId(),
                                    File.separator, sra.getSra().getSraId())));
                    log.info("{} 的路径: {}", sra.getSra().getSraId(),
                            sra.getSra().getLeftPath());
                }
                    log.info("分割了 SraID: {}, 该样品为{}",
                            sra.getSra().getSraId(), sra.getSra().isPaired() ? "双端队列" : "单端序列");
                    FastqcRequest fastqcRequest = new FastqcRequest();
                    fastqcRequest.setSra(sra.getSra());
                    File outputPath = new File(FastqcFileConstant.FASTQC_PATH, sra.getSra().getSraId());
                    fastqcRequest.setOutPutPath(outputPath);
                    FileUtil.createDirectory(outputPath);
                    outputQueue.put(fastqcRequest);
                    CommonConstant.FASTQ_DUMP_SUCCEED.getAndIncrement();
            }else{
                CommonConstant.FASTQ_DUMP_FAILED.getAndIncrement();
                CommonConstant.FAILED_NUM.getAndIncrement();
                CommonConstant.putData(sra.getSra().getSraId(), type, "FastqDumpTask 遇到未知异常");
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } catch (SraException e){
            CommonConstant.FASTQ_DUMP_FAILED.getAndIncrement();
            CommonConstant.FAILED_NUM.getAndIncrement();
            CommonConstant.putData(sra.getSra().getSraId(), type, e.getMessage());
        }finally {
            log.info("=========================================FASTQ_DUMP===========================================");
            log.info("一共有 {} 条数据，已经处理了 {} 条，其中 {} 成功，共 {} 失败，本步骤失败 {}", CommonConstant.NUM,
                    CommonConstant.FASTQ_DUMP_SUCCEED.get() + CommonConstant.FAILED_NUM.get(),
                    CommonConstant.FASTQ_DUMP_SUCCEED.get(),
                    CommonConstant.FAILED_NUM.get(),
                    CommonConstant.FASTQ_DUMP_FAILED.get());
            log.info("=========================================FASTQ_DUMP============================================");
        }
    }
}
