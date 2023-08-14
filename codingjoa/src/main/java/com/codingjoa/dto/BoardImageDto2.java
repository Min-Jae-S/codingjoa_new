package com.codingjoa.dto;

import org.springframework.core.io.UrlResource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardImageDto2 {

	private Integer boardImageIdx;
	private UrlResource boardImageResource;
}
