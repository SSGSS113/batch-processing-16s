package com.ssgss.qiime2.factory;

import com.ssgss.common.command.Command;
import com.ssgss.common.constant.FileConstant;
import com.ssgss.qiime2.command.TaxonomyCommand;
import com.ssgss.qiime2.constant.Qiime2Constant;
import com.ssgss.qiime2.entity.SraQiime2DTO;

public class TaxonomyCommandFactory {
    public static Command getCommand(SraQiime2DTO sra){
        return new TaxonomyCommand.Builder()
                .addArg("classify-sklearn")
                .addArg("--i-classifier")
                .addArg(FileConstant.getCLASSIFIER().getPath())
                .addArg("--i-reads")
                .addArg(sra.getRep().getPath())
                .addArg("--o-classification")
                .addArg(sra.getTaxonomy().getPath())
                .build();
    }
}
