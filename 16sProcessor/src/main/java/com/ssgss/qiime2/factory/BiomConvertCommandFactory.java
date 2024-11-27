package com.ssgss.qiime2.factory;

import com.ssgss.common.command.Command;
import com.ssgss.common.constant.FileConstant;
import com.ssgss.qiime2.command.BiomConvert;
import com.ssgss.qiime2.command.TaxonomyCommand;
import com.ssgss.qiime2.constant.Qiime2Constant;
import com.ssgss.qiime2.entity.SraQiime2DTO;

public class BiomConvertCommandFactory {
    public static Command getCommand(SraQiime2DTO sra){
        return new BiomConvert.Builder()
                .addArg("-i")
                .addArg(sra.getTable_biom().getPath())
                .addArg("-o")
                .addArg(sra.getTable_tsv().getPath())
                .addArg("--to-tsv")
                .build();
    }
}