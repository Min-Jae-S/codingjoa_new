package com.codingjoa.batch;

import java.util.Arrays;
import java.util.List;
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

@Slf4j
@StepScope
@Component
public class ItemReader1 implements ItemReader<String> {
	
	private final List<String> items;
	
	public ItemReader1(@Value("#{jobParameters}") JobParameters jobParameters) {
		log.info("## ItemReader1 constructor");
		log.info("\t > jobParameters = {}", jobParameters);
		
		String lastNames = jobParameters.getString("lastNamesStr");
		this.items = Arrays.stream(lastNames.split(",")).collect(Collectors.toList());
		log.info("\t > items = {}", items);
	}

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		log.info("## {}.beforeStep: {}", this.getClass().getSimpleName(), stepExecution.getJobParameters());
	}
	
	@Override
	public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		String item = !items.isEmpty() ? items.remove(0) : null;
		log.info("## {}.read: {}", this.getClass().getSimpleName(), item);
		
		return item;
	}

}
