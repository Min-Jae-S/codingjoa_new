package com.codingjoa.test;

import java.beans.PropertyEditorSupport;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		log.info("-------- {}#setAsText --------", this.getClass().getSimpleName());
		log.info("\t > text = {}", text);
		
		try {
			setValue(Integer.parseInt(text));
		} catch (NumberFormatException e) {
			log.info("\t > {}", e.getClass().getSimpleName());
			setValue(100);
		}
	}

}
