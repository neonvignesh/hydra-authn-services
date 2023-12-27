package com.enrich.hydra.authn.service;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.enrich.hydra.authn.constant.Constants;
import com.enrich.hydra.authn.pojo.StandardResponseMessage;
import com.enrich.hydra.authn.pojo.UserInfoPojo;
import com.enrich.hydra.authn.pojo.kambala.ChangePasswordKambalaResponse;
import com.enrich.hydra.authn.pojo.user.ChangePasswordUserResponse;
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
public class ChangePasswordService {
	
	@Autowired
	private KambalaUtil kambalaUtil;
	
	public StandardResponseMessage changePassword(UserInfoPojo client) {
		try {
			Boolean validationResponse = validateParameterForChangePassword(client);
			if (!validationResponse) 
				return StandardResponseUtil.prepareBadRequestResponse(Constants.CHANGE_ERROR);
			else {
				ResponseEntity<String> kambalaOutput = kambalaUtil.changePassword(client);
				StandardResponseMessage standardResponse = getChangePasswordResponse(kambalaOutput);
				return standardResponse;
			}
		} catch (Exception e) {
			log.error("Error Occured on changePassword Service :", e.getMessage());
			return StandardResponseUtil.prepareInternalServerErrorResponse();
		}
	}

	private StandardResponseMessage getChangePasswordResponse(ResponseEntity<String> kambalaOutput)
			throws JsonMappingException, JsonProcessingException, ParseException {
		StandardResponseMessage standardResponse = new StandardResponseMessage();
		if (Validator.hasData(kambalaOutput)) {
			String output = kambalaOutput.getBody();
			ObjectMapper objectMapper = new ObjectMapper();
			ChangePasswordKambalaResponse responseInfo = objectMapper.readValue(output,ChangePasswordKambalaResponse.class);
			ChangePasswordUserResponse userResponse = ResponseUtil.createChangePasswordResponse(responseInfo);
			standardResponse = prepareResponseBasedOnStatus(responseInfo.getStat(), responseInfo.getEmsg(),responseInfo.getDmsg(),userResponse, standardResponse);
		}else
			return StandardResponseUtil.prepareInternalServerErrorResponse();
		return standardResponse;
	}
	
	private StandardResponseMessage prepareResponseBasedOnStatus(String status, String message, String expiry_message,
			ChangePasswordUserResponse userResponse, StandardResponseMessage standardResponse) {
		if (Constants.NOT_OK.equals(status)) {
			if (Constants.ERROR_PASSWORD.equals(message) || Constants.OLD_PASSWORD.equals(message)
					|| Constants.INVALID_CLIENT.equals(message) || Constants.ERROR_OCCURED.equals(message)) {
				standardResponse = StandardResponseUtil.prepareBadRequestResponse(userResponse);
			}
			else
				standardResponse = StandardResponseUtil.prepareBadRequestResponse(userResponse);
		} else if (Constants.OK.equals(status)) {
				standardResponse = StandardResponseUtil.prepareSuccessResponse(userResponse);
		}
		return standardResponse;
	}
	
	private Boolean validateParameterForChangePassword(UserInfoPojo client) {
		if (!Validator.hasData(client.getUser_id()) || !Validator.hasData(client.getOld_password())
				|| !Validator.hasData(client.getPassword()))
			return false;
		else
			return true;
	}

}
