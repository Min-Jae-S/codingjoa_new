package com.codingjoa.config;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ModelMapperConfig {
	
	@PostConstruct
	public void init() {
		log.info("===============================================================");
		log.info("@ ModelMapperConfig");
		log.info("===============================================================");
	}
	
	@Bean
	public ModelMapper modelMapper() {
		log.info("## modelMapper");
		ModelMapper modelMapper = new ModelMapper();
		org.modelmapper.config.Configuration config = modelMapper.getConfiguration();
		config
			.setMatchingStrategy(MatchingStrategies.STRICT)
			// setter가 없는 Dto(UserDetailsDto)에 대한 mapping을 위해 fieldAccessLevel과 fieldMatchingEnabled를 설정 
			.setFieldAccessLevel(AccessLevel.PRIVATE)
			.setFieldMatchingEnabled(true);
		log.info("\t > matchingStrategy  = {}", config.getMatchingStrategy());
		log.info("\t > fieldAccessLevel  = {}", config.getFieldAccessLevel());
		log.info("\t > methodAccessLevel = {}", config.getMethodAccessLevel());
		log.info("\t > propertyCondition = {}", config.getPropertyCondition());
		log.info("\t > isFieldMatchingEnabled = {}", config.isFieldMatchingEnabled());
		log.info("\t > isSkipNullEnabled = {}", config.isSkipNullEnabled());
		log.info("\t > isCollectionsMergeEnabled = {}", config.isCollectionsMergeEnabled());
		return modelMapper;
	}
	
}
