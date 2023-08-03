package com.codingjoa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardImageDto {

	private int boardImageIdx;
	private String boardImageName;
	private String boardImageUrl;
	
}
