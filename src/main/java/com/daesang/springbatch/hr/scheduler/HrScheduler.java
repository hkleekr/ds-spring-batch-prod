package com.daesang.springbatch.hr.scheduler;

import com.daesang.springbatch.hr.ConcurrentPosition.job.ConcurrentPositionJobConfiguration;
import com.daesang.springbatch.hr.department.job.DepartmentJobConfiguration;
import com.daesang.springbatch.hr.employee.job.EmployeeJobConfiguration;
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
 * fileName			: HrScheduler
 * author			: 최종민차장
 * date				: 2022-12-20
 * descrition       : Hr 통합 스케쥴러
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2022-12-20			최종민차장             최초생성
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class HrScheduler {

    private final JobLauncher jobLauncher;

    private final DepartmentJobConfiguration departmentJobConfiguration;
    private final EmployeeJobConfiguration employeeJobConfiguration;
    private final ConcurrentPositionJobConfiguration concurrentPositionJobConfiguration;

    /**
     * 통합인사 부서정보 배치 스케쥴러
     * Schedule Time - AM03:30
     */
    @Scheduled(cron = "0 30 3 * * *")
    private void runDepartmentJob() {
        JobExecution execution;

        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));

        try {
            log.info(">>>>>>>>> start DepartmentJob");
            execution = jobLauncher.run(departmentJobConfiguration.departmentJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread: {}", Thread.currentThread().getName());

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 통합인사 임직원 배치 스케쥴러
     * Schedule Time - AM04:00
     */
    @Scheduled(cron = "0 0 4 * * *")
    private void runEmployeeJob() {
        JobExecution execution;

        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));

        try {
            log.info(">>>>>>>>> start employeeJob");
            execution = jobLauncher.run(employeeJobConfiguration.employeeJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread: {}", Thread.currentThread().getName());

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 통합인사 임직원 겸직 배치 스케쥴러
     * Schedule Time - AM04:15
     */
    @Scheduled(cron = "0 15 4 * * *")
    private void runConcurrentPositionJob() {
        JobExecution execution;

        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));

        try {
            log.info(">>>>>>>>> start concurrentPositionJob");
            execution = jobLauncher.run(concurrentPositionJobConfiguration.ConcurrentPositionJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread: {}", Thread.currentThread().getName());

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
