package com.ssgss.fastqc.command;

import com.ssgss.common.command.AbstractCommand;
import com.ssgss.fastqc.constant.FastqcConstant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;

@Setter
@Getter
@NoArgsConstructor
public class FastqcCommand extends AbstractCommand {
    public FastqcCommand (String command, File file){
        super(command, file);
    }

    private FastqcCommand(Builder builder){
        super(builder.buildCommandLine(), builder.workingDirectory);
    }

    public static class Builder extends AbstractCommand.Builder<Builder> {
        private File workingDirectory;

        public Builder setWorkingDirectory(File workingDirectory) {
            this.workingDirectory = workingDirectory;
            return self();
        }

        // 返回自身类型，确保链式调用
        @Override
        protected Builder self() {
            return this;
        }

        // 构建并返回具体的 FastqcCommand 对象
        @Override
        public FastqcCommand build() {
            setCommandLine(FastqcConstant.FastqcBaseLine);
            return new FastqcCommand(this);
        }
    }
}
