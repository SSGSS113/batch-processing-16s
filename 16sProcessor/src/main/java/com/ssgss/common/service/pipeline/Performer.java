package com.ssgss.common.service.pipeline;

import com.ssgss.common.constant.CommonConstant;
import com.ssgss.common.service.task.AbstractTask;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

@Slf4j
public class Performer implements Runnable{
    private final BlockingDeque<Object> inputQueue;
    private int num;
    private final ExecutorService executor;
    private final String step;
    private final Class clazz;
    public Performer(BlockingDeque<Object> inputQueue, ExecutorService executor, String step, Class clazz){
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
                log.info("{} 步骤的队列中还剩余 {} 条数据",
                        step, inputQueue.size());
                Object sra = inputQueue.take();
                AbstractTask task = (AbstractTask) constructor.newInstance(sra);
                executor.execute(task);
                num ++;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            if(num + CommonConstant.FAILED_NUM.get() >= CommonConstant.NUM){
                executor.shutdown();
                log.info("+++++++++++++++++++++++++++++++++++++{} 已经全部加载结束, 上一步线程池已关闭++++++++++++++++++++++++++++++++++++++",
                        step);
                break;
            }
        }
        log.info(String.format(step, "步骤结束"));
    }
}
