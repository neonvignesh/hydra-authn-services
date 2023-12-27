package com.enrich.hydra.authn.controller;

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
import com.enrich.hydra.authn.service.ForgotPasswordService;
import com.enrich.hydra.authn.util.StandardResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@RequestMapping("/hydra-authn-api/v1")
public class ForgotPasswordController {

	@Autowired
	private ForgotPasswordService service;
	
	@RequestMapping(value = "password/forgot", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<StandardResponseMessage> forgotPassword(@RequestBody UserInfoPojo client){
		try {
			StandardResponseMessage result = service.forgotPassword(client);
			 return StandardResponseUtil.generateResponseEntity(result);
		} catch (Exception e) {
			log.error("Error occurred in forgotPassword Controller:" + e.getMessage());
			StandardResponseMessage error = StandardResponseUtil.prepareInternalServerErrorResponse();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}
}
