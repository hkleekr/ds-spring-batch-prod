package com.daesang.springbatch.report.logreport.service;

import com.daesang.springbatch.common.domain.LogReportProperties;
import com.daesang.springbatch.common.domain.MailDto;
import com.daesang.springbatch.common.service.SmtpService;
import com.daesang.springbatch.common.service.ZipService;
import com.daesang.springbatch.common.util.CommonUtils;
import com.daesang.springbatch.common.util.CustomMultipartFileUtil;
import com.daesang.springbatch.common.util.FileDirUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * fileName         : LogReportService
 * author           : 권용성사원
 * date             : 2023-02-13
 * description      : 업무구분 - 상세업무
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-02-13       권용성사원             최초생성
 */
@Slf4j
@Service
public class LogReportService {
    private String FROM_ADDRESS;
    private List<String> TO_ADDRESSES;
    private List<String> TO_CC;
    private List<String> TO_BCC;
    private final String REPORT_FILE_EXT = ".zip";
    private String REPORT_DOWN_DIR_PATH;
    private Map<String, String> SERVICE_DETAIL;
    private String[] SERVER_NAME;
    private final SmtpService smtpService;
    private String TAB = "<span style=\"white-space:pre;\">&nbsp; &nbsp; &nbsp; &nbsp;</span>";

    /**
     * //     * @param fromAddress: application-[active.profile].report.mail.from-address / 메일 발신자
     * //     * @param toAddresses: application-[active.profile].report.mail.to-address / 메일 수신자
     * //     * @param toCc: application-[active.profile].report.mail.to-cc / 메일 참조
     * //     * @param toBcc: application-[active.profile].report.mail.to-bcc / 메일 숨은 참조
     * //     * @param reportDownDirPath: application-[active.profile].report.log.report-down-dir-path / 로그 메일 다운로드
     *
     * @param smtpService: GW SMTP MailService
     */
    public LogReportService(
            SmtpService smtpService
            , LogReportProperties logReportProperties
    ) {
        this.smtpService = smtpService;
        this.FROM_ADDRESS = logReportProperties.getFromAddress();
        this.TO_ADDRESSES = logReportProperties.getToAddress();
        this.TO_CC = logReportProperties.getToCc();
        this.TO_BCC = logReportProperties.getToBcc();
        this.SERVICE_DETAIL = logReportProperties.getServiceDetail();
        this.REPORT_DOWN_DIR_PATH = logReportProperties.getReportDownDirPath();
        this.SERVER_NAME = logReportProperties.getServerName();
    }

