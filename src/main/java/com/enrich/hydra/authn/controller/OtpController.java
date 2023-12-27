package com.enrich.hydra.authn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enrich.hydra.authn.pojo.StandardResponseMessage;
import com.enrich.hydra.authn.pojo.UserInfoPojo;
import com.enrich.hydra.authn.service.OtpService;
import com.enrich.hydra.authn.util.StandardResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@RequestMapping("/hydra-authn-api/v1")
public class OtpController {

	@Autowired
	private OtpService service;

	@RequestMapping(value = "/otp/generate", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<StandardResponseMessage> generateOtp(@RequestBody UserInfoPojo client,HttpServletResponse response) {
		try {
			StandardResponseMessage result = service.generateOtp(client,response);
			return StandardResponseUtil.generateResponseEntity(result);
		} catch (Exception e) {
			log.error("Error occurred in generateOtp Controller:" + e.getMessage());
			StandardResponseMessage error = StandardResponseUtil.prepareInternalServerErrorResponse();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}

	@RequestMapping(value = "/otp/login", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<StandardResponseMessage> loginWithOtp(@RequestBody UserInfoPojo client, HttpServletResponse response) {
		try {
			StandardResponseMessage result = service.loginWithOtp(client,response);
			return StandardResponseUtil.generateResponseEntity(result);
		} catch (Exception e) {
			log.error("Error occurred in loginWithOtp Controller:" + e.getMessage());
			StandardResponseMessage error = StandardResponseUtil.prepareInternalServerErrorResponse();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}

}
