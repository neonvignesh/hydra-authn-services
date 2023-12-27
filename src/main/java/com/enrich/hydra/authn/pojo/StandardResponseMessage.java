package com.enrich.hydra.authn.pojo;

import com.enrich.hydra.authn.common.ApiResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StandardResponseMessage implements ApiResponse {

	private String systemMessage;
	private String systemMessageType;
	private String errorMessage;
	private boolean success;
	private ApiResponse data;

}
