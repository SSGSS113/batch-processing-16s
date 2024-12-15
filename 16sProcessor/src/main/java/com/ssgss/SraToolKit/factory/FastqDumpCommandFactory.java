package com.ssgss.SraToolKit.factory;

import com.ssgss.SraToolKit.command.FastqDumpCommand;
import com.ssgss.SraToolKit.constant.SraToolKitConstant;
import com.ssgss.SraToolKit.constant.SraToolKitFileConstant;
import com.ssgss.SraToolKit.entity.SraDownloadDTO;
import com.ssgss.common.command.Command;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FastqDumpCommandFactory {

    public static Command getCommand(SraDownloadDTO sra) {
        return new FastqDumpCommand.Builder()
                .addArg(sra.getSraPath().getPath())
                .addArg("-e")
                .addArg(String.valueOf(SraToolKitConstant.THREADNUM))
                .addArg("-o")
                .addArg(String.format("%s.fastq", sra.getSra().getSraId()))
                .setWorkingDirectory(new File(SraToolKitFileConstant.DOWNLOAD_DIRECTORY, sra.getSra().getSraId()))
                .build();
    }
}
