package com.enrich.hydra.authn.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.HttpClientErrorException;

import com.enrich.hydra.authn.constant.Constants;
import com.enrich.hydra.authn.pojo.StandardResponseMessage;
import com.enrich.hydra.authn.util.StandardResponseUtil;
import com.enrich.hydra.authn.util.Validator;
import com.enrich.hydra.authn.util.external.KambalaServiceHelper;
import com.enrich.hydra.authn.util.external.KambalaUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TotpService {

	@Autowired
	KambalaUtil kambalaUtil;

	public ResponseEntity<?> getSecretKey(HttpServletRequest request, String user_id, String jKey) throws Exception {
		try {
			if (ValidateParams(user_id, jKey)) {
				JSONObject userId = KambalaServiceHelper.addUidJkey(user_id);
				Map<String, Object> inputMap = buildRequestMap(userId, jKey);
				ResponseEntity<String> secretKeyOutput = kambalaUtil.getSecretKey(inputMap);
				if (secretKeyOutput.getStatusCodeValue() == 200) {
					return studyResponse(secretKeyOutput, inputMap);
				} else {
					StandardResponseMessage error = StandardResponseUtil.prepareErrorResponse(secretKeyOutput.getBody());
					return ResponseEntity.status(secretKeyOutput.getStatusCodeValue()).body(error);
				}
			}
			return null;
		} catch (Exception e) {
			log.error("Error occured in getSecretKey service " + e.getCause());
			StandardResponseMessage error = StandardResponseUtil.prepareInternalServerErrorResponse(e.getCause().toString());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}

	private boolean ValidateParams(String user_id, String jKey) {
		if (Validator.hasData(user_id) && Validator.hasData(jKey)) {
			return true;
		} else {
			return false;
		}
	}

	public static Map<String, Object> buildRequestMap(JSONObject input, String jKey) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Constants.J_DATA, input.toJSONString());
		map.put(Constants.J_KEY, jKey);
		return map;
	}

	private ResponseEntity<?> studyResponse(ResponseEntity<String> secretKeyOutput, Map<String, Object> inputMap)
			throws Exception {
		String output = secretKeyOutput.getBody();
		if (Validator.hasData(output)) {
			JSONObject resp = (JSONObject) JSONValue.parse(output);
			return isVerified(resp, inputMap);
		} else {
			return null;
		}
	}

	private ResponseEntity<?> isVerified(JSONObject resp, Map<String, Object> inputMap) throws Exception {
		if (resp.containsKey("pwd")) {
			if (Validator.hasData(resp.get("pwd").toString())) {
				return getQRCode(resp);
			} else {
				ResponseEntity<String> secretKey = generateSecretKey(inputMap);
				return studyResponse(secretKey, inputMap);
			}
		} else {
			ResponseEntity<String> secretKey = generateSecretKey(inputMap);
			return studyResponse(secretKey, inputMap);
		}
	}

	public ResponseEntity<String> generateSecretKey(Map<String, Object> inputMap) {
		try {
			ResponseEntity<String> output = kambalaUtil.generateSecretKey(inputMap);
			return output;
		} catch (HttpClientErrorException e) {
			return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
		}
	}

	public ResponseEntity<String> getQRCode(JSONObject output) {
		HttpHeaders headers = new HttpHeaders();
		try {
			String uri = generateInputs(output);
			Map<EncodeHintType, Object> hints = new HashMap<>();
	        hints.put(EncodeHintType.MARGIN, 1);
	        BitMatrix matrix = new QRCodeWriter().encode(uri, BarcodeFormat.QR_CODE, 150, 150, hints);
			BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, Constants.PNG, baos);
			byte[] imageBytes = baos.toByteArray();
			String base64Image = Base64Utils.encodeToString(imageBytes);
			return ResponseEntity.ok(Constants.PNG_IMG_FORMAT + base64Image);
		} catch (Exception e) {
			headers.setContentType(MediaType.TEXT_PLAIN);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while generating the QR code.");
		}
	}

	public String generateInputs(JSONObject output) throws UnsupportedEncodingException {
		String user_id = (String) output.get("uid");
		String password = (String) output.get("pwd");
		String baseData = "otpauth://totp/Orca:" + user_id + "?secret=" + password + "&issuer=Orca";
		return baseData;
	}
}
