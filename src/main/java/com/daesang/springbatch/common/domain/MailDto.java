package com.daesang.springbatch.common.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * fileName         : MailDto
 * author           : 권용성사원
 * date             : 2023-02-14
 * description      : 메일발송 DTO
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-02-14       권용성사원             최초생성
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MailDto {
    // 제목
    private String subject;
    // 내용
    private String content;
    // 다건여부
    private Boolean type;
    // 발송 이메일 주소
    private String fromAddr;
    // 이메일주소 리스트
    private List<String> toAddrList;
    // 참조 리스트
    private List<String> ccAddrList;
    // 숨은참조 리스트
    private List<String> bccAddrList;
    // 첨부파일(다건일수도 있어서 리스트)
    private List<MultipartFile> multipartFile;
}
