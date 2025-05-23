package com.codingjoa.obsolete;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.codingjoa.websocket.test.WebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlarmJob implements Job {
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("## {}.execute", this.getClass().getSimpleName());
		WebSocketHandler handler = (WebSocketHandler) context.getMergedJobDataMap().get("handler");
		AlarmDto alarmDto = (AlarmDto) context.getMergedJobDataMap().get("alarmDto");
		try {
			handler.sendAlarm(alarmDto);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
