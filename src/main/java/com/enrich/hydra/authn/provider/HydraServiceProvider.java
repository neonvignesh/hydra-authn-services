package com.enrich.hydra.authn.provider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@Configuration
public class HydraServiceProvider {

	@Value("${KambalaApi.generateOtp.Url}")
	String otpUrl;
	
	@Value("${KambalaApi.authenticate.Url}")
	String authenticateUrl;
	
	@Value("${KambalaApi.login.Url}")
	String otpLoginUrl;
	
	@Value("${KambalaApi.logout.Url}")
	String logoutUrl;
	
	@Value("${KambalaApi.forgotPassword.Url}")
	String forgotPasswordUrl;
	
	@Value("${KambalaApi.changePassword.Url}")
	String changePasswordUrl;
	
	@Value("${KambalaApi.mwList.Url}")
	String mwListUrl;
	
	@Value("${Kambala.apkVersion}")
	String apkVersion;

	@Value("${Kambala.vendorKey}")
	String vendorKey;
	
	@Value("${Kambala.source}")
	String source;
	
	@Value("${Kambala.vendorCode}")
	String vendorCode;

	@Value("${Kambala.imei}")
	String imei;
	
	@Value("${Hydra.ApiKey}")
	String apiKey;

	@Value("${KambalaApi.generate.secret.key}")
	String generateSecretKey;

	@Value("${KambalaApi.get.secret.key}")
	String getSecretKey;
}
