package com.enrich.hydra.authn.pojo.kambala;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangePasswordKambalaResponse {

	@JsonProperty("stat")
	private String stat;
	
	@JsonProperty("dmsg")
	private String dmsg;
	
	@JsonProperty("request_time")
	private String request_time;
	
	@JsonProperty("emsg")
	private String emsg;
	
}
