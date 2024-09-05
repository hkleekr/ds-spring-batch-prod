package com.daesang.springbatch.rpa.report.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.util.CommonUtils;
import com.daesang.springbatch.common.util.MarkAnyUtil;
import com.daesang.springbatch.rpa.report.domain.RpaReportDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * fileName         : RpaReportJobReader
 * author           : inayoon
 * date             : 2023-01-02
 * description      : 내수 주문 관련 시험성적서 파일 전송
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-01-02       inayoon             최초생성
 */
@Slf4j
@RequiredArgsConstructor
public class RpaReportJobReader implements ItemReader<RpaReportDto> {

    private List<RpaReportDto> rpaReportList;

    private int nextIndex;

    private JobExecution jobExecution;

    private final String RPA_ABSOLUTE_PATH;
    private final MarkAnyUtil markAnyUtil;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public RpaReportDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (rpaReportDataIsNotInitialized()){
            rpaReportList = sendRpaFile();
        }

        RpaReportDto mappingDto = null;

        if (nextIndex < rpaReportList.size()) {
            mappingDto = rpaReportList.get(nextIndex);

            nextIndex++;

        }else {
            nextIndex = 0;
            rpaReportList = null;
        }

        return mappingDto;
    }

    private boolean rpaReportDataIsNotInitialized(){
        return this.rpaReportList == null;
    }

    /**
     * Get RPA Data
     * @param
     * @return
     */
    public List<RpaReportDto> sendRpaFile() {
        String rpaAbsolutePath = RPA_ABSOLUTE_PATH + CommonUtils.getToday() + "/";
        File rpaAbsoluteFile = new File(rpaAbsolutePath);
        File []dirFileList = rpaAbsoluteFile.listFiles();
        List<RpaReportDto> rpaReportDtoList = new ArrayList<RpaReportDto>();

        log.info("=============== {} 디렉토리 파일 목록 ==================", CommonUtils.getToday());
        if(rpaAbsoluteFile.isDirectory()) {
            for(File dirFile : dirFileList) {
                if(dirFile.isFile()) {
                    String dirFileName = dirFile.getName();
                    log.info(dirFileName);
                }
            }
        }else {
            log.info("해당 디렉토리가 존재하지 않습니다.");
            return rpaReportDtoList;
        }
        log.info("==============================================================");
        log.info(">>>>>>>>> RPA Directory - {} 건 조회 완료", dirFileList.length);

        String rpaFilelistPath = RPA_ABSOLUTE_PATH + CommonUtils.getToday() + "/" + "filelist.txt";
        File rpaFileText = new File(rpaFilelistPath);

        if(rpaFileText.exists()) {
            CommonUtils.readFile(rpaFileText);

            String rpaFileContent = null;
            try {
                rpaFileContent = CommonUtils.readFile(rpaFileText);
                String[] splitFileData = rpaFileContent.split("\n");

                log.info("=============== {} filelist.txt 파일 목록 ==============", CommonUtils.getToday());
                for(int i=0; i<splitFileData.length; i++) {
                    String orgFileNameText = splitFileData[i].trim();
                    log.info(orgFileNameText);

                    File rpaFile = new File(rpaAbsolutePath + orgFileNameText);
                    if (!rpaFile.exists()) {
                        log.info("해당 파일({})이 다음 경로에 존재하지 않습니다. - {}", orgFileNameText, rpaAbsolutePath);
                        continue;
                    }

                    String oriFileName = rpaFile.getName();
                    // DRM 해제
                    markAnyUtil.decrypt(rpaAbsolutePath, oriFileName, rpaFile);

                    File decFile = new File(rpaAbsolutePath + oriFileName);
                    byte[] bytes = FileUtils.readFileToByteArray(decFile);
                    String encodingString = Base64.getEncoder().encodeToString(bytes);

                    RpaReportDto rpaReportDto = new RpaReportDto();
                    rpaReportDto.setFile(encodingString);
                    rpaReportDto.setFileName(oriFileName);
                    rpaReportDtoList.add(rpaReportDto);
                }
                log.info("==============================================================");
                log.info(">>>>>>>>> filelist - {} 건 조회 완료", splitFileData.length);

            } catch (IOException e) {
                e.printStackTrace();
                log.info("IOException :::::: {}", e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                log.info("Exception   :::::: {}", e.getMessage());
            }
        } else {
            log.info("=========== {} ============", CommonUtils.getToday());
            log.info("해당 파일이 존재하지 않습니다. - {}", rpaFilelistPath);
        }


        if (rpaReportDtoList.size() > 0) {
            log.info("RPA Report - {} 건", rpaReportDtoList.size());

            InterfaceInfo interfaceInfo = new InterfaceInfo();
            if (rpaReportDtoList.size() % Constant.CHUNK_SIZE == 0) {
                interfaceInfo.setTotalPage(rpaReportDtoList.size()/ Constant.CHUNK_SIZE);
            }else {
                interfaceInfo.setTotalPage(rpaReportDtoList.size()/ Constant.CHUNK_SIZE + 1);
            }
            interfaceInfo.setChunkSize(Constant.CHUNK_SIZE);
            interfaceInfo.setTotalCount(rpaReportDtoList.size());

            jobExecution.getExecutionContext().put("IF", interfaceInfo);

        }else {
            log.info("RPA Report - 업데이트된 내역이 없습니다.");
        }
        return rpaReportDtoList;
    }
}