package com.daesang.springbatch.sap.scheduler;

import com.daesang.springbatch.common.util.CommonUtils;
import com.daesang.springbatch.sap.bondoverdue.job.BondOverdueJobConfiguration;
import com.daesang.springbatch.sap.domesticdelivery.job.SapDomesticDeliveryJobConfiguration;
import com.daesang.springbatch.sap.domesticorder.job.SapDomesticOrderJobConfiguration;
import com.daesang.springbatch.sap.domestictransport.job.SapDomesticTransportJobConfiguration;
import com.daesang.springbatch.sap.exportdelivery.job.SapExportDeliveryJobConfiguration;
import com.daesang.springbatch.sap.exportloan.job.SapExportLoanJobConfiguration;
import com.daesang.springbatch.sap.exportorder.job.SapExportOrderJobConfiguration;
import com.daesang.springbatch.sap.materialbondoverdue.job.MaterialBondOverdueJobConfiguration;
import com.daesang.springbatch.sap.sample.job.SampleJobConfiguration;
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
 * fileName			: SapScheduler
 * author			: 최종민차장
 * date				: 2022-12-20
 * descrition       :
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2022-12-20			최종민차장             최초생성
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class SapScheduler {
    private final JobLauncher jobLauncher;
    private final BondOverdueJobConfiguration bondOverdueJobConfiguration;
    private final MaterialBondOverdueJobConfiguration materialBondOverdueJobConfiguration;
    private final SapExportLoanJobConfiguration sapExportLoanJobConfiguration;
    private final SapDomesticOrderJobConfiguration sapDomesticOrderJobConfiguration;
    private final SapDomesticDeliveryJobConfiguration sapDomesticDeliveryJobConfiguration;
    private final SapDomesticTransportJobConfiguration sapDomesticTransportJobConfiguration;
    private final SapExportOrderJobConfiguration sapExportOrderJobConfiguration;
    private final SapExportDeliveryJobConfiguration sapExportDeliveryJobConfiguration;
    private final SampleJobConfiguration sampleJobConfiguration;

    /************************************************************
     *****                   SAP SCHEDULER                  *****
     ************************************************************/

    /**
     * 채권 연체 현황 스케쥴러
     * Schedule Time - 1일 1회  AM06:00
     * @throws Exception
     */
    @Scheduled(cron = "0 0 6 * * *")
    private void runBondOverdueJob() {
        JobExecution execution;
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));

        try {
            log.info(">>>>>>>>> start bondOverdueJob");
            execution = jobLauncher.run(bondOverdueJobConfiguration.bondOverdueJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread: {}", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 수출 Account 여신 스케쥴러
     * Schedule Time - 1일 1회  AM05:00
     * @throws Exception
     */
    @Scheduled(cron = "0 0 5 * * *")
    private void runSapExportLoanJob() {
        JobExecution execution;
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));

        try {
            log.info(">>>>>>>>> start sapExportLoanJobParam");
            execution = jobLauncher.run(sapExportLoanJobConfiguration.sapExportLoanJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread: {}", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 주문관리 - 내수 주문정보 스케줄러1(2일치)
     * Schedule Time - AM06:30 ~ PM06:00 매 30분
     * Schedule Time - PM11:40
     * @throws Exception
     */
    @Scheduled(cron = "0 30 6 * * *")
    @Scheduled(cron = "0 0/30 7-18 * * *")
    @Scheduled(cron = "0 40 23 * * *")
    private void runSapDomesticOrderJob1() throws Exception {
        JobExecution execution;
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        confMap.put("fromDate", new JobParameter(CommonUtils.getLastDay(1,"yyyyMMdd")));
        confMap.put("toDate", new JobParameter(CommonUtils.getToday("yyyyMMdd")));
        confMap.put("ifTerm", new JobParameter("time"));

        try {
            log.info(">>>>>>>>> start activateSapDomesticOrderJob1");
            execution = jobLauncher.run(sapDomesticOrderJobConfiguration.sapDomesticOrderJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status1 : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread1: {}", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * 주문관리 - 내수 주문정보 스케줄러2(3개월치)
     * Schedule Time - AM5:00
     * @throws Exception
     */
    @Scheduled(cron = "0 0 5 * * *")
    private void runSapDomesticOrderJob2() throws Exception {
        JobExecution execution;
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        confMap.put("fromDate", new JobParameter(CommonUtils.getLastMonthFirstDay(2,"yyyyMMdd")));
        confMap.put("toDate", new JobParameter(CommonUtils.getToday("yyyyMMdd")));
        confMap.put("ifTerm", new JobParameter("day"));

        try {
            log.info(">>>>>>>>> start activateSapDomesticOrderJob2");
            execution = jobLauncher.run(sapDomesticOrderJobConfiguration.sapDomesticOrderJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status2 : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread2: {}", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 배송관리 - 내수 배송정보 스케줄러1(2일치)
     * Schedule Time - AM06:35 ~ PM06:05 매 30분
     * Schedule Time - PM11:50
     * @throws Exception
     */
    @Scheduled(cron = "0 35 6 * * *")
    @Scheduled(cron = "0 5/30 7-18 * * *")
    @Scheduled(cron = "0 50 23 * * *")
    private void runSapDomesticDeliveryJob1() throws Exception {
        JobExecution execution;
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        confMap.put("fromDate", new JobParameter(CommonUtils.getLastDay(1,"yyyyMMdd")));
        confMap.put("toDate", new JobParameter(CommonUtils.getToday("yyyyMMdd")));
        confMap.put("ifTerm", new JobParameter("time"));

        try {
            log.info(">>>>>>>>> start activateSapDomesticDeliveryJob1");
            execution = jobLauncher.run(sapDomesticDeliveryJobConfiguration.sapDomesticDeliveryJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status1 : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread1: {}", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * 배송관리 - 내수 배송정보 스케줄러2(3개월치)
     * Schedule Time - AM5:30
     * @throws Exception
     */
    @Scheduled(cron = "0 30 5 * * *")
    private void runSapDomesticDeliveryJob2() throws Exception {
        JobExecution execution;
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        confMap.put("fromDate", new JobParameter(CommonUtils.getLastMonthFirstDay(2,"yyyyMMdd")));
        confMap.put("toDate", new JobParameter(CommonUtils.getToday("yyyyMMdd")));
        confMap.put("ifTerm", new JobParameter("day"));

        try {
            log.info(">>>>>>>>> start activateSapDomesticDeliveryJob2");
            execution = jobLauncher.run(sapDomesticDeliveryJobConfiguration.sapDomesticDeliveryJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status2 : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread2: {}", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 배송관리 - 내수 납품정보(LE) 스케줄러1(2일치)
     * Schedule Time - AM06:40 ~ PM06:10 매 30분
     * @throws Exception
     */
    @Scheduled(cron = "0 40 6 * * *")
    @Scheduled(cron = "0 10/30 7-18 * * *")
    private void runSapDomesticTransportJob1() throws Exception {
        JobExecution execution;
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        confMap.put("fromDate", new JobParameter(CommonUtils.getLastDay(1,"yyyyMMdd")));
        confMap.put("toDate", new JobParameter(CommonUtils.getToday("yyyyMMdd")));

        try {
            log.info(">>>>>>>>> start activateSapDomesticTransportJob");
            execution = jobLauncher.run(sapDomesticTransportJobConfiguration.sapDomesticTransportJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread: {}", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * 배송관리 - 내수 납품정보(LE) 스케줄러2(3개월치)
     * Schedule Time - AM06:00
     * @throws Exception
     */
    @Scheduled(cron = "0 0 6 * * *")
    private void runSapDomesticTransportJob2() throws Exception {
        JobExecution execution;
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        confMap.put("fromDate", new JobParameter(CommonUtils.getLastMonthFirstDay(2,"yyyyMMdd")));
        confMap.put("toDate", new JobParameter(CommonUtils.getToday("yyyyMMdd")));

        try {
            log.info(">>>>>>>>> start activateSapDomesticTransportJob2");
            execution = jobLauncher.run(sapDomesticTransportJobConfiguration.sapDomesticTransportJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status2 : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread2: {}", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 주문관리 - 수출 주문정보 스케줄러1(2일치)
     * Schedule Time - AM06:45 ~ PM06:15 매 30분
     * @throws Exception
     */
    @Scheduled(cron = "0 45 6 * * *")
    @Scheduled(cron = "0 15/30 7-18 * * *")
    private void runSapExportOrderJob1() throws Exception {
        JobExecution execution;
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        confMap.put("fromDate", new JobParameter(CommonUtils.getLastDay(1,"yyyyMMdd")));
        confMap.put("toDate", new JobParameter(CommonUtils.getToday("yyyyMMdd")));
        confMap.put("ifTerm", new JobParameter("time"));

        try {
            log.info(">>>>>>>>> start activateSapExportOrderJob");
            execution = jobLauncher.run(sapExportOrderJobConfiguration.sapExportOrderJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread: {}", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 주문관리 - 수출 주문정보 스케줄러2(3개월치)
     * Schedule Time - AM4:00
     * @throws Exception
     */
    @Scheduled(cron = "0 0 4 * * *")
    private void runSapExportOrderJob2() throws Exception {
        JobExecution execution;
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        confMap.put("fromDate", new JobParameter(CommonUtils.getLastMonthFirstDay(2,"yyyyMMdd")));
        confMap.put("toDate", new JobParameter(CommonUtils.getToday("yyyyMMdd")));
        confMap.put("ifTerm", new JobParameter("day"));

        try {
            log.info(">>>>>>>>> start activateSapExportOrderJob2");
            execution = jobLauncher.run(sapExportOrderJobConfiguration.sapExportOrderJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status2 : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread2: {}", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 배송관리 - 수출 배송정보 스케줄러1(2일치)
     * Schedule Time - AM06:50 ~ PM06:20 매 30분
     * @throws Exception
     */
    @Scheduled(cron = "0 50 6 * * *")
    @Scheduled(cron = "0 20/30 7-18 * * *")
    private void runSapExportDeliveryJob1() throws Exception {
        JobExecution execution;
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        confMap.put("fromDate", new JobParameter(CommonUtils.getLastDay(1,"yyyyMMdd")));
        confMap.put("toDate", new JobParameter(CommonUtils.getToday("yyyyMMdd")));
        confMap.put("ifTerm", new JobParameter("time"));

        try {
            log.info(">>>>>>>>> start activateSapExportDeliveryJob");
            execution = jobLauncher.run(sapExportDeliveryJobConfiguration.sapExportDeliveryJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread: {}", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 배송관리 - 수출 배송정보 스케줄러2(3개월치)
     * Schedule Time - AM4:30
     * @throws Exception
     */
    @Scheduled(cron = "0 30 4 * * *")
    private void runSapExportDeliveryJob2() throws Exception {
        JobExecution execution;
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        confMap.put("fromDate", new JobParameter(CommonUtils.getLastMonthFirstDay(2, "yyyyMMdd")));
        confMap.put("toDate", new JobParameter(CommonUtils.getToday("yyyyMMdd")));
        confMap.put("ifTerm", new JobParameter("day"));

        try {
            log.info(">>>>>>>>> start activateSapExportDeliveryJob2");
            execution = jobLauncher.run(sapExportDeliveryJobConfiguration.sapExportDeliveryJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status2 : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread2: {}", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 무상 주문 정보 스케쥴러(당월)
     * Schedule Time - AM04:30
     * @throws Exception
     */
    @Scheduled(cron = "0 30 4 * * *")
    private void runSampleJob1() {
        JobExecution execution;
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        confMap.put("fromDate", new JobParameter(CommonUtils.getCurrentMonthFirstDay("yyyyMMdd")));
        confMap.put("toDate", new JobParameter(CommonUtils.getToday("yyyyMMdd")));

        try {
            log.info(">>>>>>>>> start sampleJob");
            execution = jobLauncher.run(sampleJobConfiguration.sampleJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread: {}", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * (소재) Account 내수 연체 정보 조회 스케쥴러 (당월)
     * Schedule Time - AM02:00
     */
    @Scheduled(cron = "0 0 2 * * *")
    private void runMaterialBondOverdueJob() {
        JobExecution execution;
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
//        confMap.put("month", new JobParameter(CommonUtils.getCurrentMonth("yyyyMM")));
        confMap.put("month", new JobParameter(CommonUtils.getLastMonth(1,"yyyyMM")));

        try {
            log.info(">>>>>>>>> start materialBondOverdueJob");
            execution = jobLauncher.run(materialBondOverdueJobConfiguration.materialBondOverdueJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread: {}", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }


}