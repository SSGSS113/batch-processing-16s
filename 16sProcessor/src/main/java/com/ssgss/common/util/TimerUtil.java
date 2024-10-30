package com.ssgss.common.util;

import java.util.concurrent.TimeUnit;

public class TimerUtil {
    private long startTime;
    private long pausedTime;
    private boolean isRunning;
    private String name;
    private final TimeUnit timeUnit;

    // 构造函数，指定计时单位
    private TimerUtil(TimeUnit timeUnit, String name) {
        this.timeUnit = timeUnit;
        this.name = name;
    }

    // 工厂方法，用于创建并启动计时器
    public static TimerUtil startNew(TimeUnit timeUnit, String name) {
        return new TimerUtil(timeUnit, name);
    }

    // 开始计时
    public void start() {
        startTime = System.nanoTime();
        pausedTime = 0;
        isRunning = true;
    }

    // 暂停计时
    public void pause() {
        if (isRunning) {
            pausedTime = System.nanoTime() - startTime;
            isRunning = false;
        }
    }

    // 继续计时
    public void resume() {
        if (!isRunning && pausedTime != 0) {
            startTime = System.nanoTime() - pausedTime;
            isRunning = true;
        }
    }

    // 重置计时器
    public void reset() {
        startTime = 0;
        pausedTime = 0;
        isRunning = false;
    }

    // 获取已计时间，根据指定单位返回结果
    public double getElapsedTime() {
        long elapsedTime = isRunning ? System.nanoTime() - startTime : pausedTime;

        switch (timeUnit) {
            case SECONDS:
                return elapsedTime / 1_000_000_000.0;
            case MILLISECONDS:
                return elapsedTime / 1_000_000.0;
            case MICROSECONDS:
                return elapsedTime / 1_000.0;
            case MINUTES:
                return elapsedTime / (60 * 1_000_000_000.0);
            case HOURS:
                return elapsedTime / (3600 * 1_000_000_000.0);
            default:
                return elapsedTime; // 默认为纳秒
        }
    }
}