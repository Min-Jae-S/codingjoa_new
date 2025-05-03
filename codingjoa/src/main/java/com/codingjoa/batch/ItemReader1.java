package com.codingjoa.batch;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@StepScope
@Component
public class ItemReader1 implements ItemReader<String> {
	
	private List<String> items;
	
//	public ItemReader1(@Value("#{jobParameters}") Map<String, Object> jobParameters) {
//		log.info("## ItemReader1 constructor");
//		
//		String lastNamesStr = (String) jobParameters.get("lastNamesStr");
//		this.items = Arrays.stream(lastNamesStr.split(",")).collect(Collectors.toList());
//		log.info("\t > items = {}", items);
//	}

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		log.info("## {}.beforeStep: {}", this.getClass().getSimpleName(), stepExecution.getJobParameters());
		
		JobParameters jobParameters = stepExecution.getJobParameters();
		String lastNamesStr = jobParameters.getString("lastNamesStr");
		
		this.items = Arrays.stream(lastNamesStr.split(",")).collect(Collectors.toList());
		log.info("\t > items = {}", items);
	}
	
	@Override
	public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		String item = !items.isEmpty() ? items.remove(0) : null;
		log.info("## {}.read: {}", this.getClass().getSimpleName(), item);
		
		return item;
	}

}
