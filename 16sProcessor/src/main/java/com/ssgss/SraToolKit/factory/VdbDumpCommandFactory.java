package com.ssgss.SraToolKit.factory;

import com.ssgss.SraToolKit.command.VdbDumpCommand;
import com.ssgss.SraToolKit.constant.SraToolKitConstant;
import com.ssgss.SraToolKit.constant.SraToolKitFileConstant;
import com.ssgss.SraToolKit.entity.SraDownloadDTO;
import com.ssgss.common.command.Command;
import com.ssgss.common.entity.SraDTO;

public class VdbDumpCommandFactory {
    public static Command getCommand(SraDownloadDTO sra){
        return new VdbDumpCommand.Builder()
                .addArg(sra.getSra().getSraId())
                .setWorkingDirectory(SraToolKitFileConstant.DOWNLOAD_DIRECTORY)
                .build();
    }
}
