package com.codingjoa.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.ToString;

//	{
//	  "resultcode" : "00",
//	  "message" : "success",
//	  "response" : {
//	    "id" : "UYTs-zkmWvHlYIrBiwcqLJu7i04g94NbIlfeuEOl-Og",
//	    "email" : "smj20228@naver.com",
//	    "name" : "서민재"
//	  }
//	}

@ToString
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverUserInfoResponse {
	
	@JsonProperty("resultcode")
	private String resultCode;
	
	private String message;
	private String id;
	private String email;
	private String name;
	
	@JsonProperty("response")
	private void unpackResponse(JsonNode response) {
		this.id = response.path("id").asText();
		this.email = response.path("email").asText();
		this.name = response.path("name").asText();
	}

}
