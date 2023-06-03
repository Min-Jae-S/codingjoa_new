package com.codingjoa.test;

import java.beans.PropertyEditorSupport;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestEditor extends PropertyEditorSupport {

	@Override
	public String getAsText() {
		log.info("-------- {}#getAsText --------", this.getClass().getSimpleName());
		return super.getAsText();
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		log.info("-------- {}#setAsText --------", this.getClass().getSimpleName());
		super.setAsText(text);
	}

}
