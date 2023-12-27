package com.enrich.hydra.authn.pojo.kambala;

import java.util.List;
import java.util.Map;

import com.enrich.hydra.authn.common.ApiResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginKambalaResponse implements ApiResponse {

	private String request_time;
	private String actid;
	private List<String> access_type;
	private String uname;
	private List<ProductInfo> prarr;
	private String stat;
	private String susertoken;
	private String email;
	private String uid;
	private String brnchid;
	private String totp_set;
	private List<String> orarr;
	private List<String> exarr;
	private List<String> values;
	private Map<String, List<MarketWatchItem>> mws;
	private String brkname;
    private String emsg;
	private String lastaccesstime;

	@Getter
	@Setter
	public static class ProductInfo {
		private String prd;
		private String s_prdt_ali;
		private List<String> exch;
	}

	@Getter
	@Setter
	public static class MarketWatchItem {
	    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
		private List<String> exch;
		private String token;
		private String tsym;
		private String weekly;
		private String instname;
		private String pp;
		private String ls;
		private String ti;
		private String optt;
		private String nontrd;
	}	
}
