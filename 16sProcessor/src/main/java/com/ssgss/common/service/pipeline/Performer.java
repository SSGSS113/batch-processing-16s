package com.ssgss.common.service.pipeline;

import com.ssgss.common.constant.CommonConstant;
import com.ssgss.common.service.task.AbstractTask;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.*;

@Slf4j
public class Performer implements Runnable{
    private final BlockingDeque<Object> inputQueue;
    private int num;
    private final Executor executor;
    private final String step;
    private final Class clazz;
    public Performer(BlockingDeque<Object> inputQueue, Executor executor, String step, Class clazz){
        this.executor = executor;
        this.inputQueue = inputQueue;
        this.step = step;
        num = 0;
        this.clazz = clazz;
    }
    public void run() {
        while (true) {
            try {
                Constructor<?> constructor = clazz.getDeclaredConstructor(Object.class);
                log.info("{} 步骤正在从阻塞队列: {} 中读取数据", step);
                Object sra = inputQueue.take();
                AbstractTask task = (AbstractTask) constructor.newInstance(sra);
                executor.execute(task);
                num ++;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            if(num >= CommonConstant.NUM){
                break;
            }
        }
        log.info(String.format(step, "步骤结束"));
    }
}
