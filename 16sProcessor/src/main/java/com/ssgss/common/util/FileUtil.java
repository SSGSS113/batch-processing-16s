package com.ssgss.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FileUtil {
    public static boolean createDirectory(File file) {
        String dirPath = file.getPath();
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
    /**
     * 在指定目录中模糊查询文件
     *
     * @param directory 要查询的目录路径
     * @param pattern       要匹配的文件名模式（支持简单的通配符*）
     * @return 符合条件的文件列表
     */
    public static List<File> searchFiles(File directory, String pattern) {
        List<File> matchingFiles = new ArrayList<>();

        if (directory.isDirectory()) {
            searchFilesRecursive(directory, pattern, matchingFiles);
        } else {
            System.out.println("指定的路径不是一个目录");
        }

        return matchingFiles;
    }

    private static void searchFilesRecursive(File directory, String pattern, List<File> matchingFiles) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                // 使用通配符进行匹配
                if (file.isDirectory()) {
                    searchFilesRecursive(file, pattern, matchingFiles);
                } else if (file.getName().matches(convertToRegex(pattern))) {
                    matchingFiles.add(file);
                }
            }
        }
    }

    private static String convertToRegex(String pattern) {
        // 将通配符*替换为正则表达式的.*
        return pattern.replace("?", ".?").replace("*", ".*");
    }
    private FileUtil(){}
}
