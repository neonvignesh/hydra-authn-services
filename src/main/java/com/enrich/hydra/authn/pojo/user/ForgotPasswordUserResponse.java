package com.enrich.hydra.authn.pojo.user;

import com.enrich.hydra.authn.common.ApiResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForgotPasswordUserResponse implements ApiResponse {

	private String request_date_time;
	private String status;
	private String error_message;
}
