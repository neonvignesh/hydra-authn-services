package com.enrich.hydra.authn.util.external;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.enrich.hydra.authn.constant.Constants;
import com.enrich.hydra.authn.pojo.UserInfoPojo;
import com.enrich.hydra.authn.provider.HydraServiceProvider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KambalaUtil {

	@Autowired
	private HydraServiceProvider provider;

	public ResponseEntity<String> authenticate(UserInfoPojo client) {
		try {
			JSONObject inputJson = new JSONObject();
			KambalaServiceHelper.addParams(inputJson, client, provider, false);
			RestTemplate restTemplate = new RestTemplate();
			Map<String, Object> map = new HashMap<>();
			map.put(Constants.J_DATA, inputJson.toJSONString());
			String bodyproperties = KambalaServiceHelper.urlEncodeUTF8(map);
			HttpEntity<String> requestparam = new HttpEntity<>(bodyproperties);	
			return restTemplate.postForEntity(provider.getAuthenticateUrl(), requestparam, String.class);
		} catch (Exception e) {
			log.error("Error occurred on authenticate service: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<String> getLoginResponse(UserInfoPojo client) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			JSONObject inputJson = new JSONObject();
			KambalaServiceHelper.addParams(inputJson, client, provider, true);
			Map<String, Object> map = new HashMap<>();
			map.put(Constants.J_DATA, inputJson.toJSONString());
			String bodyproperties = KambalaServiceHelper.urlEncodeUTF8(map);
			HttpEntity<String> requestparam = new HttpEntity<>(bodyproperties);
			return restTemplate.postForEntity(provider.getOtpLoginUrl(), requestparam, String.class);
		} catch (Exception e) {
			log.error("Error Occured on getLoginResponse service :", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.INTERNAL_SERVER_ERROR);
		}

	}

	public ResponseEntity<String> generateOtpResponse(UserInfoPojo client) {
		try {
			JSONObject inputJson = new JSONObject();
			KambalaServiceHelper.addOtpParam(inputJson, client, provider);
			RestTemplate restTemplate = new RestTemplate();
			Map<String, Object> map = new HashMap<>();
			map.put(Constants.J_DATA, inputJson.toJSONString());
			String bodyproperties = KambalaServiceHelper.urlEncodeUTF8(map);
			HttpEntity<String> requestparam = new HttpEntity<>(bodyproperties);
			return restTemplate.postForEntity(provider.getOtpUrl(), requestparam, String.class);
		} catch (Exception e) {
			log.error("Error Occured on generateOtpResponse Service :", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<String> getLogoutResponse(UserInfoPojo client, String jKey) {
		try {
			JSONObject inputJson = new JSONObject();
			KambalaServiceHelper.addUserParam(inputJson, client, provider);
			RestTemplate restTemplate = new RestTemplate();
			Map<String, Object> map = new HashMap<>();
			map.put(Constants.J_DATA, inputJson.toJSONString());
			map.put(Constants.J_KEY, jKey);
			String bodyproperties = KambalaServiceHelper.urlEncodeUTF8(map);
			HttpEntity<String> requestparam = new HttpEntity<>(bodyproperties);
			return restTemplate.postForEntity(provider.getLogoutUrl(), requestparam, String.class);
		} catch (HttpClientErrorException e) {
		    if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Constants.UNAUTHORIZED);	        
		    } else {
		        log.error("Error Occurred on getLogoutResponse Service: " + e.getMessage());
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.INTERNAL_SERVER_ERROR);
		    }
		} catch (Exception e) {
			log.error("Error Occured on getLogoutResponse Service :", e.getMessage());			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<String> forgotPassword(UserInfoPojo client) {
		try {
			JSONObject inputJson = new JSONObject();
			KambalaServiceHelper.addForgotPasswordParam(inputJson, client, provider);
			RestTemplate restTemplate = new RestTemplate();
			Map<String, Object> map = new HashMap<>();
			map.put(Constants.J_DATA, inputJson.toJSONString());
			String bodyproperties = KambalaServiceHelper.urlEncodeUTF8(map);
			HttpEntity<String> requestparam = new HttpEntity<>(bodyproperties);
			return restTemplate.postForEntity(provider.getForgotPasswordUrl(), requestparam, String.class);
		} catch (Exception e) {
			log.error("Error Occured on forgotPassword Service :", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<String> changePassword(UserInfoPojo client) {
		try {
			JSONObject inputJson = new JSONObject();
			KambalaServiceHelper.addChangePasswordParam(inputJson, client, provider);
			RestTemplate restTemplate = new RestTemplate();
			Map<String, Object> map = new HashMap<>();
			map.put(Constants.J_DATA, inputJson.toJSONString());
			String bodyproperties = KambalaServiceHelper.urlEncodeUTF8(map);
			HttpEntity<String> requestparam = new HttpEntity<>(bodyproperties);
			return restTemplate.postForEntity(provider.getChangePasswordUrl(),requestparam,String.class);
		} catch (Exception e) {
			log.error("Error Occured on changePassword Service :", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<String> getSecretKey(Map<String, Object> map) {
		try {
			String bodyproperties = KambalaServiceHelper.urlEncodeUTF8(map);
			HttpEntity<String> requestparam = new HttpEntity<>(bodyproperties);
			RestTemplate restTemplate = new RestTemplate();
			return restTemplate.postForEntity(provider.getGetSecretKey(), requestparam, String.class);
		} catch (HttpClientErrorException e) {
			log.error("Error occured in getSecretKey service : " + e.getMessage());
			return ResponseEntity.status(e.getStatusCode()).body(e.getStatusText());
		}
	}

	public ResponseEntity<String> generateSecretKey(Map<String, Object> map) {
		try {
			String bodyproperties = KambalaServiceHelper.urlEncodeUTF8(map);
			HttpEntity<String> requestparam = new HttpEntity<>(bodyproperties);
			RestTemplate restTemplate = new RestTemplate();
			return restTemplate.postForEntity(provider.getGenerateSecretKey(), requestparam, String.class);
		} catch (HttpClientErrorException e) {
			log.error("Error occured in generateSecretKey service : " + e.getMessage());
			return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
		}
	}

	public ResponseEntity<String> getMWList(UserInfoPojo client, String jKey) {
		try {
			JSONObject inputJson = new JSONObject();
			KambalaServiceHelper.addUserParam(inputJson, client, provider);
			RestTemplate restTemplate = new RestTemplate();
			Map<String, Object> map = new HashMap<>();
			map.put(Constants.J_DATA, inputJson.toJSONString());
			map.put(Constants.J_KEY, jKey);
			String bodyproperties = KambalaServiceHelper.urlEncodeUTF8(map);
			HttpEntity<String> requestparam = new HttpEntity<>(bodyproperties);
			return restTemplate.postForEntity(provider.getMwListUrl(), requestparam, String.class);
		} catch (HttpClientErrorException e) {
		    if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Constants.UNAUTHORIZED);	        
		    } else {
		        log.error("Error Occurred on getMWList Service: " + e.getMessage());
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.INTERNAL_SERVER_ERROR);
		    }
		} catch (Exception e) {
			log.error("Error Occured on getMWList Service :", e.getMessage());			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.INTERNAL_SERVER_ERROR);
		}
	}
}