    /**
     * 스케쥴 로그 보고 메일 발송
     *
     * @return
     * @throws IOException
     */
    public void sendReportMail(File[] files, List<String> fileNames, String month) throws IOException, MessagingException {
        try {
            SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMM");
            Date dt = dtFormat.parse(month);
            LocalDate date = dt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String mailDate = date.format(DateTimeFormatter.ofPattern("yyyy년 MM월"));

            // 메일 제목
            String subject = "";
//            subject += "[" + CommonUtils.getLastMonth(1, "yyyy년 MM월") + "]";
            subject += "[" + mailDate + "]";
            subject += " 세일즈포스 내부 CRM 연동서버 로그 내역";
            // 첨부 파일명
            String fileName = "";
//            fileName += "[" + CommonUtils.getLastMonth(1, "yyyy년 MM월") + "]";
            fileName += "[" + mailDate + "]";
            fileName += "세일즈포스_내부_CRM_연동서버_로그_내역" + REPORT_FILE_EXT;
            // 메일 본문
            String content = "";
            content += "<b>";
//            content += "[" + CommonUtils.getLastMonth(1, "yyyy년 MM월") + "]";
            content += "[" + mailDate + "]";
            content += "세일즈포스 내부 CRM 연동서버 로그내역 상세";
            content += "</b>";
            content += "<br/>";
            content += "<br/>";
            content += "1. 첨부파일:";
            content += "<br/>";
            content += TAB + "<b>" + fileName + "</b>";
            content += "<br/>";
            content += "2. 첨부파일 상세: <br/>";
            content += createContentLogDetail(fileNames);
            content += "<br/>";
            content += "<br/>";
            content += "<b>발송일: " + CommonUtils.getToday("yyyy년 MM월 dd일") + "</b>";

            ZipService zipService = ZipService.builder()
                    .downDirPath(FileDirUtils.getToDirPath(REPORT_DOWN_DIR_PATH, CommonUtils.getLastMonth(1, "yyyyMM")))
                    .files(files)
                    .fileName(CommonUtils.getLastMonth(1, "yyyyMM"))
                    .ext(REPORT_FILE_EXT)
                    .isDownload(true)
                    .build();

            MailDto mailDto = new MailDto();
            mailDto.setType(false);
            mailDto.setSubject(subject);
            mailDto.setContent(content);
            mailDto.setFromAddr(FROM_ADDRESS);
            mailDto.setToAddrList(TO_ADDRESSES);
            mailDto.setCcAddrList(TO_CC);
            mailDto.setBccAddrList(TO_BCC);
            // 로그파일 생성
            MultipartFile attachFile = CustomMultipartFileUtil.convert(zipService.zip(), fileName);
            // 로그파일 메일 첨부
            List<MultipartFile> multipartFileUtilsList = new ArrayList<>();
            multipartFileUtilsList.add(attachFile);
            mailDto.setMultipartFile(multipartFileUtilsList);

            // 메일 발송
            if(!TO_ADDRESSES.isEmpty())smtpService.mailSender(mailDto);
            else throw new RuntimeException("메일 수신자가 설정되지 않았습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String createContentLogDetail(List<String> fileNames) {
        String content = "";
        String dev = TAB + "SERVER: dev(192.168.9.55):<br/>";
        String web1 = TAB + "SERVER: WEB1(192.168.60.95):<br/>";
        String web2 = TAB + "SERVER: WEB2(192.168.60.96):<br/>";
        String was1 = TAB + "SERVER: WAS1(192.168.60.97):<br/>";
        String was2 = TAB + "SERVER: WAS2(192.168.60.98):<br/>";
        Map<String, String> serverMap = new HashMap<>();

        for (String fileName : fileNames) {
            if (fileName.contains("dev")) {
                for (String key : SERVICE_DETAIL.keySet()) {
                    if (fileName.contains(key))
                        dev += TAB + TAB + fileName + " / " + SERVICE_DETAIL.get(key) + "<br/>";
                }
            }
            if (fileName.contains("web1")) {
                for (String key : SERVICE_DETAIL.keySet()) {
                    if (fileName.contains(key))
                        web1 += TAB + TAB + fileName + " / " + SERVICE_DETAIL.get(key) + "<br/>";
                }
            }
            if (fileName.contains("web2")) {
                for (String key : SERVICE_DETAIL.keySet()) {
                    if (fileName.contains(key))
                        web2 += TAB + TAB + fileName + " / " + SERVICE_DETAIL.get(key) + "<br/>";
                }
            }
            if (fileName.contains("was1")) {
                for (String key : SERVICE_DETAIL.keySet()) {
                    if (fileName.contains(key))
                        was1 += TAB + TAB + fileName + " / " + SERVICE_DETAIL.get(key) + "<br/>";
                }
            }
            if (fileName.contains("was2")) {
                for (String key : SERVICE_DETAIL.keySet()) {
                    if (fileName.contains(key))
                        was2 += TAB + TAB + fileName + " / " + SERVICE_DETAIL.get(key) + "<br/>";
                }
            }
        }
        serverMap.put("dev", dev);
        serverMap.put("web1", web1);
        serverMap.put("web2", web2);
        serverMap.put("was1", was1);
        serverMap.put("was2", was2);

        for(String server : SERVER_NAME){
            content += serverMap.get(server);
        }

        return content;
    }
}
