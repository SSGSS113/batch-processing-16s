package com.ssgss.SraToolKit.service;

import com.ssgss.SraToolKit.entity.SraDownloadDTO;
import com.ssgss.SraToolKit.factory.FastqDumpCommandFactory;
import com.ssgss.SraToolKit.factory.PrefetchCommandFactory;
import com.ssgss.SraToolKit.factory.VdbDumpCommandFactory;
import com.ssgss.common.command.Command;
import com.ssgss.common.constant.SraException;
import com.ssgss.common.entity.Result;

public class SraToolKitService {
    public static boolean isPaired(SraDownloadDTO sra) throws SraException {
        Command command = VdbDumpCommandFactory.getCommand(sra);
        Result result = command.execute();
        return result.getText().equals("2");
    }

    public static boolean downPrefetch(SraDownloadDTO sra) throws SraException {
        if(sra.getSraPath().exists()){
            return true;
        }
        Command command = PrefetchCommandFactory.getCommand(sra);
            Result result = command.execute();
            if(!isSuccess(result)){
                throw new SraException(String.format("Prefetch发生异常, sra = %s",sra.getSra().getSraId()));
            }
        return result.isSucess();
    }

    public static boolean doFastqDump(SraDownloadDTO sra) throws SraException {
        Command command = FastqDumpCommandFactory.getCommand(sra);
        Result result = command.execute();
        if(!isSuccess(result)){
            throw new SraException(String.format("FastqDump发生异常, sra = %s",sra.getSra().getSraId()));
        }
        return true;
    }
    private static boolean isSuccess(Result result) {
        return result != null && result.isSucess();
    }
}