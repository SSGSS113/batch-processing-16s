package com.ssgss.qiime2.command;

import com.ssgss.common.command.AbstractCommand;
import com.ssgss.qiime2.constant.Qiime2Constant;

public class ImportCommand extends AbstractCommand {
    public ImportCommand(String command){
        super(command);
    }

    private ImportCommand(Builder builder){
        super(builder.buildCommandLine());
    }

    public static class Builder extends AbstractCommand.Builder<Builder> {

        // 返回自身类型，确保链式调用
        @Override
        protected Builder self() {
            return this;
        }

        // 构建并返回具体的 Command 对象
        @Override
        public ImportCommand build() {
            this.setCommandLine(Qiime2Constant.IMPORT_BASELINE);
            return new ImportCommand(this);
        }
    }
}
