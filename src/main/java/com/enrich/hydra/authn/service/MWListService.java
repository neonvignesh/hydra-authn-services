package com.enrich.hydra.authn.service;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.enrich.hydra.authn.constant.Constants;
import com.enrich.hydra.authn.pojo.StandardResponseMessage;
import com.enrich.hydra.authn.pojo.UserInfoPojo;
import com.enrich.hydra.authn.pojo.kambala.LogoutKambalaResponse;
import com.enrich.hydra.authn.pojo.user.LogoutUserResponse;
import com.enrich.hydra.authn.util.StandardResponseUtil;
import com.enrich.hydra.authn.util.Validator;
import com.enrich.hydra.authn.util.external.KambalaUtil;
import com.enrich.hydra.authn.util.external.ResponseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MWListService {

	@Autowired
	private KambalaUtil kambalaUtil;
	
	public StandardResponseMessage getMWList(UserInfoPojo client,HttpServletRequest request) {
		try {
			String jKey = request.getHeader(Constants.X_TOKEN);
			Boolean validationResponse = validateParameterForMWList(client,jKey);
			if (!validationResponse)
				return StandardResponseUtil.prepareBadRequestResponse(Constants.MW_LIST);
			else {
				ResponseEntity<String> mwListOutput = kambalaUtil.getMWList(client,jKey);
				StandardResponseMessage standardResponse = getMWListResponse(mwListOutput);
				return standardResponse;
			}
		} catch (Exception e) {
			log.error("Error Occured on getMWList Service :", e.getMessage());
			return StandardResponseUtil.prepareInternalServerErrorResponse();
		}
	}

	private StandardResponseMessage getMWListResponse(ResponseEntity<String> mwListOutput) throws JsonMappingException, JsonProcessingException, ParseException {
			StandardResponseMessage standardResponse = new StandardResponseMessage();
			if (mwListOutput.getStatusCode() == HttpStatus.UNAUTHORIZED)
				return StandardResponseUtil.prepareUnAuthorizedResponse();
			if (Validator.hasData(mwListOutput)) {
				String output = mwListOutput.getBody();
				ObjectMapper objectMapper = new ObjectMapper();
				LogoutKambalaResponse responseInfo = objectMapper.readValue(output, LogoutKambalaResponse.class);
				LogoutUserResponse userResponse = ResponseUtil.createLogoutUserResponse(responseInfo);
				standardResponse = prepareResponseBasedOnStatus(responseInfo.getStat(), responseInfo.getEmsg(),userResponse, standardResponse);
			} else
				return StandardResponseUtil.prepareInternalServerErrorResponse();
			return standardResponse;
		}

		private StandardResponseMessage prepareResponseBasedOnStatus(String status, String message,
				LogoutUserResponse userResponse, StandardResponseMessage standardResponse) {
			if (Constants.NOT_OK.equals(status)) {
				if (Constants.SESSION_EXPIRED.equals(message))
					standardResponse = StandardResponseUtil.prepareUnAuthorizedResponse(userResponse);
				else
					standardResponse = StandardResponseUtil.prepareBadRequestResponse(userResponse);
			} else if (Constants.OK.equals(status))
				standardResponse = StandardResponseUtil.prepareSuccessResponse(userResponse);
			return standardResponse;
		}

	private Boolean validateParameterForMWList(UserInfoPojo client,String jKey) {
		if (!Validator.hasData(client.getUser_id()) || !Validator.hasData(jKey))
			return false;
		else
			return true;
	}
}

