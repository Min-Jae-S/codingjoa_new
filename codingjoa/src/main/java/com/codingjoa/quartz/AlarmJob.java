package com.codingjoa.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.codingjoa.websocket.test.WebSocketTestHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlarmJob implements Job {
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("## {}.execute", this.getClass().getSimpleName());
		WebSocketTestHandler handler = (WebSocketTestHandler) context.getMergedJobDataMap().get("handler");
		AlarmDto alarmDto = (AlarmDto) context.getMergedJobDataMap().get("alarmDto");
		handler.sendAlarmNotification(alarmDto);
	}
	
}
