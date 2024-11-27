package com.ssgss.common.command;

import com.ssgss.common.aop.annotation.HandleException;
import com.ssgss.common.constant.FileConstant;
import com.ssgss.common.constant.SraException;
import com.ssgss.common.entity.Result;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Service
@Slf4j
public abstract class AbstractCommand implements Command{
    private final Runtime runtime = Runtime.getRuntime();
    private File workingDirectory;
    private String command;
    public AbstractCommand(String command){
        this.command = command;
        workingDirectory = FileConstant.getWorkDirectory();
    }
    public AbstractCommand(String command, File workingDirectory){
        this.command = command;
        this.workingDirectory = workingDirectory;
    }

    @Override
    @HandleException
    public Result execute() throws SraException{
        try {
            log.info("命令内容: {} ,执行线程是: {}", command, Thread.currentThread().getName());
            Process process = runtime.exec(command, null, workingDirectory);
            // 读取并输出执行结果
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            String outPutText;
            StringBuilder buffer = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append(System.lineSeparator());
            }
            String error;
            StringBuilder sb = new StringBuilder();
            while((line = stdError.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
            error = sb.toString();
            outPutText = buffer.toString();
            // 等待进程完成
            int exitCode = process.waitFor();
            if(exitCode == 0){
                return new Result.Builder().setSucess(true).setText(outPutText).build();
            }else{
                log.error(error);
                return new Result.Builder().setSucess(false).setText(error).build();
            }
        } catch (Exception e) {
            throw new SraException(e);
        }
    }
    public static abstract class Builder<T extends Builder<T>> {
        private String commandLine;

        protected List<String> arguments = new ArrayList<>();

        // 设置命令行
        protected T setCommandLine(String commandLine) {
            this.commandLine = commandLine;
            return self();
        }

        // 设置工作目录
        public T addArg(String arg){
            arguments.add(arg);
            return self();
        }

        public String buildCommandLine(){
            StringBuilder sb = new StringBuilder();
            sb.append(this.commandLine);
            for(String arg : arguments){
                sb.append(" ").append(arg);
            }
            return sb.toString();
        }
        // 抽象方法返回子类类型，确保链式调用
        protected abstract T self();
        // 构建抽象的命令对象，具体实现由子类完成
        public abstract AbstractCommand build();
    }
}