package com.ssgss.SraToolKit.factory;

import com.ssgss.SraToolKit.command.FastqDumpCommand;
import com.ssgss.SraToolKit.constant.SraToolKitConstant;
import com.ssgss.SraToolKit.entity.SraDownloadDTO;
import com.ssgss.common.command.AbstractCommand;
import com.ssgss.common.command.Command;
import com.ssgss.common.entity.SraDTO;
import com.ssgss.fastqc.command.FastqcCommand;
import com.ssgss.fastqc.entity.FastqcRequest;

public class FastqDumpCommandFactory {
    public static Command getCommand(SraDownloadDTO sra) {
        if(sra.getSra().isPaired()){
            return new FastqDumpCommand.Builder()
                    .addArg("--outdir")
                    .addArg(SraToolKitConstant.SRA_DIRECTORY.getPath())
                    .addArg("--split-files")
                    .addArg(sra.getSra().getSraId())
                    .setWorkingDirectory(SraToolKitConstant.DOWNLOAD_DIRECTORY)
                    .build();
        }else{
            return new FastqDumpCommand.Builder()
                    .addArg("--outdir")
                    .addArg(SraToolKitConstant.SRA_DIRECTORY.getPath())
                    .addArg(sra.getSra().getSraId())
                    .setWorkingDirectory(SraToolKitConstant.DOWNLOAD_DIRECTORY)
                    .build();
        }
    }
}
