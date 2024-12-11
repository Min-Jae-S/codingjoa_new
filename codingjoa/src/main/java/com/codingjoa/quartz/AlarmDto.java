package com.codingjoa.quartz;

import java.time.LocalTime;

import lombok.Data;

@Data
public class AlarmDto {
	
	private String message;
	private LocalTime time;
	
}
