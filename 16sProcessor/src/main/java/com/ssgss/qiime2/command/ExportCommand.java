package com.ssgss.qiime2.command;

import com.ssgss.common.command.AbstractCommand;
import com.ssgss.qiime2.constant.Qiime2Constant;

public class ExportCommand extends AbstractCommand {
    ExportCommand(Builder builder){
        super(builder.buildCommandLine());
    }
    public static class Builder extends AbstractCommand.Builder<Builder> {

        // 返回自身类型，确保链式调用
        @Override
        protected ExportCommand.Builder self() {
            return this;
        }

        // 构建并返回具体的 Command 对象
        @Override
        public ExportCommand build() {
            this.setCommandLine(Qiime2Constant.EXPORT_BASELINE);
            return new ExportCommand(this);
        }
    }
}
