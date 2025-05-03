package com.codingjoa.batch;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ItemReader2 implements ItemReader<String> {
	
	private List<String> items;
	
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
