package com.ssgss.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
public class FileUtil {
    public static boolean deleteFile(File file){
        if(!file.exists()){
            return true;
        }
        return file.delete();
    }

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
        log.info("CSVUtil Files = {}" , files);
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

    public static void unzip(File zipPath, String destDir) throws IOException {
        log.info("解压 {}, 解压到 {}", zipPath.getPath(), destDir);
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File outFile = new File(destDir, entry.getName());
                if (entry.isDirectory()) {
                    outFile.mkdirs();
                } else {
                    outFile.getParentFile().mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(outFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry();
            }
            log.info("解压完成，解压到 {}", destDir);
        }
    }

    public static boolean MoveFileTo(String oldPath, String newPath){
        File newFile = new File(newPath);
        File oldFile = new File(oldPath);
        if(!oldFile.exists()){
            log.info("源文件 {} 不存在", oldPath);
            return false;
        }
        if(newFile.exists()){
            log.info("文件移动过程中 {} 已经存在", newPath);
            return true;
        }
        if(oldFile.renameTo(newFile)){
            log.info("{} 已经被移动到 {}", oldPath, newPath);

            return true;
        }else{
            log.info("文件 {} 移动失败", oldPath);
            return false;
        }
    }

    public static boolean isEmpty(File file) throws IOException {
        log.info("file 的路径为 {}", file);
        if((file != null) && file.isDirectory()){
            if(file.list().length > 0){
                return false;
            }else{
                return true;
            }
        }else{
            throw new IOException("该文件不是文件夹或路径为空");
        }
    }

    private static String convertToRegex(String pattern) {
        // 将通配符*替换为正则表达式的.*
        return pattern.replace("?", ".?").replace("*", ".*");
    }
    private FileUtil(){}
}
