package com.ssgss.common.aop;

import com.ssgss.common.aop.annotation.ProcessTimer;
import com.ssgss.common.util.TimerUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class ExecutionTimeAspect {
    @Pointcut("@annotation(com.ssgss.common.aop.annotation.ProcessTimer)")
    private void pointcut(){}
    // 存储方法的总耗时和调用次数
    private Map<String, ExecutionStats> executionStatsMap = new HashMap<>();
    @Around("pointcut()")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        Method method = joinPoint.getTarget().getClass().getMethod(methodName, parameterTypes);
        ProcessTimer processTimer = method.getAnnotation(ProcessTimer.class);
        String stepName = processTimer.value();
        ExecutionStats stats = executionStatsMap.getOrDefault(stepName,
                new ExecutionStats(stepName, TimerUtil.startNew(TimeUnit.MINUTES, stepName)));
        TimerUtil curTimer = TimerUtil.startNew(TimeUnit.MINUTES, stepName);
        curTimer.start();
        stats.start();
        try {
            return joinPoint.proceed();
        } finally {
            curTimer.pause();
            stats.pause();
            log.info(String.format("本次执行方法：%s ，本次方法执行了 %.2f min，平均执行了 %.2f min"
                    , stepName
                    , curTimer.getElapsedTime()
                    , stats.getAverageTime()));
            // 更新执行统计
            executionStatsMap.putIfAbsent(stepName, stats);
        }
    }

    // 内部类，用于存储统计信息
    private static class ExecutionStats {
        private String stepName;
        private long count;
        private TimerUtil pre;

        private ExecutionStats(String methodName, TimerUtil pre) {
            this.stepName = methodName;
            count = 0;
            this.pre = pre;
        }

        protected String getMethodName() {
            return stepName;
        }

        protected void setMethodName(String methodName) {
            this.stepName = methodName;
        }

        protected long getCount() {
            return count;
        }

        protected void setCount(long count) {
            this.count = count;
        }

        protected TimerUtil getPre() {
            return pre;
        }

        protected void setPre(TimerUtil pre) {
            this.pre = pre;
        }
        private void addCount(){
            count ++;
        }
        protected void start(){
            pre.start();
        }
        protected void pause(){
            pre.pause();
        }
        protected double getAverageTime(){
            addCount();
            double ans = pre.getElapsedTime()/count;
            pause();
            return ans;
        }
    }
}
