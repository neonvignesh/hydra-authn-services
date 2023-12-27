package com.enrich.hydra.authn.util.external;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.simple.JSONObject;

import com.enrich.hydra.authn.constant.Constants;
import com.enrich.hydra.authn.pojo.UserInfoPojo;
import com.enrich.hydra.authn.provider.HydraServiceProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KambalaServiceHelper {

	public static String urlEncodeUTF8(Map<String, Object> map) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			if (sb.length() > 0) {
				sb.append("&");
			}
			sb.append(String.format("%s=%s", entry.getKey().toString(), entry.getValue().toString()));
		}
		return sb.toString();
	}

	public static String KambalaUserPasswordData(String data)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String sha256hexStr = DigestUtils.sha256Hex(data);
		return sha256hexStr;
	}

	public static void addParams(JSONObject json, UserInfoPojo client, HydraServiceProvider hydraServiceProvider,
			boolean includeOTP) {
		try {
			String pswd = KambalaUserPasswordData(client.getPassword());
			String vendorCode = hydraServiceProvider.getVendorKey();
			String appkey = KambalaUserPasswordData(client.getUser_id().toUpperCase() + "|" + vendorCode);
			json.put(Constants.USER_ID, client.getUser_id().toUpperCase());
			json.put(Constants.PASSWORD, pswd);
			if (includeOTP) {
				json.put(Constants.OTP, client.getOtp());
			}
			json.put(Constants.APK_VERSION, hydraServiceProvider.getApkVersion());
			json.put(Constants.IMEI, hydraServiceProvider.getImei());
			json.put(Constants.VERSION_CODE, hydraServiceProvider.getVendorCode());
			json.put(Constants.SOURCE, hydraServiceProvider.getSource());
			json.put(Constants.APP_KEY, appkey);
		} catch (Exception e) {
			log.error("Error Occurred in addParams: " + e.getMessage());
		}
	}

	public static void addOtpParam(JSONObject inputJson, UserInfoPojo client, HydraServiceProvider provider) {
		inputJson.put(Constants.USER_ID, client.getUser_id());
		inputJson.put(Constants.PAN_NO, client.getPan_no());
	}

	public static void addUserParam(JSONObject inputJson, UserInfoPojo client, HydraServiceProvider provider) {
		inputJson.put(Constants.USER_ID, client.getUser_id());
	}

	public static void addForgotPasswordParam(JSONObject inputJson, UserInfoPojo client,
			HydraServiceProvider provider) {
		inputJson.put(Constants.USER_ID, client.getUser_id());
		inputJson.put(Constants.PAN_NO, client.getPan_no());
		inputJson.put(Constants.DOB, client.getDate_of_birth());
	}

	public static void addChangePasswordParam(JSONObject inputJson, UserInfoPojo client,
			HydraServiceProvider provider) {
		try {
			String oldpswd = KambalaServiceHelper.KambalaUserPasswordData(client.getOld_password());
			inputJson.put(Constants.USER_ID, client.getUser_id());
			inputJson.put(Constants.OLD_PWD, oldpswd);
			inputJson.put(Constants.PWD, client.getPassword());
		} catch (Exception e) {
			log.error("Error Occurred in addChangePasswordParam: " + e.getMessage());
		}
	}

	public static void addClientParam(JSONObject inputJson, UserInfoPojo client, HydraServiceProvider provider) {
		inputJson.put(Constants.USER_ID, client.getUser_id());
		inputJson.put(Constants.ACCOUNT_ID, client.getAccount_id());
	}

	public static JSONObject addUidJkey(String user_id) {
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(Constants.USER_ID, user_id);
		return new JSONObject(inputMap);
	}
}