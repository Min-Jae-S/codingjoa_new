package com.codingjoa.entity;

import lombok.Data;

@Data
public class BoardLikes {
	
	private Integer boardLikesIdx;
	private Integer boardIdx;
	private Integer memberIdx;
}
