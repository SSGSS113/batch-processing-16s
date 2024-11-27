package com.ssgss.common.constant;

import com.ssgss.common.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
@Slf4j
public final class FileConstant {
    private static File WORK_DIRECTORY;
    private static File FILES;
    private static File CLASSIFIER;
    private static File CSV_PATH;
    public static File getWorkDirectory() {
        return WORK_DIRECTORY;
    }
    public static void setWorkDirectory(File workDirectory) {
        log.info(String.format("workDirectory 设置为: %s", workDirectory.getPath()));
        WORK_DIRECTORY = workDirectory;
        FileUtil.createDirectory(WORK_DIRECTORY);
    }

    public static File getFILES() {
        return FILES;
    }

    public static void setFILES(File FILES) {
        log.info(String.format("FILES 设置为: %s", FILES.getPath()));
        FileConstant.FILES = FILES;
        FileUtil.createDirectory(FileConstant.FILES);
    }

    public static File getCLASSIFIER() {
        return CLASSIFIER;
    }

    public static void setCLASSIFIER(File CLASSIFIER) {
        log.info(String.format("CLASSIFIER 设置为: %s", CLASSIFIER.getPath()));
        FileConstant.CLASSIFIER = CLASSIFIER;
    }

    public static File getCsvPath() {
        return CSV_PATH;
    }

    public static void setCsvPath(File csvPath) {
        log.info(String.format("CSV_PATH 设置为: %s", csvPath.getPath()));
        CSV_PATH = csvPath;
    }
}
