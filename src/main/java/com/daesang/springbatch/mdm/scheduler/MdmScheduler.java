package com.daesang.springbatch.mdm.scheduler;

import com.daesang.springbatch.common.util.CommonUtils;
import com.daesang.springbatch.mdm.accountmaster.job.MdmAccountMasterJobConfiguration;
import com.daesang.springbatch.mdm.accountpartner.job.MdmAccountPartnerJobConfiguration;
import com.daesang.springbatch.mdm.meals.job.MdmMealsJobConfiguration;
import com.daesang.springbatch.mdm.productSalesInfo.job.MdmProductSalesInfoJobConfiguration;
import com.daesang.springbatch.mdm.productmaster.job.MdmProductMasterJobConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * fileName			: MdmScheduler
 * author			: 최종민차장
 * date				: 2022-12-20
 * descrition       : Mdm 통합 스케쥴러
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2022-12-20			최종민차장             최초생성
 * 2022-12-20			권용성사원             자재 판매조직 I/F 추가
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MdmScheduler {
    private final JobLauncher jobLauncher;
    private final MdmAccountMasterJobConfiguration mdmAccountMasterJobConfiguration;
    private final MdmAccountPartnerJobConfiguration mdmAccountPartnerJobConfiguration;
    private final MdmMealsJobConfiguration mdmMealsJobConfiguration;
    private final MdmProductMasterJobConfiguration mdmProductMasterJobConfiguration;
    private final MdmProductSalesInfoJobConfiguration mdmProductSalesInfoJobConfiguration;

    /**
     * 고객정보 - Account 마스터 정보
     * Schedule Time - AM03:00
     * @throws Exception
     */
    @Scheduled(cron = "0 0 3 * * *")
    private void runActivateMdmAccountMasterJob() throws Exception {
        JobExecution execution;

        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        confMap.put("fromDate", new JobParameter(CommonUtils.getLastDay(1,"yyyy-MM-dd")));
        confMap.put("toDate", new JobParameter(CommonUtils.getToday("yyyy-MM-dd")));

        try {
            log.info(">>>>>>>>> start mdmAccountMasterJob");
            execution = jobLauncher.run(mdmAccountMasterJobConfiguration.mdmAccountMasterJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread: {}", Thread.currentThread().getName());

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 고객정보 - Account 파트너 기능 배치
     * Schedule Time - AM04:30
     */
    @Scheduled(cron = "0 30 4 * * *")
    private void runMdmAccountPartnerJob() {
        JobExecution execution;

        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        confMap.put("chSize" ,new JobParameter(2d));
        confMap.put("fromDate", new JobParameter(CommonUtils.getLastDay(1,"yyyy-MM-dd")));
        confMap.put("toDate", new JobParameter(CommonUtils.getToday("yyyy-MM-dd")));

        try {
            log.info(">>>>>>>>> start mdmAccountPartnerJob");
            execution = jobLauncher.run(mdmAccountPartnerJobConfiguration.mdmAccountPartnerJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread: {}", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 고객정보 - 급식거래처 조회
     * Schedule Time - AM04:15
     */
    @Scheduled(cron="0 15 4 * * *")
    private void runMdmMealsJob() {
        JobExecution execution;

        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        confMap.put("chSize" ,new JobParameter(2d));
        confMap.put("fromDate", new JobParameter(CommonUtils.getLastDay(1,"yyyy-MM-dd")));
        confMap.put("toDate", new JobParameter(CommonUtils.getToday("yyyy-MM-dd")));

        try {
            log.info(">>>>>>>>> start mdmMealsJob");
            execution = jobLauncher.run(mdmMealsJobConfiguration.mdmMealsJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread: {}", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 제품정보 - 제품 마스터 정보
     * Schedule Time - AM03:50
     * @throws Exception
     */
    @Scheduled(cron = "0 50 3 * * *")
    private void runMdmProductMasterJob() throws Exception {
        JobExecution execution;

        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        confMap.put("fromDate", new JobParameter(CommonUtils.getLastDay(1,"yyyy-MM-dd")));
        confMap.put("toDate", new JobParameter(CommonUtils.getToday("yyyy-MM-dd")));

        try {
            log.info(">>>>>>>>> start mdmProductMasterJob");
            execution = jobLauncher.run(mdmProductMasterJobConfiguration.mdmProductMasterJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread: {}", Thread.currentThread().getName());

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 제품정보 - 자재 판매조직기준 정보
     * Schedule Time - AM03:55
     * @throws Exception
     */
    @Scheduled(cron = "0 55 3 * * *")
    private void runMdmProductSalesInfoJob() throws Exception {
        JobExecution execution;

        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        confMap.put("fromDate", new JobParameter(CommonUtils.getLastDay(1,"yyyy-MM-dd")));
        confMap.put("toDate", new JobParameter(CommonUtils.getToday("yyyy-MM-dd")));

        try {
            log.info(">>>>>>>>> start runMdmProductSalesInfoJob");
            execution = jobLauncher.run(mdmProductSalesInfoJobConfiguration.mdmProductSalesInfoJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread: {}", Thread.currentThread().getName());

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

}
