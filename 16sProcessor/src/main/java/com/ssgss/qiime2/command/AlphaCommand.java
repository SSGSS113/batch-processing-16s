package com.ssgss.qiime2.command;

import com.ssgss.common.command.AbstractCommand;
import com.ssgss.qiime2.constant.Qiime2Constant;

public class AlphaCommand extends AbstractCommand {
    AlphaCommand(Builder builder){
        super(builder.buildCommandLine());
    }
    public static class Builder extends AbstractCommand.Builder<AlphaCommand.Builder> {

        // 返回自身类型，确保链式调用
        @Override
        protected AlphaCommand.Builder self() {
            return this;
        }

        // 构建并返回具体的 Command 对象
        @Override
        public AlphaCommand build() {
            this.setCommandLine(Qiime2Constant.ALPHA_DIV_BASELINE);
            return new AlphaCommand(this);
        }
    }
}
