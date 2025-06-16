package com.codingjoa.batch;

import java.text.NumberFormat;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CountSyncMonitoringListener {
	
	private final NumberFormat numberFormat = NumberFormat.getInstance();
	private final NumberFormat percentFormat = NumberFormat.getPercentInstance();
	private SqlSessionTemplate sqlSessionTemplate;
	private String queryId;
	private String jobName;
	private int totalCount;
	private volatile boolean isCompleted = false;
	private Thread monitoringThread;
	
	public CountSyncMonitoringListener(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
		percentFormat.setMaximumFractionDigits(1);
		percentFormat.setMinimumFractionDigits(1);
	}
	
	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		jobName = stepExecution.getJobExecution().getJobInstance().getJobName();
		totalCount = sqlSessionTemplate.selectOne(queryId);
		isCompleted = false;

		log.info("============ 배치 {} 시작 | 총 {}건 ============", jobName, numberFormat.format(totalCount));
		startMonitoring(stepExecution);
	}
	
	private void startMonitoring(StepExecution stepExecution) {
		monitoringThread = new Thread(() -> {
			while (!isCompleted) {
				try {
					Thread.sleep(3000);
					
					int readCount = stepExecution.getReadCount();
					if (readCount > 0 && totalCount > 0) {
						double ratio = (double) readCount / totalCount;
						log.info("\t > {} 진행 중... {} ( {} / {} )", 
								jobName, percentFormat.format(ratio), numberFormat.format(readCount), numberFormat.format(totalCount));
					}
				} catch (Exception e) {
					break;
				}
			}
		});
		
		monitoringThread.setDaemon(true);
		monitoringThread.start();
	}
	
	@AfterStep
	public ExitStatus afterStep(StepExecution stepExecution) {
		isCompleted = true;
		try {
            monitoringThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
		
		int writeCount = stepExecution.getWriteCount();
		int skipCount = stepExecution.getSkipCount();
		log.info("============ 배치 {} 종료 | 성공: {}, 실패: {} ============", 
			jobName, numberFormat.format(writeCount), numberFormat.format(skipCount));
		
		return stepExecution.getExitStatus();
	}
	
}
