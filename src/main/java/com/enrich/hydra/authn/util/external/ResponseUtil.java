package com.enrich.hydra.authn.util.external;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Configuration;

import com.enrich.hydra.authn.constant.Constants;
import com.enrich.hydra.authn.pojo.kambala.AuthenticateKambalaResponse;
import com.enrich.hydra.authn.pojo.kambala.ChangePasswordKambalaResponse;
import com.enrich.hydra.authn.pojo.kambala.ForgotPasswordKambalaResponse;
import com.enrich.hydra.authn.pojo.kambala.GenerateOtpKambalaResponse;
import com.enrich.hydra.authn.pojo.kambala.LoginKambalaResponse;
import com.enrich.hydra.authn.pojo.kambala.LogoutKambalaResponse;
import com.enrich.hydra.authn.pojo.user.AuthenticateUserResponse;
import com.enrich.hydra.authn.pojo.user.ChangePasswordUserResponse;
import com.enrich.hydra.authn.pojo.user.ForgotPasswordUserResponse;
import com.enrich.hydra.authn.pojo.user.GenerateOtpUserResponse;
import com.enrich.hydra.authn.pojo.user.LoginUserResponse;
import com.enrich.hydra.authn.pojo.user.LogoutUserResponse;
import com.enrich.hydra.authn.util.CommonUtil;
import com.enrich.hydra.authn.util.Validator;

@Configuration
public class ResponseUtil {

	public static AuthenticateUserResponse standardizeAuthenticateResponse(AuthenticateKambalaResponse responseInfo)
			throws ParseException {
		AuthenticateUserResponse authenticateResponse = new AuthenticateUserResponse();
		standardizeAuthenticateProperties(authenticateResponse, responseInfo);
		if (Validator.hasData(responseInfo.getMws())) {
			AuthenticateUserResponse.MarketWatchList marketWatchList = buildMarketWatchList(responseInfo.getMws());
			authenticateResponse.setMarket_watch_list(marketWatchList);
		}
		return authenticateResponse;
	}

	private static void standardizeAuthenticateProperties(AuthenticateUserResponse authenticateResponse,
			AuthenticateKambalaResponse responseInfo) throws ParseException {
		if (Validator.hasData(responseInfo.getPrarr())) {
			authenticateResponse.setProduct_list(standardizeAuthenticateProducts(responseInfo.getPrarr()));
		}
		String formattedDate = CommonUtil.formatDateTime(responseInfo.getRequest_time(), Constants.HOUR_TO_DATE,
				Constants.DATE_TO_HOUR);
		setData(authenticateResponse, responseInfo, formattedDate);
		authenticateResponse.setValues(responseInfo.getValues());
		authenticateResponse.setBroker_name(responseInfo.getBroker_name());
		authenticateResponse.setError_message(responseInfo.getEmsg());
		authenticateResponse.setLast_access_date_time(responseInfo.getLast_access_date_time());
	}

	private static void setData(AuthenticateUserResponse authenticateResponse, AuthenticateKambalaResponse responseInfo,
			String formattedDate) {
		authenticateResponse.setRequest_date_time(formattedDate);
		authenticateResponse.setAccount_id(responseInfo.getActid());
		authenticateResponse.setUser_name(responseInfo.getUname());
		authenticateResponse.setStatus(responseInfo.getStat());
		authenticateResponse.setUser_token(responseInfo.getSusertoken());
		authenticateResponse.setEmail(responseInfo.getEmail());
		authenticateResponse.setUser_id(responseInfo.getUid());
		authenticateResponse.setBranch_id(responseInfo.getBrnchid());
		authenticateResponse.setTotp_status(responseInfo.getTotp_set());
		authenticateResponse.setOrder_list(responseInfo.getOrarr());
		authenticateResponse.setExchange_list(responseInfo.getExarr());
	}

