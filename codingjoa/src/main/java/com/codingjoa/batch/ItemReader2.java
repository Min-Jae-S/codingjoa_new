package com.codingjoa.batch;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

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
	
	private List<String> items = new ArrayList<>(List.of("lee", "park", "kim"));
	
	@PostConstruct
	public void init() {
		log.info("## {}.init", this.getClass().getSimpleName());
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
