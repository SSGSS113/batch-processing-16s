package com.ssgss.SraToolKit.service.impl;

import com.ssgss.SraToolKit.entity.SraDownloadDTO;
import com.ssgss.SraToolKit.factory.FastqDumpCommandFactory;
import com.ssgss.SraToolKit.factory.PrefetchCommandFactory;
import com.ssgss.SraToolKit.factory.VdbDumpCommandFactory;
import com.ssgss.SraToolKit.service.SraToolKitService;
import com.ssgss.common.command.Command;
import com.ssgss.common.constant.SraException;
import org.springframework.stereotype.Service;

@Service
public class SraToolKitServiceImpl implements SraToolKitService {
    @Override
    public boolean isPaired(SraDownloadDTO sra) throws SraException {
        Command command = VdbDumpCommandFactory.getCommand(sra);
        String callBack = command.execute();
        return callBack.equals("2");
    }

    @Override
    public boolean downPrefetch(SraDownloadDTO sra) throws SraException {
        if(sra.getSraPath().exists()){
            return true;
        }
        Command command = PrefetchCommandFactory.getCommand(sra);
        try {
            String callBack = command.execute();
        }catch (SraException e){
            throw new SraException(String.format("Prefetch发生异常, sra = %s",sra.getSra().getSraId()), e);
        }
        return true;
    }

    @Override
    public boolean doFastqDump(SraDownloadDTO sra) throws SraException {
        Command command = FastqDumpCommandFactory.getCommand(sra);
        try {
            String callBack = command.execute();
        }catch (SraException e) {
            throw new SraException(String.format("FastqDump发生异常, sra = %s",sra.getSra().getSraId()), e);
        }
        return true;
    }
}