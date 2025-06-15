package com.codingjoa.batch;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CountSyncMonitoringListener {
	
	private SqlSessionTemplate sqlSessionTemplate;
	private String queryId;
	private String jobName;
	private int totalCount;
	
	public CountSyncMonitoringListener(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	
	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		jobName = stepExecution.getJobExecution().getJobInstance().getJobName();
		totalCount = sqlSessionTemplate.selectOne(queryId);
		log.info("======== 배치 {} 시작, 총 {}건 ========", jobName, String.format("%,d", totalCount));
		
		startMonitoring(stepExecution);
	}
	
	private void startMonitoring(StepExecution stepExecution) {
		Thread monitoringThread = new Thread(() -> {
			while (stepExecution.getStatus().isRunning()) {
				try {
					int processedCount = stepExecution.getWriteCount();
					if (processedCount > 0) {
						double percentage = (totalCount > 0) ? (double) processedCount/ totalCount * 100 : 0;
						String formattedProcessed = String.format("%,d", processedCount);
						String formattedTotal = String.format("%,d", totalCount);
						String formattedPercentage = String.format("%.1f", percentage);
						log.info("\t - {} 진행중.. {}% ( {} / {} )", jobName, formattedPercentage, formattedProcessed, formattedTotal);
					}
					Thread.sleep(3000);
				} catch (Exception e) {
					log.error("## 모니터링 중 오류: {}", e.getMessage());
				}
			}
		});
		
		monitoringThread.setName(jobName + "Monitoring");
		monitoringThread.setDaemon(true);
		monitoringThread.start();
	}
	
	@AfterStep
	public ExitStatus afterStep(StepExecution stepExecution) {
		String jobName = stepExecution.getJobExecution().getJobInstance().getJobName();
		BatchStatus status = stepExecution.getStatus();
		log.info("======== 배치 {} 종료, {} ========", jobName, status);
		return stepExecution.getExitStatus();
	}
	
}
