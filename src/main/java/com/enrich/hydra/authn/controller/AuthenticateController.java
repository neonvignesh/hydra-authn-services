package com.enrich.hydra.authn.controller;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.enrich.hydra.authn.pojo.StandardResponseMessage;
import com.enrich.hydra.authn.pojo.UserInfoPojo;
import com.enrich.hydra.authn.service.AuthenticateService;
import com.enrich.hydra.authn.util.StandardResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequestMapping("/hydra-authn-api/v1")
public class AuthenticateController {

	@Autowired
	private AuthenticateService service;

	@RequestMapping(value = "authenticate", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<StandardResponseMessage> loginAuthenticate(@RequestBody UserInfoPojo client,HttpServletResponse response) {
		try {
			StandardResponseMessage result = service.authenticateService(client,response);
			return StandardResponseUtil.generateResponseEntity(result);
		} catch (Exception e) {
			log.error("Error occurred in authenticate Controller: " + e.getMessage());
			StandardResponseMessage error = StandardResponseUtil.prepareInternalServerErrorResponse();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}

	@RequestMapping(value = "logout", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<StandardResponseMessage> getLogout(@RequestBody UserInfoPojo client,
			HttpServletRequest request)
			throws MalformedURLException, UnsupportedEncodingException, NoSuchAlgorithmException {
		try {
			StandardResponseMessage result = service.logout(client, request);
			return StandardResponseUtil.generateResponseEntity(result);
		} catch (Exception e) {
			log.error("Error occurred in logout Controller:" + e.getMessage());
			StandardResponseMessage error = StandardResponseUtil.prepareInternalServerErrorResponse();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}
}
