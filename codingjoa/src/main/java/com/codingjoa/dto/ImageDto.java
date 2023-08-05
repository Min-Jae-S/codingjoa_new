package com.codingjoa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {

	private Integer imageIdx;
	private String imageName;
	private String imageUrl;
	
}
