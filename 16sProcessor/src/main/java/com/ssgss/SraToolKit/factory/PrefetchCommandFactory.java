package com.ssgss.SraToolKit.factory;

import com.ssgss.SraToolKit.command.PrefetchCommand;
import com.ssgss.SraToolKit.constant.SraToolKitConstant;
import com.ssgss.SraToolKit.entity.SraDownloadDTO;
import com.ssgss.common.command.Command;
import com.ssgss.common.entity.SraDTO;
import com.ssgss.fastqc.command.FastqcCommand;
import com.ssgss.fastqc.entity.FastqcRequest;

public class PrefetchCommandFactory {
    public static Command getCommand(SraDownloadDTO sra) {
        return new PrefetchCommand.Builder()
                .addArg(sra.getSra().getSraId())
                .setWorkingDirectory(SraToolKitConstant.DOWNLOAD_DIRECTORY)
                .build();
    }
}
