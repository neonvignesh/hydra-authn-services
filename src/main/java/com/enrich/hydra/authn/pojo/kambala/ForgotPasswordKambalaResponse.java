package com.enrich.hydra.authn.pojo.kambala;

import com.enrich.hydra.authn.common.ApiResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForgotPasswordKambalaResponse implements ApiResponse{
	
	private String request_time;
	private String stat;
	private String emsg;
}
