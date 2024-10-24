package com.ssgss.qiime2.factory;

import com.ssgss.common.command.Command;
import com.ssgss.common.entity.SraDTO;
import com.ssgss.qiime2.command.AlphaCommand;
import com.ssgss.qiime2.command.ImportCommand;
import com.ssgss.qiime2.constant.Bag;
import com.ssgss.qiime2.constant.Qiime2Constant;
import com.ssgss.qiime2.constant.SampleData;
import com.ssgss.qiime2.entity.SraQiime2DTO;

public class AlphaCommandFactory {
    public static Command getCommand(SraQiime2DTO sra, String metric, String output_path){
        return new AlphaCommand.Builder()
                .addArg("--i-table")
                .addArg(sra.getTable().getPath())
                .addArg("--p-metric")
                .addArg(metric)
                .addArg("--o-alpha-diversity")
                .addArg(output_path)
                .build();
    }
}
