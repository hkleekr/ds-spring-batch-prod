package com.daesang.springbatch.dwrs.scheduler;

import com.daesang.springbatch.dwrs.scemployee.job.DwrsScEmployeeJobConfiguration;
import com.daesang.springbatch.dwrs.scemployeeaccount.job.DwrsScEmployeeAccountJobConfiguration;
import com.daesang.springbatch.dwrs.scemployeemaster.job.DwrsScEmployeeMasterJobConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * fileName			: DwrsScheduler
 * author			: 최종민차장
 * date				: 2022-12-20
 * descrition       : Dwrs 통합 스케쥴러
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2022-12-20			최종민차장             최초생성
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DwrsScheduler {
    private final JobLauncher jobLauncher;

    private final DwrsScEmployeeJobConfiguration dwrsScEmployeeJobConfiguration;
    private final DwrsScEmployeeAccountJobConfiguration dwrsScEmployeeAccountJobConfiguration;
    private final DwrsScEmployeeMasterJobConfiguration dwrsScEmployeeMasterJobConfiguration;

    /**
     * ScEmployeeAccountJob 0018
     * Schedule Time - 1일 1회  AM04:30
     * @throws Exception
     */
    @Scheduled(cron = "0 30 4 * * *")
    private void runActivateScEmployeeAccountJob() {
        JobExecution execution;

        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        confMap.put("chSize" ,new JobParameter(2d));

        try{
            log.info(">>>>>>>>> start activateScEmployeeAccountJob");
            execution = jobLauncher.run(dwrsScEmployeeAccountJobConfiguration.activateScEmployeeAccountJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread: {}", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * DwrsScEmployeeJob 0053
     * Schedule Time - 1일 1회  AM00:10
     * @throws Exception
     */
    @Scheduled(cron = "0 10 0 * * *")
    private void runActivateScEmployeeJob() {
        JobExecution execution;

        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));

        try{
            log.info(">>>>>>>>> start activateDwrsScEmployeeJob");
            execution = jobLauncher.run(dwrsScEmployeeJobConfiguration.activateScEmployeeJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread: {}", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * deprecated
     * ScEmployeeMasterJob 0091
     * Schedule Time - 1일 1회  AM00:00
     * @throws Exception
     */
//    @Scheduled(fixedDelay = 30000*1)
//    private void runActivateScEmployeeMasterJob() {
//        JobExecution execution;
//
//        Map<String, JobParameter> confMap = new HashMap<>();
//        confMap.put("time", new JobParameter(System.currentTimeMillis()));
//        confMap.put("chSize" ,new JobParameter(2d));
//
//        try{
//            log.info(">>>>>>>>> start activateDwrsScEmployeeMasterJob");
//            execution = jobLauncher.run(dwrsScEmployeeMasterJobConfiguration.activateScEmployeeMasterJob(), new JobParameters(confMap));
//            log.info(">>>>>>>>> Job finished with status : " + execution.getStatus());
//            log.info(">>>>>>>>> Current Thread: {}", Thread.currentThread().getName());
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            e.printStackTrace();
//        }
//    }
}
