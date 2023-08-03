package com.codingjoa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadResultDto {

	private int idx;
	private String uploadUrl;
	private String filename;
	
}
