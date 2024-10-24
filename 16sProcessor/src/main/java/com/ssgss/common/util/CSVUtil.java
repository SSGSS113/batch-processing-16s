package com.ssgss.common.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CSVUtil {

    /**
     * 创建一个新的CSV文件，并写入表头
     *
     * @param filePath CSV文件路径
     * @param headers  表头列表
     * @throws IOException 如果创建文件或写入时发生IO异常
     */
    public static void createCSV(String filePath, List<String> headers) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // 写入表头
            writeLine(writer, headers);
        }
    }

    /**
     * 向CSV文件中添加一行数据
     *
     * @param filePath CSV文件路径
     * @param data     要添加的数据列表
     * @throws IOException 如果写入时发生IO异常
     */
    public static void addDataToCSV(String filePath, List<String> data) throws IOException {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            // 添加数据
            writeLine(writer, data);
        }
    }

    /**
     * 向CSV文件中添加数据，保证数据顺序与表头一致，缺失值补空
     *
     * @param filePath CSV文件路径
     * @param headers  表头列表，用于保证数据顺序
     * @param data     要添加的数据，使用表头名称作为键
     * @throws IOException 如果写入时发生IO异常
     */
    public static void addDataToCSV(String filePath, List<String> headers, Map<String, String> data) throws IOException {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            // 生成与表头顺序一致的行数据
            writeLine(writer, headers, data);
        }
    }

    /**
     * 将列表内容以CSV格式写入文件
     *
     * @param writer 文件写入器
     * @param values 要写入的数据列表
     * @throws IOException 如果写入时发生IO异常
     */
    private static void writeLine(FileWriter writer, List<String> values) throws IOException {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            line.append(values.get(i) != null ? values.get(i) : ""); // 处理空值
            if (i < values.size() - 1) {
                line.append(","); // 用逗号分隔值
            }
        }
        line.append("\n");
        writer.write(line.toString());
    }

    /**
     * 将与表头顺序一致的数据写入文件，数据来自Map
     *
     * @param writer 文件写入器
     * @param headers 表头，用于保证顺序
     * @param data    要写入的数据，使用Map映射表头到数据值
     * @throws IOException 如果写入时发生IO异常
     */
    private static void writeLine(FileWriter writer, List<String> headers, Map<String, String> data) throws IOException {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i);
            String value = data.getOrDefault(header, ""); // 如果没有对应的值，填入空字符串
            line.append(value);
            if (i < headers.size() - 1) {
                line.append(","); // 用逗号分隔值
            }
        }
        line.append("\n");
        writer.write(line.toString());
    }
}
