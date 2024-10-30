package com.ssgss.SraToolKit.factory;

import com.ssgss.SraToolKit.command.FastqDumpCommand;
import com.ssgss.SraToolKit.constant.SraToolKitFileConstant;
import com.ssgss.SraToolKit.entity.SraDownloadDTO;
import com.ssgss.common.command.Command;
import com.ssgss.common.configration.FileConfig;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class FastqDumpCommandFactory {

    public static Command getCommand(SraDownloadDTO sra) {
        if(sra.getSra().isPaired()){
            return new FastqDumpCommand.Builder()
                    .addArg("--outdir")
                    .addArg(SraToolKitFileConstant.SRA_DIRECTORY.getPath())
                    .addArg("--split-files")
                    .addArg(sra.getSra().getSraId())
                    .setWorkingDirectory(SraToolKitFileConstant.DOWNLOAD_DIRECTORY)
                    .build();
        }else{
            return new FastqDumpCommand.Builder()
                    .addArg("--outdir")
                    .addArg(SraToolKitFileConstant.SRA_DIRECTORY.getPath())
                    .addArg(sra.getSra().getSraId())
                    .setWorkingDirectory(SraToolKitFileConstant.DOWNLOAD_DIRECTORY)
                    .build();
        }
    }
}
