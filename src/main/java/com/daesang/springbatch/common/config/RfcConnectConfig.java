package com.daesang.springbatch.common.config;

import com.daesang.springbatch.common.service.RfcConnectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * fileName			: RfcConnectConfig
 * author			: 최종민차장
 * date				: 2022-11-04
 * descrition       : RfcConectionCall Config
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2022-11-04			최종민차장             최초생성
 */
@Component
@Slf4j
public class RfcConnectConfig implements CommandLineRunner {
    @Autowired
    RfcConnectService rfcConnectService;
    @Override
    public void run(String... args) throws Exception {
        rfcConnectService.rfcConnectedProd();
//        rfcConnectService.rfcConnectedDev();
    }
}
