package com.ssgss.qiime2.factory;

import com.ssgss.common.command.Command;
import com.ssgss.qiime2.command.PairedData2Command;
import com.ssgss.qiime2.constant.Qiime2Constant;
import com.ssgss.qiime2.entity.SraQiime2DTO;

public class PairedData2CommandFactory {
    public static Command getCommand(SraQiime2DTO sra){
        return new PairedData2Command.Builder()
                .addArg("--i-demultiplexed-seqs")
                .addArg(sra.getDemux().getPath())
                .addArg("--p-n-threads")
                .addArg(String.valueOf(Qiime2Constant.THREAD))
                .addArg("--p-trim-left-f")
                .addArg(String.valueOf(sra.getSra().getLeftTrim()))
                .addArg("--p-trim-left-r")
                .addArg(String.valueOf(sra.getSra().getRightTrim()))
                .addArg("--p-trunc-len-f")
                .addArg(String.valueOf(sra.getSra().getLeftLen()))
                .addArg("--p-trunc-len-r")
                .addArg(String.valueOf(sra.getSra().getRightLen()))
                .addArg("--o-representative-sequences")
                .addArg(sra.getRep().getPath())
                .addArg("--o-table")
                .addArg(sra.getTable().getPath())
                .addArg("--o-denoising-stats")
                .addArg(sra.getStats().getPath())
                .build();
    }
}