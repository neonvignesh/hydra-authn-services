package com.enrich.hydra.authn.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.enrich.hydra.authn.pojo.StandardResponseMessage;
import com.enrich.hydra.authn.pojo.UserInfoPojo;
import com.enrich.hydra.authn.service.MWListService;
import com.enrich.hydra.authn.util.StandardResponseUtil;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
@RequestMapping("/hydra-authn-api/v1")
public class MWListController {

	@Autowired
	private MWListService service;
	
	@RequestMapping(value = "/mwList", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<StandardResponseMessage> mwList(@RequestBody UserInfoPojo client,HttpServletRequest request){		
		try {
			StandardResponseMessage result =  service.getMWList(client,request);
			 return StandardResponseUtil.generateResponseEntity(result);
		} catch (Exception e) {
			log.error("Error occurred in mwList Controller:" + e.getMessage());
			StandardResponseMessage error = StandardResponseUtil.prepareInternalServerErrorResponse();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}
}
