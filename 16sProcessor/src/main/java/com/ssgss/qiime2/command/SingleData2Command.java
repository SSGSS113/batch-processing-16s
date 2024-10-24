package com.ssgss.qiime2.command;

import com.ssgss.common.command.AbstractCommand;
import com.ssgss.qiime2.constant.Qiime2Constant;

public class SingleData2Command extends AbstractCommand {
    SingleData2Command(Builder builder){
        super(builder.buildCommandLine());
    }
    public static class Builder extends AbstractCommand.Builder<SingleData2Command.Builder> {

        // 返回自身类型，确保链式调用
        @Override
        protected SingleData2Command.Builder self() {
            return this;
        }

        // 构建并返回具体的 Command 对象
        @Override
        public SingleData2Command build() {
            this.setCommandLine(Qiime2Constant.SINGLE_DATA2_BASELINE);
            return new SingleData2Command(this);
        }
    }
}