	private static AuthenticateUserResponse.MarketWatchList buildMarketWatchList(
			Map<String, List<AuthenticateKambalaResponse.MarketWatchItem>> marketWatchItems) {
		AuthenticateUserResponse.MarketWatchList marketWatchList = new AuthenticateUserResponse.MarketWatchList();
		Map<String, List<AuthenticateUserResponse.MarketWatchItem>> marketWatchItemMap = new HashMap<>();
		for (Map.Entry<String, List<AuthenticateKambalaResponse.MarketWatchItem>> entry : marketWatchItems.entrySet()) {
			String key = entry.getKey();
			List<AuthenticateUserResponse.MarketWatchItem> itemList = new ArrayList<>();
			for (AuthenticateKambalaResponse.MarketWatchItem kambalaItem : entry.getValue()) {
				AuthenticateUserResponse.MarketWatchItem item = new AuthenticateUserResponse.MarketWatchItem();
				item.setExchange(kambalaItem.getExch());
				item.setToken(kambalaItem.getToken());
				item.setTrading_symbol(kambalaItem.getTsym());
				item.setWeekly(kambalaItem.getWeekly());
				item.setInstrument_name(kambalaItem.getInstname());
				item.setPrice_precision(kambalaItem.getPp());
				item.setLot_size(kambalaItem.getLs());
				item.setTick_size(kambalaItem.getTi());
				item.setNon_tradable(kambalaItem.getNontrd());
				item.setOption_type(kambalaItem.getOptt());
				itemList.add(item);
			}
			marketWatchItemMap.put(key, itemList);
		}
		marketWatchList.setMarket_watch_item(marketWatchItemMap);
		return marketWatchList;
	}

	private static List<AuthenticateUserResponse.ProductInfo> standardizeAuthenticateProducts(
			List<AuthenticateKambalaResponse.ProductInfo> kambalaProducts) {
		List<AuthenticateUserResponse.ProductInfo> userProducts = new ArrayList<>();
		if (Validator.hasData(kambalaProducts)) {
			for (AuthenticateKambalaResponse.ProductInfo kambalaProduct : kambalaProducts) {
				AuthenticateUserResponse.ProductInfo userProduct = new AuthenticateUserResponse.ProductInfo();
				userProduct.setProduct(kambalaProduct.getPrd());
				userProduct.setExchange(kambalaProduct.getExch());
				userProduct.setProduct_display_name(kambalaProduct.getS_prdt_ali());
				userProducts.add(userProduct);
			}
		}
		return userProducts;
	}

	public static LoginUserResponse standardizeLoginResponse(LoginKambalaResponse responseInfo) throws ParseException {
		LoginUserResponse userResponse = new LoginUserResponse();
		userResponse.setAccount_id(responseInfo.getActid());
		userResponse.setAccess_type(responseInfo.getAccess_type());
		setLoginData(responseInfo, userResponse);
		if (Validator.hasData(responseInfo.getPrarr())) {
			userResponse.setMarket_watch_list(mapMarketWatchList(responseInfo.getMws()));
			userResponse.setProduct_list(mapProductInfoList(responseInfo.getPrarr()));
		}
		String formattedDate = CommonUtil.formatDateTime(responseInfo.getRequest_time(), Constants.HOUR_TO_DATE,
				Constants.DATE_TO_HOUR);
		userResponse.setRequest_date_time(formattedDate);
		userResponse.setLast_access_date_time(responseInfo.getLastaccesstime());
		return userResponse;
	}

	private static void setLoginData(LoginKambalaResponse responseInfo, LoginUserResponse userResponse) {
		userResponse.setUser_name(responseInfo.getUname());
		userResponse.setStatus(responseInfo.getStat());
		userResponse.setUser_token(responseInfo.getSusertoken());
		userResponse.setEmail(responseInfo.getEmail());
		userResponse.setUser_id(responseInfo.getUid());
		userResponse.setBranch_id(responseInfo.getBrnchid());
		userResponse.setTotp_status(responseInfo.getTotp_set());
		userResponse.setOrder_list(responseInfo.getOrarr());
		userResponse.setExchange_list(responseInfo.getExarr());
		userResponse.setValues(responseInfo.getValues());
		userResponse.setBroker_name(responseInfo.getBrkname());
		userResponse.setError_message(responseInfo.getEmsg());
	}

