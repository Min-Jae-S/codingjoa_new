package com.codingjoa.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.codingjoa.websocket.test.WebSocketTestHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AlarmJob implements Job {
	
	private final WebSocketTestHandler webSocketTestHandler;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("## {}.execute", this.getClass().getSimpleName());
		AlarmDto alarmDto = (AlarmDto) context.getMergedJobDataMap().get("alarmDto");
		webSocketTestHandler.sendNoticiation(alarmDto);
	}
	
}
