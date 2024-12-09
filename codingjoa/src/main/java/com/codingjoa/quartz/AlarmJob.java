package com.codingjoa.quartz;

import javax.annotation.PostConstruct;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.codingjoa.websocket.test.WebSocketTestHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@RequiredArgsConstructor
public class AlarmJob implements Job {
	
	//private final WebSocketTestHandler webSocketTestHandler;

	@Autowired
	private WebSocketTestHandler webSocketTestHandler;
	
	@PostConstruct
	public void init() {
		log.info("## webSocketTestHandler = {}", webSocketTestHandler);
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("## {}.execute", this.getClass().getSimpleName());
		AlarmDto alarmDto = (AlarmDto) context.getMergedJobDataMap().get("alarmDto");
		webSocketTestHandler.sendNoticiation(alarmDto);
	}
	
}
