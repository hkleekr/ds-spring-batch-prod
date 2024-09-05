package com.daesang.springbatch.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Arrays;

/**
 * fileName         : FileDirUtils
 * author           : 권용성사원
 * date             : 2023-01-31
 * description      : 파일 유틸
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-01-31       권용성사원             최초생성
 */
@Slf4j
public class FileDirUtils {
    public static String getToDirPath(String fromDir, String toDirName) {
        File file = new File(fromDir);
        String sep = file.separator;
        if (file.isDirectory() && !file.isFile()) {
            return file.getPath() + sep + toDirName;
        } else {
            return file.getParentFile().getPath() + sep + toDirName;
        }
    }

    public static File getToDirFile(File fromFile, String toDirName) {
        File file = fromFile;
        String sep = file.separator;
        if (file.isDirectory() && !file.isFile()) {
            return new File(file.getPath() + sep + toDirName);
        } else {
            return new File(file.getParentFile().getPath() + sep + toDirName);
        }
    }

    public static String changeFileDirPath(File file, String toDir) {
        String sep = file.separator;
        return toDir + sep + file.getName();
    }

    public static String getDownloadFilePath(String downDir, String fileName) {
        File fileDir = new File(downDir);
        String sep = fileDir.separator;
        return fileDir + sep + fileName;
    }

    public static File[] getFileWithTextInName(File searchDir, String[] texts) {
        if (searchDir.isFile()) searchDir = searchDir.getParentFile();
        File[] files = searchDir.listFiles((dir, name) ->
                Arrays.stream(texts).anyMatch(
                        text -> name.contains(text)
                ));
        return files;
    }

    public static File[] getFileWithTextInName(File searchDir, String[] texts, String[] exceptTexts) {
        if (searchDir.isFile()) searchDir = searchDir.getParentFile();
        File[] files = searchDir.listFiles((dir, name) ->
                Arrays.stream(texts).anyMatch(
                        text -> name.contains(text)
                ));
        files = Arrays.stream(files).filter((file) ->
                Arrays.stream(exceptTexts).anyMatch((text) ->
                        !file.getName().contains(text))
                ).toArray(File[]::new);
        return files;
    }
}