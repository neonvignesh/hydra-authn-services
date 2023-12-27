package com.enrich.hydra.authn.pojo.user;

import java.util.List;
import java.util.Map;

import com.enrich.hydra.authn.common.ApiResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticateUserResponse implements ApiResponse {

    private String request_date_time;
    private String account_id;
    private String user_name;
    private List<ProductInfo> product_list;
    private String status;
    private String user_token;
    private String email;
    private String user_id;
    private String branch_id;
    private String totp_status;
    private List<String> order_list;
    private List<String> exchange_list;
    private List<String> values;
    private MarketWatchList market_watch_list;
    private String broker_name;
    private String error_message;
    private String last_access_date_time;

@Getter
@Setter
public static class ProductInfo {
    private String product;
    private String product_display_name;
    private List<String> exchange;
}

@Getter
@Setter
public static class MarketWatchList {
    private Map<String, List<MarketWatchItem>> market_watch_item;
}

@Getter
@Setter
public static class MarketWatchItem {
	private List<String> exchange;
    private String token;
    private String trading_symbol;
    private String weekly;
    private String instrument_name;
    private String price_precision;
    private String lot_size;
    private String tick_size;
    private String option_type;
    private String non_tradable;
}


}
