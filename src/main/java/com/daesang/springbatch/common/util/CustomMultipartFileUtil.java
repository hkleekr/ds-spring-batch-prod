package com.daesang.springbatch.common.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.file.Files;

/**
 * fileName         : CustomMultipartFileUtil
 * author           : 권용성사원
 * date             : 2023-02-14
 * description      : 이메일 첨부파일 생성, FILE to MultipartFile / byte[] to MultipartFile
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-02-14       권용성사원             최초생성
 */
public class CustomMultipartFileUtil {

    /**
     * byte[] to Multipartfile
     * @param bytes:    변환 byte[]
     * @param fileName: Multipartfile 변환 파일 명
     * @return MultipartFile: 메일 첨부 Multipartfile
     * @throws IOException
     */
    public static MultipartFile convert(byte[] bytes, String fileName) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(bytes);
        FileItem fileItem = new DiskFileItem("file", "application/octet-stream", false, fileName, (int) bytes.length, null);
        OutputStream os = fileItem.getOutputStream();
        IOUtils.copy(inputStream, os);
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        return multipartFile;
    }

    /**
     * file to Multipartfile
     * @param  file:    변환 File
     * @return MultipartFile: 메일 첨부 Multipartfile
     * @throws IOException
     */
    public static MultipartFile convert(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        FileItem fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
        OutputStream os = fileItem.getOutputStream();
        IOUtils.copy(inputStream, os);
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        return multipartFile;
    }
}