package com.ssgss.qiime2.factory;

import com.ssgss.common.command.Command;
import com.ssgss.qiime2.command.ExportCommand;
import com.ssgss.qiime2.entity.SraQiime2DTO;

public class ExportCommandFactory {
    public static Command getCommand(SraQiime2DTO sra, String inputPath, String outputPath) {
        return new ExportCommand.Builder()
                .addArg("--input-path")
                .addArg(inputPath)
                .addArg("--output-path")
                .addArg(outputPath)
                .build();
    }
}
