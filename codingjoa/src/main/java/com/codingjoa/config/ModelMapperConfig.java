package com.codingjoa.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
	
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration()
			.setMatchingStrategy(MatchingStrategies.STRICT)
			// setter가 없는 Dto(UserDetailsDto)에 대한 mapping을 위해 fieldAccessLevel과 fieldMatchingEnabled를 설정 
			.setFieldAccessLevel(AccessLevel.PRIVATE)
			.setFieldMatchingEnabled(true);
//		log.info("\t > matchingStrategy = {}", config.getMatchingStrategy());
//		log.info("\t > fieldAccessLevel = {}", config.getFieldAccessLevel());
//		log.info("\t > methodAccessLevel = {}", config.getMethodAccessLevel());
//		log.info("\t > propertyCondition = {}", config.getPropertyCondition());
//		log.info("\t > isFieldMatchingEnabled = {}", config.isFieldMatchingEnabled());
//		log.info("\t > isSkipNullEnabled = {}", config.isSkipNullEnabled());
//		log.info("\t > isCollectionsMergeEnabled = {}", config.isCollectionsMergeEnabled());
		
		return modelMapper;
	}
	
}
