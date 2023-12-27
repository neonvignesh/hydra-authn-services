package com.enrich.hydra.authn.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enrich.hydra.authn.constant.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/hydra-authn-api/internal/health")
public class HealthController {

	 @GetMapping
		public String doHealthCheck() {
			log.info("Health Ok");
			return Constants.OK;
		}


}