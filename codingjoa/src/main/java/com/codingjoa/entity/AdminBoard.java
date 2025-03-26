package com.codingjoa.entity;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AdminBoard {
	
	private Board board;
	private User user;
	private Category category;
	
}
