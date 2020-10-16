package com.schedule.spring;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  定时任务执行类
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/08/27 17:20
 * @Version 1.0
 */
@Service
public class SysJobRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SysJobRunner.class);

    /*@Autowired
    private ISysJobRepository sysJobRepository;*/

    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    @Override
    public void run(String... args) {
        // 初始加载数据库里状态为正常的定时任务
        //List<SysJobPO> jobList = sysJobRepository.getSysJobListByStatus(SysJobStatus.NORMAL.ordinal());

        List<SysJobVO> jobList = new ArrayList<>();
        SysJobVO sysJobPO = SysJobVO
                .builder()
                .jobId(1)
                .jobStatus(1)
                .beanName("demoTask")
                .methodName("taskWithParams")
                .methodParams("666")
                .cronExpression("0/10 * * * * ?")
                .remark("带参定时任务test")
                .build();

        jobList.add(sysJobPO);

        if (CollectionUtils.isNotEmpty(jobList)) {
            for (SysJobVO job : jobList) {
                SchedulingRunnable task = new SchedulingRunnable(job.getBeanName(), job.getMethodName(), job.getMethodParams());
                cronTaskRegistrar.addCronTask(task, job.getCronExpression());
            }

            logger.info("定时任务已加载完毕...");
        }
    }
}
