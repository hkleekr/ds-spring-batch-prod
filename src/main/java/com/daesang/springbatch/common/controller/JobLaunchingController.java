package com.daesang.springbatch.common.controller;

        import lombok.RequiredArgsConstructor;
        import org.springframework.batch.core.ExitStatus;
        import org.springframework.batch.core.Job;
        import org.springframework.batch.core.JobParameter;
        import org.springframework.batch.core.JobParameters;
        import org.springframework.batch.core.launch.JobLauncher;
        import org.springframework.context.ApplicationContext;
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.PathVariable;
        import org.springframework.web.bind.annotation.RestController;

        import java.util.HashMap;
        import java.util.Map;

/**
 * fileName			: JobLaunchingController
 * author			: 최종민차장
 * date				: 2022-11-02
 * descrition       : 임의의 Job 실행 test 용
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2022-11-02			최종민차장             최초생성
 */
@RestController
@RequiredArgsConstructor
public class JobLaunchingController {

    private final JobLauncher jobLauncher;

    private final ApplicationContext context;

    /**
     * 서버 재시작 시 일배치 기록 싱글톤 객체 초기화 방지
     * @param 
     * @return
     * @throws Exception
     */
    @GetMapping("/run/dailyBatchReportJob")
    public ExitStatus runJob() throws Exception {
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        Job job = context.getBean("dailyBatchReportJob", Job.class);
        return jobLauncher.run(job, new JobParameters(confMap)).getExitStatus();
    }

}
