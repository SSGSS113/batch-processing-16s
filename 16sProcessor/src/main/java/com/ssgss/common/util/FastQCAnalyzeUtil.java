package com.ssgss.common.util;

import com.ssgss.fastqc.constant.FastqcFileConstant;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
@Slf4j
public class FastQCAnalyzeUtil {
    public static int analyzeFastQC(File zipFile) throws IOException {
        FileUtil.unzip(zipFile, zipFile.getParent());
        File file = new File(zipFile.getPath().substring(0, zipFile.getPath().indexOf('.')) +
                File.separator + "fastqc_data.txt");
        log.info("读取 {}", file.getPath());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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
                    if (columns.length >= 7) {

                        // 获取 10th Percentile 值
                        String[] bases = columns[0].split("-");
                        int base = Integer.parseInt(bases[0]);
                        float tenthPercentile = Float.parseFloat(columns[5]);
                        // 如果找到小于 20 的 10th Percentile，返回上一行的最右边的值
                        if (tenthPercentile < 20.0) {
                            return base;
                        }
                        // 记录最大序列数
                        maxBase = Math.max(maxBase, base);
                    }else{
                        return maxBase;
                    }
                }
            }
        }
        return -1;
    }
}
