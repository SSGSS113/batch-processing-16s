package com.ssgss.common.aop;

import com.ssgss.common.aop.annotation.HandleException;
import com.ssgss.common.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class ExceptionHandlingAspect {
    @Pointcut("@annotation(com.ssgss.common.aop.annotation.HandleException)")
    private void pointcut(){}

    @Around("pointcut()") // 注解切点
    public Object handleMethodException(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        String methodName = joinPoint.getSignature().getName();
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        Method method = joinPoint.getTarget().getClass().getMethod(methodName, parameterTypes);
        HandleException handleException = method.getAnnotation(HandleException.class);
        String message = handleException.message();
        try {
            return joinPoint.proceed(); // 执行目标方法
        } catch (Throwable throwable) {
            // 获取注解中的信息
            log.error("Exception in method: {}", methodName);
            log.error("Custom message: {}", message);
            log.error("Exception parameters: {}", (Object[]) method.getParameters());
            log.error("Exception details: {}", throwable.getMessage());
            return new Result.Builder().setText(throwable.getMessage()).setSucess(false).build();
        }
    }
}