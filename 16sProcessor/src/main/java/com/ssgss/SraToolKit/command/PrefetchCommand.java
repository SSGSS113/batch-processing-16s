package com.ssgss.SraToolKit.command;

import com.ssgss.SraToolKit.constant.SraToolKitConstant;
import com.ssgss.common.command.AbstractCommand;
import com.ssgss.qiime2.command.ImportCommand;

import java.io.File;

public class PrefetchCommand extends AbstractCommand {
    PrefetchCommand(String commond, File work_directory){
        super(commond, work_directory);
    }

    private PrefetchCommand(Builder builder){
        super(builder.buildCommandLine(), builder.workingDirectory);
    }

    public static class Builder extends AbstractCommand.Builder<Builder> {
        private File workingDirectory;
        // 返回自身类型，确保链式调用

        public Builder setWorkingDirectory(File workingDirectory){
            this.workingDirectory = workingDirectory;
            return self();
        }
        @Override
        protected Builder self() {
            return this;
        }

        // 构建并返回具体的 Command 对象
        @Override
        public PrefetchCommand build() {
            this.setCommandLine(SraToolKitConstant.PREFETCH);
            return new PrefetchCommand(this);
        }
    }
}
