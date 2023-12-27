package com.enrich.hydra.authn.pojo.kambala;

import com.enrich.hydra.authn.common.ApiResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerateOtpKambalaResponse implements ApiResponse {

	@JsonProperty("uid")
	private String uid;

	@JsonProperty("ReqStatus")
	private String reqStatus;
}
