package com.enrich.hydra.authn.util;

import java.util.List;

public class Validator {

	public static boolean hasData(String data) {
		if (data != null && !data.isEmpty() && !data.isBlank()) {
			return true;
		}
		return false;
	}
	
	public static boolean hasData(Object data) {
		if (data != null) {
			return true;
		}
		return false;
	}

	public static boolean hasData(List<?> data) {
		if (data != null && data.size() > 0) {
			return true;
		}
		return false;
	}

}
