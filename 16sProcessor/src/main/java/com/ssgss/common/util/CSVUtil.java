package com.ssgss.common.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.ssgss.fastqc.entity.FastqcNode;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class CSVUtil {
    private static ReentrantLock alpha_lock = new ReentrantLock();
    /**
     * 创建一个新的CSV文件，并写入表头
     *
     * @param filePath CSV文件路径
     * @param headers  表头列表
     * @throws IOException 如果创建文件或写入时发生IO异常
     */
    public static void createCSV(String filePath, List<String> headers, char sep) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // 写入表头
            writeLine(writer, headers, sep);
            log.info("{} 已经创建成功", filePath);
        }
    }

    /**
     * 向CSV文件中添加一行数据
     *
     * @param filePath CSV文件路径
     * @param data     要添加的数据列表
     * @throws IOException 如果写入时发生IO异常
     */
    public static void addDataToCSV(String filePath, List<String> data, char sep) throws IOException {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            // 添加数据
            writeLine(writer, data, sep);
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
    public static void addDataToCSV(String filePath, List<String> headers,
                                    Map<String, String> data, String rep) throws IOException {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            // 生成与表头顺序一致的行数据
            writeLine(writer, headers, data, rep);
        }
    }

    /**
     * 将列表内容以CSV格式写入文件
     *
     * @param writer 文件写入器
     * @param values 要写入的数据列表
     * @throws IOException 如果写入时发生IO异常
     */
    private static void writeLine(FileWriter writer, List<String> values, char sep) throws IOException {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            line.append(values.get(i) != null ? values.get(i) : ""); // 处理空值
            if (i < values.size() - 1) {
                line.append(sep); // 用逗号分隔值
            }
        }
        line.append("\n");
        writer.write(line.toString());
        writer.flush();
    }

    /**
     * 将与表头顺序一致的数据写入文件，数据来自Map
     *
     * @param writer 文件写入器
     * @param headers 表头，用于保证顺序
     * @param data    要写入的数据，使用Map映射表头到数据值
     * @throws IOException 如果写入时发生IO异常
     */
    private static void writeLine(FileWriter writer, List<String> headers,
                                  Map<String, String> data, String rep) throws IOException {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i);
            String value = data.getOrDefault(header, ""); // 如果没有对应的值，填入空字符串
            line.append(value);
            if (i < headers.size() - 1) {
                line.append(rep); // 用逗号分隔值
            }
        }
        line.append("\n");
        writer.write(line.toString());
        writer.flush();
    }

    public static String getValueFromCSV(String filePath, String filterColumn, String filterValue, String targetColumn) {
        String result = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                return null; // 空文件
            }

            String[] headers = headerLine.split(",");
            int filterColumnIndex = -1;
            int targetColumnIndex = -1;

            // 找到过滤列和目标列的索引
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equalsIgnoreCase(filterColumn)) {
                    filterColumnIndex = i;
                }
                if (headers[i].trim().equalsIgnoreCase(targetColumn)) {
                    targetColumnIndex = i;
                }
            }

            if (filterColumnIndex == -1 || targetColumnIndex == -1) {
                return null; // 过滤列或目标列不存在
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length > filterColumnIndex && values[filterColumnIndex].trim().equals(filterValue)) {
                    if (values.length > targetColumnIndex) {
                        result = values[targetColumnIndex].trim();
                    }
                    break; // 找到匹配行后退出
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result; // 返回结果，如果为空则返回null
    }

    public static void freshData(String filePath, List<String[]> data, String rep){
        try(FileWriter writer = new FileWriter(filePath)){
            StringBuilder sb = new StringBuilder();
            for(String[] line : data){
                for(String s : line){
                    sb.append(s).append(rep);
                }
                sb.append("\n");
            }
            writer.write(sb.toString());
            log.info("{} 已更新成功", filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String[]> getData(String filePath, String rep) {
        List<String[]> data = new LinkedList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            while((line = reader.readLine()) != null){
                data.add(line.split(rep));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public static void modifyCSV(String filePath, String keyColumn, String keyValue,
                                 String modifyColumn, String modifyValue, String rep) throws IOException {
        List<String[]> data = new ArrayList<>();
        log.info("filePath = {}", filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line = null;
            while((line = reader.readLine())!= null) {
                data.add(line.split(rep));
            }
        }
        String[] header = data.get(0);
        int keyColumnIndex = -1;
        int modifyColumnIndex = -1;
        // 获取列索引
        for (int i = 0; i < header.length; i++) {
            if (header[i].trim().equalsIgnoreCase(keyColumn.trim())) {
                keyColumnIndex = i;
            }
            if (header[i].trim().equalsIgnoreCase(modifyColumn.trim())) {
                modifyColumnIndex = i;
            }
        }

        // 如果找不到指定的列，抛出异常
        if (keyColumnIndex == -1 || modifyColumnIndex == -1) {
            log.error("keyColumn: {}, modifyColumn: {}, headers: {}", keyColumn, modifyColumn, header);
            throw new IllegalArgumentException("指定的列名不存在");
        }
        StringBuilder backData = new StringBuilder();
        // 修改数据
        for (String[] row : data) {
            if (row[keyColumnIndex].trim().equalsIgnoreCase(keyValue.trim())) {
                row[modifyColumnIndex] = modifyValue;
            }
            for(String s : row){
                backData.append(s).append(',');
            }
            backData.append("\n");
        }

        // 写回 CSV 文件
        try(FileWriter writer = new FileWriter(filePath)){
            writer.write(backData.toString());
        }
    }

    public static int countLines(String filePath) {
        int lineCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while (br.readLine() != null) {
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lineCount;
    }
    public static List<String> getHeader(File filePath ,String rep){
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            List<String> ans = new ArrayList<>();
            String line = reader.readLine();
            if(line != null){
                String[] headers = line.split(rep);
                ans.addAll(Arrays.asList(headers));
            }
            return ans;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addValueByHeader(File file, String value, String header, String rep) throws IOException{
        try(FileWriter writer = new FileWriter(file, true)) {
            Map<String, String> maps = new HashMap<>();
            maps.put(header, value);
            writeLine(writer,getHeader(file, String.valueOf(rep)), maps, rep);
        }
    }
    public static boolean findValueByHeader(File file, String value, String header, String rep) throws IOException {
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            List<String> headers = getHeader(file, ",");
            int index = -1;
            for(int i = 0;i<headers.size();i++){
                if(headers.get(i).trim().equalsIgnoreCase(header.trim())){
                    index = i;
                }
            }
            if(index == -1){
                log.error("{} 中不存在对应的 header:{}",file.getPath(), header);
                return false;
            }
            String line;
            while((line = reader.readLine()) != null){
                if(line.split(",")[index].trim().equalsIgnoreCase(value.trim())){
                    return true;
                }
            }
            return false;
        }
    }
}
