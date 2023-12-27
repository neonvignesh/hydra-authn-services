package com.enrich.hydra.authn.pojo.user;

import com.enrich.hydra.authn.common.ApiResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerateOtpUserResponse implements ApiResponse{

	private String Status;
    private String user_id;
	
}