	private static List<LoginUserResponse.ProductInfo> mapProductInfoList(
			List<LoginKambalaResponse.ProductInfo> kambalaProducts) {
		List<LoginUserResponse.ProductInfo> userProducts = new ArrayList<>();
		if (Validator.hasData(kambalaProducts)) {
			for (LoginKambalaResponse.ProductInfo kambalaProduct : kambalaProducts) {
				LoginUserResponse.ProductInfo userProduct = new LoginUserResponse.ProductInfo();
				userProduct.setProduct(kambalaProduct.getPrd());
				userProduct.setProduct_display_name(kambalaProduct.getS_prdt_ali());
				userProduct.setExchange(kambalaProduct.getExch());
				userProducts.add(userProduct);
			}
		}
		return userProducts;
	}

	private static Map<String, List<LoginUserResponse.MarketWatchItem>> mapMarketWatchList(
			Map<String, List<LoginKambalaResponse.MarketWatchItem>> marketWatchItems) {
		Map<String, List<LoginUserResponse.MarketWatchItem>> userMarketWatchList = new HashMap<>();
		if (Validator.hasData(marketWatchItems)) {
			for (Map.Entry<String, List<LoginKambalaResponse.MarketWatchItem>> entry : marketWatchItems.entrySet()) {
				String key = entry.getKey();
				List<LoginUserResponse.MarketWatchItem> itemList = new ArrayList<>();
				for (LoginKambalaResponse.MarketWatchItem kambalaItem : entry.getValue()) {
					LoginUserResponse.MarketWatchItem item = new LoginUserResponse.MarketWatchItem();
					item.setExchange(kambalaItem.getExch());
					item.setToken(kambalaItem.getToken());
					item.setTrading_symbol(kambalaItem.getTsym());
					item.setWeekly(kambalaItem.getWeekly());
					item.setInstrument_name(kambalaItem.getInstname());
					item.setPrice_precision(kambalaItem.getPp());
					item.setLot_size(kambalaItem.getLs());
					item.setTick_size(kambalaItem.getTi());
					item.setNon_tradable(kambalaItem.getNontrd());
					item.setOption_type(kambalaItem.getOptt());
					itemList.add(item);
				}
				userMarketWatchList.put(key, itemList);
			}
		}
		return userMarketWatchList;
	}

	public static LogoutUserResponse createLogoutUserResponse(LogoutKambalaResponse responseInfo)
			throws ParseException {
		LogoutUserResponse userResponse = new LogoutUserResponse();
		userResponse.setStatus(responseInfo.getStat());
		String formattedDate = CommonUtil.formatDateTime(responseInfo.getRequest_time(), Constants.HOUR_TO_DATE,
				Constants.DATE_TO_HOUR);
		userResponse.setRequest_date_time(formattedDate);
		userResponse.setError_message(responseInfo.getEmsg());
		return userResponse;
	}

	public static ChangePasswordUserResponse createChangePasswordResponse(ChangePasswordKambalaResponse responseInfo)
			throws ParseException {
		ChangePasswordUserResponse userResponse = new ChangePasswordUserResponse();
		userResponse.setStatus(responseInfo.getStat());
		userResponse.setExpiry_message(responseInfo.getDmsg());
		String formattedDate = CommonUtil.formatDateTime(responseInfo.getRequest_time(), Constants.HOUR_TO_DATE,
				Constants.DATE_TO_HOUR);
		userResponse.setRequest_date_time(formattedDate);
		userResponse.setError_message(responseInfo.getEmsg());
		return userResponse;
	}

	public static ForgotPasswordUserResponse createForgotPasswordResponse(ForgotPasswordKambalaResponse responseInfo)
			throws ParseException {
		ForgotPasswordUserResponse userResponse = new ForgotPasswordUserResponse();
		userResponse.setStatus(responseInfo.getStat());
		userResponse.setError_message(responseInfo.getEmsg());
		String formattedDate = CommonUtil.formatDateTime(responseInfo.getRequest_time(), Constants.HOUR_TO_DATE,
				Constants.DATE_TO_HOUR);
		userResponse.setRequest_date_time(formattedDate);
		return userResponse;
	}

	public static GenerateOtpUserResponse createGenerateOtpResponse(GenerateOtpKambalaResponse responseInfo)
			throws ParseException {
		GenerateOtpUserResponse userResponse = new GenerateOtpUserResponse();
		userResponse.setStatus(responseInfo.getReqStatus());
		userResponse.setUser_id(responseInfo.getUid());
		return userResponse;
	}
}
