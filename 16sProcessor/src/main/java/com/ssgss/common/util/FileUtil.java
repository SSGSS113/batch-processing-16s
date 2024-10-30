package com.ssgss.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Slf4j
public class FileUtil {
    public static boolean createDirectory(File file) {
        String dirPath = file.getPath();
        if(!file.isDirectory()){
            log.error(String.format("并非文件夹: %s", dirPath));
            return false;
        }
        if (!file.exists()) {
            file.mkdir();
            return true; // 文件夹创建成功
        } else {
            log.info(String.format("文件夹已存在: %s", dirPath));
            return true; // 文件夹已存在
        }
    }
    public static boolean createFile(File file) {
        String path = file.getPath();
        if (!file.isFile()) {
            log.error(String.format("非文件: %s", path));
            return false;
        }
        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    return true; // 文件创建成功
                } else {
                    System.out.println("文件已存在: " + path);
                    return false; // 文件已存在
                }
            }
        } catch(IOException e){
            System.err.println("创建文件时出错: " + e.getMessage());
            return false; // 文件创建失败
        }
        return true;
    }
    private FileUtil(){}
}
