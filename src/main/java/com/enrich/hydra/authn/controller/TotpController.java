package com.enrich.hydra.authn.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enrich.hydra.authn.constant.Constants;
import com.enrich.hydra.authn.pojo.StandardResponseMessage;
import com.enrich.hydra.authn.service.TotpService;
import com.enrich.hydra.authn.util.StandardResponseUtil;
import com.enrich.hydra.authn.util.Validator;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/hydra-authn-api/v1")
public class TotpController {

	@Autowired
	TotpService totpService;

	@PostMapping("/enable/totp")
	public ResponseEntity<?> enableTotp(HttpServletRequest request, @RequestParam String user_id) throws Exception {
		try {
			String jKey = request.getHeader(Constants.X_TOKEN);
			if (Validator.hasData(jKey) && Validator.hasData(user_id)) {
				return totpService.getSecretKey(request, user_id, jKey);
			} else {
				return new ResponseEntity<>(StandardResponseUtil.prepareBadRequestResponse(Constants.MW_LIST), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.error("Error occured in enableTotp controller " + e.getMessage());
			StandardResponseMessage error = StandardResponseUtil.prepareInternalServerErrorResponse();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}
}
