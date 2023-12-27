package com.enrich.hydra.authn.service;

import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.enrich.hydra.authn.constant.Constants;
import com.enrich.hydra.authn.pojo.StandardResponseMessage;
import com.enrich.hydra.authn.pojo.UserInfoPojo;
import com.enrich.hydra.authn.pojo.kambala.GenerateOtpKambalaResponse;
import com.enrich.hydra.authn.pojo.kambala.LoginKambalaResponse;
import com.enrich.hydra.authn.pojo.user.GenerateOtpUserResponse;
import com.enrich.hydra.authn.pojo.user.LoginUserResponse;
import com.enrich.hydra.authn.util.StandardResponseUtil;
import com.enrich.hydra.authn.util.Validator;
import com.enrich.hydra.authn.util.external.KambalaUtil;
import com.enrich.hydra.authn.util.external.ResponseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OtpService {

	@Autowired
	private KambalaUtil kambalaUtil;

	public StandardResponseMessage generateOtp(UserInfoPojo client,HttpServletResponse response) {
		try {
			Boolean validationResponse = validateParameterForGenerateOtp(client);
			if (!validationResponse)
				return StandardResponseUtil.prepareBadRequestResponse(Constants.OTP_ERROR);
			else {
				ResponseEntity<String> kambalaOutput = kambalaUtil.generateOtpResponse(client);
				StandardResponseMessage standardResponse = getOtpResponse(kambalaOutput,response);
				return standardResponse;
			}
		} catch (Exception e) {
			log.error("Error Occured on generateOtpService :", e.getMessage());
			  return StandardResponseUtil.prepareInternalServerErrorResponse(Constants.LOGIN_CUSTOMER_ERRR,response);
		}
	}

	private StandardResponseMessage getOtpResponse(ResponseEntity<String> kambalaOutput,HttpServletResponse response)
			throws JsonMappingException, JsonProcessingException, ParseException {
		StandardResponseMessage standardResponse = new StandardResponseMessage();
		if (Validator.hasData(kambalaOutput)) {
			String output = kambalaOutput.getBody();
			ObjectMapper objectMapper = new ObjectMapper();
			GenerateOtpKambalaResponse responseInfo = objectMapper.readValue(output, GenerateOtpKambalaResponse.class);
			GenerateOtpUserResponse userResponse = ResponseUtil.createGenerateOtpResponse(responseInfo);
			standardResponse = prepareResponseBasedOnStatus(output, responseInfo, userResponse, standardResponse);
		} else
			  return StandardResponseUtil.prepareInternalServerErrorResponse(Constants.LOGIN_CUSTOMER_ERRR,response);
		return standardResponse;
	}

	private StandardResponseMessage prepareResponseBasedOnStatus(String output, GenerateOtpKambalaResponse responseInfo,
			GenerateOtpUserResponse userResponse, StandardResponseMessage standardResponse) {
		String status = responseInfo.getReqStatus();
		if (Constants.OTP_SUCCESS.equals(status)) {
			standardResponse = StandardResponseUtil.prepareSuccessResponse(userResponse);
		} else if (Constants.USER_INVALID.equals(status) || Constants.PASSWORD_WRONG.equals(status)
				|| Constants.WRONG_PAN.equals(status)) {
			userResponse.setStatus(Constants.PAN_INVALID);
			standardResponse = StandardResponseUtil.prepareBadRequestResponse(userResponse);
		} else
			standardResponse = StandardResponseUtil.prepareBadRequestResponse(userResponse);
		return standardResponse;
	}

	private Boolean validateParameterForGenerateOtp(UserInfoPojo client) {
		if (!Validator.hasData(client.getUser_id()) || !Validator.hasData(client.getPan_no()))
			return false;
		else
			return true;
	}

	public StandardResponseMessage loginWithOtp(UserInfoPojo client,HttpServletResponse response) throws Exception {
		try {
			Boolean validationResponse = validateParameterForLogin(client);
			if (!validationResponse)
				return StandardResponseUtil.prepareBadRequestResponse(Constants.OTP_INVALD);
			else {
				ResponseEntity<String> loginOutput = kambalaUtil.getLoginResponse(client);
				return processLoginResponse(loginOutput,response);
			}
		} catch (Exception e) {
			log.error("Error Occurred in  loginWithOtp service : " + e.getMessage());
			  return StandardResponseUtil.prepareInternalServerErrorResponse(Constants.LOGIN_CUSTOMER_ERRR,response);
		}
	}

	private StandardResponseMessage processLoginResponse(ResponseEntity<String> loginOutput,HttpServletResponse response) throws Exception {
		StandardResponseMessage standardResponse = new StandardResponseMessage();
		if (Validator.hasData(loginOutput)) {
			String loginResponse = loginOutput.getBody();
			ObjectMapper objectMapper = new ObjectMapper();
			LoginKambalaResponse loginInfo = objectMapper.readValue(loginResponse, LoginKambalaResponse.class);
			LoginUserResponse userResponse = ResponseUtil.standardizeLoginResponse(loginInfo);
			return handleLoginResponse(loginInfo.getStat(), loginInfo.getEmsg(), userResponse, standardResponse);
		} else
			  return StandardResponseUtil.prepareInternalServerErrorResponse(Constants.LOGIN_CUSTOMER_ERRR,response);
	}

	private StandardResponseMessage handleLoginResponse(String status, String message, LoginUserResponse userResponse,
			StandardResponseMessage standardResponse2) {
		StandardResponseMessage standardResponse = new StandardResponseMessage();
		if (Constants.NOT_OK.equals(status)) {
			if (Constants.INVALID_USER.equals(message) || Constants.INVALID_OTP.equals(message)
					|| Constants.WRONG_OTP.equals(message) || Constants.WRONG_PASSWORD.equals(message)) {
				userResponse.setError_message(Constants.OTP_INVALD);
				standardResponse = StandardResponseUtil.prepareUnAuthorizedResponse(userResponse);
			} else if (Constants.USER_BLOCKED.equals(message)) {
				standardResponse = StandardResponseUtil.prepareForbiddenResponse(userResponse);
			} else
				standardResponse = StandardResponseUtil.prepareBadRequestResponse(userResponse);
		} else if (Constants.OK.equals(status)) {
			standardResponse = StandardResponseUtil.prepareSuccessResponse(userResponse);
		}
		return standardResponse;
	}

	private Boolean validateParameterForLogin(UserInfoPojo client) {
		if (!Validator.hasData(client.getUser_id()) || !Validator.hasData(client.getPassword())
				|| !Validator.hasData(client.getOtp()))
			return false;
		else
			return true;
	}
}
