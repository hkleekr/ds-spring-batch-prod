package com.daesang.springbatch.common.service;

import com.daesang.springbatch.common.Constant;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.DestinationDataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * fileName			: RfcConnectService
 * author			: 최종민차장
 * date				: 2022-11-04
 * descrition       :
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2022-11-04			최종민차장             최초생성
 */
@Slf4j
@Service
public class RfcConnectService {
    @PostConstruct
    protected  void init(){
        if(System.getProperty("java.library.path").equals("/home/tmax/crm/libs"))
            System.loadLibrary("sapjco3");
    }

    @Value("${file.home}")
    private String fileHome;

    @Value("${rfc.common.user}")
    private String user;
    @Value("${rfc.common.password}")
    private String password;
    @Value("${rfc.common.client-no}")
    private String clientNo;
    @Value("${rfc.common.lang}")
    private String lang;
    @Value("${rfc.common.pool-capa}")
    private String poolCapa;
    @Value("${rfc.common.client-no}")
    private String peakLimit;

    /************************ dev ***********************/
    @Value("${rfc.dev.host}")
    private String devHost;
    @Value("${rfc.dev.system-no}")
    private String systemNo;
    /************************ dev ***********************/

    /************************ prod ***********************/
    @Value("${rfc.prod.host}")
    private String prodHost;

    @Value("${rfc.prod.r3name}")
    private String r3name;

    @Value("${rfc.prod.group}")
    private String group;
    /************************ prod ***********************/

    /**
     * JcoProperty 연결
     * @throws JCoException
     */
    public void rfcConnectedDev() throws JCoException {
        log.debug("rfc connection start");

        Properties jcoProperties = new Properties();
        // DEV
        jcoProperties.setProperty(DestinationDataProvider.JCO_ASHOST, devHost); // 호스트
        jcoProperties.setProperty(DestinationDataProvider.JCO_SYSNR, systemNo); // 시스템 번호
        jcoProperties.setProperty(DestinationDataProvider.JCO_CLIENT, clientNo); // 클라이언트 번호
        jcoProperties.setProperty(DestinationDataProvider.JCO_USER, user); // 계정
        jcoProperties.setProperty(DestinationDataProvider.JCO_PASSWD, password); // 암호
        jcoProperties.setProperty(DestinationDataProvider.JCO_LANG, lang); // 언어
        jcoProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, poolCapa); // 대상에서 열린 상태로 유지되는 최대 유휴 연결
        // 개수입니다. Default = 1
        jcoProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, peakLimit); // 대상에 대해 동시에 만들 수 있는 최대 활성 연결
//      createDestinationDataFile(Constant.ABAP_AS_POOLED, jcoProperties);
        createDestinationDataFile(Constant.ABAP_MS, jcoProperties);

        log.debug("rfc connection end");
    }

    /**
     * JcoProperty 연결
     * @throws JCoException
     */
    public void rfcConnectedProd() {
        log.debug("rfc connection start");

        Properties jcoProperties = new Properties();
        // PROD
        jcoProperties.setProperty(DestinationDataProvider.JCO_MSHOST, prodHost); // 호스트
        jcoProperties.setProperty(DestinationDataProvider.JCO_CLIENT, clientNo); // 클라이언트 번호
        jcoProperties.setProperty(DestinationDataProvider.JCO_USER, user);       // 계정
        jcoProperties.setProperty(DestinationDataProvider.JCO_PASSWD, password); // 암호
        jcoProperties.setProperty(DestinationDataProvider.JCO_LANG, lang);       // 언어
        jcoProperties.setProperty(DestinationDataProvider.JCO_R3NAME, r3name);
        jcoProperties.setProperty(DestinationDataProvider.JCO_GROUP, group);
        jcoProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, poolCapa); // 대상에서 열린 상태로 유지되는 최대 유휴 연결
        // 개수입니다. Default = 1
        jcoProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, peakLimit); // 대상에 대해 동시에 만들 수 있는 최대 활성 연결

        createDestinationDataFile(Constant.ABAP_AS_POOLED, jcoProperties);

        log.debug("rfc connection end");
    }

    // 연결정보 파일 생성
    /**
     * JcoProperty 연결정보 파일 생성
     * @param destinationName
     * @param connectProperties
     */
    private void createDestinationDataFile(String destinationName, Properties connectProperties) {
        File destCfg = new File(destinationName + ".jcoDestination");
        try {
            FileOutputStream fos = new FileOutputStream(destCfg, false);
            connectProperties.store(fos, "for tests only !");
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException("Unable to create the destination files", e);
        }
    }

}
