package com.daesang.springbatch.common.service;

import com.daesang.springbatch.common.domain.MailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * fileName         : SmtpService
 * author           : 김수진과장
 * date             : 2023-01-17
 * descrition       : 대량 메일 전송
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-01-17       김수진과장             최초생성
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SmtpService {
    private final JavaMailSender javaMailSender;

    // 프로퍼티에 저장된 username
    @Value("${spring.mail.username}")
    private String userName;

    public void mailSender(MailDto mailDto) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        // 발신자 셋팅 , 보내는 사람의 이메일주소를 한번 더 입력
        if(mailDto.getFromAddr().isBlank() || mailDto.getFromAddr().isEmpty()) messageHelper.setFrom(new InternetAddress(userName));
        else messageHelper.setFrom(new InternetAddress(mailDto.getFromAddr()));

        // 받는사람
        InternetAddress[] toAddress = new InternetAddress[mailDto.getToAddrList().size()];
        for (int i = 0; i <mailDto.getToAddrList().size(); i++) {
            toAddress[i] = new InternetAddress(mailDto.getToAddrList().get(i));
        }

        // 참조
        if (mailDto.getCcAddrList() != null) {
            InternetAddress[] ccAddress = new InternetAddress[mailDto.getCcAddrList().size()];
            for (int i = 0; i <mailDto.getCcAddrList().size(); i++) {
                ccAddress[i] = new InternetAddress(mailDto.getCcAddrList().get(i));
            }
            // 참조
            if (mailDto.getCcAddrList().size() > 0) {
                messageHelper.setCc(ccAddress);
            }
        }

        // 숨은참조
        if (mailDto.getBccAddrList() != null) {
            InternetAddress[] bccAddress = new InternetAddress[mailDto.getBccAddrList().size()];
            for (int i = 0; i <mailDto.getBccAddrList().size(); i++) {
                bccAddress[i] = new InternetAddress(mailDto.getBccAddrList().get(i));
            }
            // 숨은참조
            if (mailDto.getBccAddrList().size() > 0) {
                messageHelper.setBcc(bccAddress);
            }
        }

        // 받는사람
        messageHelper.setTo(toAddress);
        // 제목
        messageHelper.setSubject(mailDto.getSubject());
        // 내용
        messageHelper.setText(mailDto.getContent(), true);
        // 파일첨부
        if(!CollectionUtils.isEmpty(mailDto.getMultipartFile())) {
            for(MultipartFile multipartFile: mailDto.getMultipartFile()) {
                messageHelper.addAttachment(multipartFile.getOriginalFilename(), multipartFile);
            }
        }

        // 메일 발송
        javaMailSender.send(mimeMessage);
    }
}
