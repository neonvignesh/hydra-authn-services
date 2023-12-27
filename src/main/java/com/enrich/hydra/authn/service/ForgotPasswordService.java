package com.enrich.hydra.authn.service;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.enrich.hydra.authn.constant.Constants;
import com.enrich.hydra.authn.pojo.StandardResponseMessage;
import com.enrich.hydra.authn.pojo.UserInfoPojo;
import com.enrich.hydra.authn.pojo.kambala.ForgotPasswordKambalaResponse;
import com.enrich.hydra.authn.pojo.user.ForgotPasswordUserResponse;
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
public class ForgotPasswordService {

	@Autowired
	private KambalaUtil kambalaUtil;
	
	public StandardResponseMessage forgotPassword(UserInfoPojo client) {
		try {
			Boolean validationResponse = validateParameterForForgotPassword(client);
			if (!validationResponse)
				return StandardResponseUtil.prepareBadRequestResponse(Constants.FORGOT_ERROR);
			else {
				ResponseEntity<String> forgotOutput = kambalaUtil.forgotPassword(client);
				StandardResponseMessage standardResponse = getForgotPasswordResponse(forgotOutput);
				return standardResponse;
			}
		} catch (Exception e) {
			log.error("Error Occured on forgotPassword Service :", e.getMessage());
			return StandardResponseUtil.prepareInternalServerErrorResponse();
		}
	}

	private StandardResponseMessage getForgotPasswordResponse(ResponseEntity<String> kambalaOutput) throws JsonMappingException, JsonProcessingException, ParseException {
		StandardResponseMessage standardResponse = new StandardResponseMessage();
		if (Validator.hasData(kambalaOutput)) {
			String output = kambalaOutput.getBody();
			ObjectMapper objectMapper = new ObjectMapper();
			ForgotPasswordKambalaResponse responseInfo = objectMapper.readValue(output,ForgotPasswordKambalaResponse.class);
			ForgotPasswordUserResponse userResponse = ResponseUtil.createForgotPasswordResponse(responseInfo);
			standardResponse = prepareResponseBasedOnStatus(output, userResponse, standardResponse);
		}else
			return StandardResponseUtil.prepareInternalServerErrorResponse();
		return standardResponse;
	}
	
	private StandardResponseMessage prepareResponseBasedOnStatus(String output, ForgotPasswordUserResponse userResponse,
			StandardResponseMessage standardResponse) {
		if (output.contains(Constants.NOT_OK)) {
			standardResponse = StandardResponseUtil.prepareBadRequestResponse(userResponse);
		} else if (output.contains(Constants.OK)) {
			standardResponse = StandardResponseUtil.prepareSuccessResponse(userResponse);
		}
		return standardResponse;
	}

	private Boolean validateParameterForForgotPassword(UserInfoPojo client) {
		if (!Validator.hasData(client.getUser_id()) || !Validator.hasData(client.getPan_no())
				|| !Validator.hasData(client.getDate_of_birth()))
			return false;
		else
			return true;
	}

}
