package com.ssgss.qiime2.factory;

import com.ssgss.common.command.AbstractCommand;
import com.ssgss.common.command.Command;
import com.ssgss.common.entity.SraDTO;
import com.ssgss.qiime2.command.ImportCommand;
import com.ssgss.qiime2.constant.Bag;
import com.ssgss.qiime2.constant.Qiime2Constant;
import com.ssgss.qiime2.constant.SampleData;
import com.ssgss.qiime2.entity.SraQiime2DTO;

public class ImportCommandFactory {
    public static Command getCommand(SraQiime2DTO sra){
            return new ImportCommand.Builder()
                    .addArg("--type")
                    .addArg(sra.getType().getType())
                    .addArg("--input-path")
                    .addArg(sra.getManifest().getPath())
                    .addArg("--output-path")
                    .addArg(sra.getDemux().getPath())
                    .addArg("--input-format")
                    .addArg(sra.getBag().getBag())
                    .build();
    }
}
