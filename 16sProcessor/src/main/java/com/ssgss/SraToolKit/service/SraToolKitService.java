package com.ssgss.SraToolKit.service;

import com.ssgss.SraToolKit.constant.SraToolKitFileConstant;
import com.ssgss.SraToolKit.entity.SraDownloadDTO;
import com.ssgss.SraToolKit.factory.FastqDumpCommandFactory;
import com.ssgss.SraToolKit.factory.PrefetchCommandFactory;
import com.ssgss.SraToolKit.factory.VdbDumpCommandFactory;
import com.ssgss.common.aop.annotation.ProcessTimer;
import com.ssgss.common.command.Command;
import com.ssgss.common.constant.SraException;
import com.ssgss.common.entity.Result;
import com.ssgss.common.util.FileUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class SraToolKitService {
    public static boolean isPaired(SraDownloadDTO sra) throws SraException {
        List<File> files = FileUtil.searchFiles(new File(SraToolKitFileConstant.DOWNLOAD_DIRECTORY,
                sra.getSra().getSraId()),String.format("*%s*", sra.getSra().getSraId()));
        int size = files.size();
        log.info("Sra: {} , files: {} , size = {}", sra.getSra().getSraId(), files, size);
        return size == 3;
    }

    @ProcessTimer("SraToolKit:downloadSra")
    public static boolean downPrefetch(SraDownloadDTO sra) throws SraException {
        File sraDownloadDir = new File(SraToolKitFileConstant.DOWNLOAD_DIRECTORY, sra.getSra().getSraId());
        try {
            if(!FileUtil.isEmpty(sraDownloadDir)){
                return true;
            }
        } catch (IOException e) {
            log.info("{} 还未下载", sra.getSra().getSraId());
        }
        Command command = PrefetchCommandFactory.getCommand(sra);
            Result result = command.execute();
            if(!isSuccess(result)){
                log.error(String.format("Prefetch发生异常, sra = %s",sra.getSra().getSraId()));
                return false;
            }
        return true;
    }

    @ProcessTimer("SraToolKit:doFastDump")
    public static boolean doFastqDump(SraDownloadDTO sra) throws SraException {
        if(FileUtil.searchFiles(new File(SraToolKitFileConstant.DOWNLOAD_DIRECTORY,
                sra.getSra().getSraId()),String.format("*%s*", sra.getSra().getSraId())).size()>1){
            return true;
        }
        Command command = FastqDumpCommandFactory.getCommand(sra);
        Result result = command.execute();
        if(!isSuccess(result)){
            log.error(String.format("FastqDump发生异常, sra = %s",sra.getSra().getSraId()));
            return false;
        }
        return true;
    }
    private static boolean isSuccess(Result result) {
        return result != null && result.isSucess();
    }
}