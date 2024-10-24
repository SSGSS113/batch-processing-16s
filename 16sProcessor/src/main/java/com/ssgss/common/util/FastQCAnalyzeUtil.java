package com.ssgss.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FastQCAnalyzeUtil {
    public static int analyzeFastQC(File zipFile) throws IOException {
        // 打开zip文件
        try (ZipFile zip = new ZipFile(zipFile)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                
                // 找到目标文件 fastqc_data.txt
                if (entry.getName().equals("fastqc_data.txt")) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(zip.getInputStream(entry)))) {
                        String line;
                        boolean isDataSection = false;
                        int maxBase = Integer.MIN_VALUE;
                        // 读取文件中的行
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith(">>Per base sequence quality")) {
                                // 找到表头
                                isDataSection = true;
                                reader.readLine();
                                continue;
                            }
                            if (isDataSection) {
                                String[] columns = line.trim().split("\\s+");
                                if (columns.length < 7) continue; // 确保有足够的列
                                
                                // 获取 10th Percentile 值
                                String[] bases = columns[0].split("-");
                                int base = Integer.parseInt(bases[0]);
                                float tenthPercentile = Float.parseFloat(columns[5]);
                                // 如果找到小于 20 的 10th Percentile，返回上一行的最右边的值
                                if (tenthPercentile < 20.0) {
                                    return maxBase;
                                }
                                // 记录最大序列数
                                maxBase = Math.max(maxBase, base);
                            }
                            if(line.trim().equals(">>END_MODULE")){
                                return maxBase;
                            }
                        }
                    }
                }
            }
        }
        return -1;
    }
}
