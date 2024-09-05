package com.daesang.springbatch.common.service;

import com.daesang.springbatch.common.util.FileDirUtils;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * fileName         : FileService
 * author           : 권용성사원
 * date             : 2023-02-06
 * description      : 폴더를 특정 텍스트로 파일 검색 및 대상 파일 압축
 * description      : 압축 파일 byte[] 반환 또는 파일 다운로드 서비스
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-02-06       권용성사원             최초생성
 */
@Slf4j
@Getter
public class ZipService {
    private String downDirPath;
    private String fileFullName;
    private String ext;
    private File[] files;
    private boolean isDownload;

    /**
     * @param downDirPath: 압축파일 다운로드 디렉토리
     * @param files: 압축파일 파일
     * @param fileName: 압축파일 파일명
     * @param ext: 압축파일 확장자
     * @param isDownload: 다운로드 유무
     */
    @Builder
    public ZipService(
             String downDirPath
            , @NotNull File[] files
            , @NotNull String fileName
            , @NotNull String ext
            , @NotNull boolean isDownload
    ) {
        this.downDirPath = downDirPath;
        this.ext = ext;
        this.fileFullName = fileName  + ext;
        this.files = files;
        this.isDownload = isDownload;
    }

    /**
     * 폴더(fileDir)를 검색하여 특정 단어(fileFilterText)가 포함된 파일을 압축하여 byte[] 반환 또는 특정 폴더(downDirPath)에 저장
     * @return
     */
    public byte[] zip() {
        byte[] zip;
        if(isDownload && (downDirPath.isEmpty() || "".equals(downDirPath))) throw new RuntimeException("다운로드 폴더가 지정되지 않았습니다.");
        else zip = zipToByteArray(files, downDirPath, fileFullName, isDownload);
        return zip;
    }

    /**
     * 폴더(fileDir)를 검색하여 특정 단어(fileFilterText)가 포함된 파일을 압축하여 byte[] 반환
     * @param files: 압축파일 파일
     * @param downDirPath: 압축 파일을 저장할 폴더
     * @param fileName: 압축 파일 명칭(확장자 X)
     * @param isDownload: 압축 파일 저장 유무
     * @return byte[]: 압축 파일
     */
    private byte[] zipToByteArray(File[] files,String downDirPath, String fileName, boolean isDownload) {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        ZipOutputStream zos = null;

        if (files == null || files.length <= 0) throw new RuntimeException("해당하는 파일이 없습니다.");

        try {
            bos = new ByteArrayOutputStream();
            zos = new ZipOutputStream(bos);

            for (File fileToZip : files) {
                fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zos.putNextEntry(zipEntry);
                byte[] bytes = new byte[2048];
                int length;
                while ((length = fis.read(bytes)) > 0) {
                    zos.write(bytes, 0, length);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (zos != null) zos.closeEntry();
            } catch (IOException e2) {
                log.error(e2.getMessage());
            }
            try {
                if (fis != null) fis.close();
            } catch (IOException e1) {
                log.error(e1.getMessage());
            }
            try {
                if (zos != null) zos.close();
            } catch (IOException e3) {
                log.error(e3.getMessage());
            }
            try {
                if (bos != null) bos.close();
            } catch (IOException e1) {
                log.error(e1.getMessage());
            }
        }
        if (isDownload) {
            try {
                Path path = Paths.get(FileDirUtils.getDownloadFilePath(downDirPath, fileName));
                Files.write(path, bos.toByteArray());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return bos.toByteArray();
    }

}