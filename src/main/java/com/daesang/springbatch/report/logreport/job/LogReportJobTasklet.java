package com.daesang.springbatch.report.logreport.job;

import com.daesang.springbatch.common.domain.LogReportProperties;
import com.daesang.springbatch.common.util.CommonUtils;
import com.daesang.springbatch.common.util.FileDirUtils;
import com.daesang.springbatch.report.logreport.service.LogReportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * fileName         : LogReportJobTasklet
 * author           : 권용성사원
 * date             : 2023-02-27
 * description      : 업무구분 - 상세업무
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-02-27       권용성사원             최초생성
 */
@Slf4j
public class LogReportJobTasklet implements Tasklet, StepExecutionListener {
    @Value(value = "${logging.file.root-path}")
    private String FILE_ROOT_PATH;
    private final String REPORT_FILE_DIR_PATH;
    private final String[] SERVER_NAME;
    private final String[] FILTER_TEXT_ARRAY;
    private final LogReportService logReportService;
    private boolean isError;
    private String lastMonth;
    private File[] logFileArray;
    private List<String> fileNames;

    /**
     * 생성자
     *
     * @param logReportService
     * @param logReportProperties: application-[active.profile].report / properties
     */
    public LogReportJobTasklet(
            LogReportService logReportService
            , LogReportProperties logReportProperties
    ) {
        this.logReportService = logReportService;
        this.REPORT_FILE_DIR_PATH = logReportProperties.getReportFileDirPath();
        this.SERVER_NAME = logReportProperties.getServerName();
        this.FILTER_TEXT_ARRAY = logReportProperties.getFilterTextArray();
    }

    /**
     * 로그 폴더 검색 및 리포트 로그 복사 이동 및 기존 리포트 로그 삭제
     *
     * @param stepExecution instance of {@link StepExecution}.
     */
    @Override
    public void beforeStep(StepExecution stepExecution) {
        logFileArray = new File[0];
        fileNames = new ArrayList<>();
        Map<String, JobParameter> parameters =  stepExecution.getJobParameters().getParameters();
        lastMonth = String.valueOf(parameters.get("month"));
//        lastMonth = CommonUtils.getLastMonth(1, "yyyyMM");

        //TODO: Nas 로그 루트 폴더에서 로그 폴더 검색
        File[] dirs = new File(FILE_ROOT_PATH).listFiles((dir, name) ->
                Arrays.stream(SERVER_NAME).anyMatch(
                        text -> dir.isDirectory() && name.contains(text)
                ));

        //TODO: 발송 대살 폴더 초기화(지난달 로그 삭제)
        try {
            File reportFileDirPath = new File(REPORT_FILE_DIR_PATH);
            if (!reportFileDirPath.exists()) reportFileDirPath.mkdirs();
            FileUtils.cleanDirectory(reportFileDirPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (File dir : dirs) {
            //TODO: 해야할 것 -> 로그 파일이 이동하는 시점이 다 다름, 그렇기 때문에 두 폴더를 스캔하여 발송 로그를 정확히 찾아야함
            File[] tmp = new File[0];
            // 월 변경후 월변경 Appender 적용 폴더 검색
            if (FileDirUtils.getToDirFile(dir, lastMonth).exists())
                tmp = FileDirUtils.getFileWithTextInName(FileDirUtils.getToDirFile(dir, lastMonth), FILTER_TEXT_ARRAY, new String[]{"error"});
            // 월 변경후 월변경 Appender 미적용 폴더 검색
            File[] finalTmp = tmp;
            String[] newFilterTextArray = Arrays.stream(FILTER_TEXT_ARRAY)
                    .filter((text) ->
                            Arrays.stream(finalTmp).noneMatch((file) ->
                                    file.getName().contains(text)
                            )
                    ).toArray(String[]::new);
            if (FILTER_TEXT_ARRAY.length > newFilterTextArray.length && newFilterTextArray.length != 0){
                File[] tmp2 = FileDirUtils.getFileWithTextInName(dir, newFilterTextArray, new String[]{"error"});
                File[] tmp3 = new File[tmp.length + tmp2.length];
                System.arraycopy(tmp, 0, tmp3, 0, tmp.length);
                System.arraycopy(tmp2, 0, tmp3, tmp.length, tmp2.length);
                tmp = tmp3;
            }

            //TODO: 기존 파일 Array 와 검색된 파일 Array Merge
            File[] tmp2 = new File[logFileArray.length + tmp.length];
            System.arraycopy(logFileArray, 0, tmp2, 0, logFileArray.length);
            System.arraycopy(tmp, 0, tmp2, logFileArray.length, tmp.length);
            logFileArray = tmp2;
        }

        if (logFileArray.length == 0) {
            isError = true;
            throw new RuntimeException("로그 파일이 존재하지 않습니다.");
        }

        //TODO: 기존 로그 파일 리스트를 발송 대상 폴더로 이동
        for (File file : logFileArray) {
            String copyDirPath = FileDirUtils.getToDirPath(REPORT_FILE_DIR_PATH, lastMonth);
            String copyFilePath = FileDirUtils.changeFileDirPath(file, copyDirPath);

            String fileName = file.getName();
            String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            fileName = fileName.substring(0, fileName.lastIndexOf("."));

            if (!fileName.endsWith(lastMonth)) {
                fileName = fileName + "_" + lastMonth + "." + fileExt;
                copyFilePath = copyDirPath + file.separator + fileName;
            }

            log.debug("BEFORE LOG FILE: {}", file.getPath());
            log.info("LOG FILE: {}", copyFilePath);
            try {
                File toCopy = new File(copyFilePath);
                fileNames.add(toCopy.getName());
                FileUtils.copyFile(new File(file.getPath()), toCopy);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 검색된 파일들을 메일발송
     *
     * @param contribution mutable state to be passed back to update the current
     * @param chunkContext attributes shared between invocations but not between
     * @return
     * @throws Exception
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            if (!isError) logReportService.sendReportMail(logFileArray, fileNames, lastMonth);
        } catch (Exception e) {
            isError = true;
        }
        return RepeatStatus.FINISHED;
    }

    /**
     * Tasklet ExitStatus 결과 설정
     *
     * @param stepExecution {@link StepExecution} instance.
     * @return
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (!isError) return ExitStatus.COMPLETED;
        else return ExitStatus.FAILED;
    }
}
