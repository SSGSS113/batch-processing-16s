package com.ssgss.fastqc.factory;

import com.ssgss.common.command.Command;
import com.ssgss.fastqc.command.FastqcCommand;
import com.ssgss.fastqc.entity.FastqcRequest;

public class FastqcCommandFactory {
    public static Command getCommand(FastqcRequest request) {
        if(request.getSra().isPaired()) {
            return new FastqcCommand.Builder()
                    .addArg("-o")
                    .addArg(request.getOutPutPath().getPath())
                    .addArg(request.getSra().getLeftPath().getPath())
                    .addArg(request.getSra().getRightPath().getPath())
                    .build();
        }else{
            return new FastqcCommand.Builder()
                    .addArg("-o")
                    .addArg(request.getOutPutPath().getPath())
                    .addArg(request.getSra().getLeftPath().getPath())
                    .build();
        }
    }
}
