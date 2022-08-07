/**
 * (C) Copyright 2021 Araf Karsh Hamid 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.fusion.air.microservice.utils;

import java.util.LinkedHashMap;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import io.fusion.air.microservice.domain.models.StandardResponse;
import io.fusion.air.microservice.server.config.ServiceConfiguration;
import org.slf4j.MDC;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author arafkarsh
 *
 */
public final class Utils {
	
	/***
	 * 
	 * @param _object
	 * @return
	 */
	public static String toJsonString(Object _object) {
		if(_object == null) {
			return "";
		}
		try {
			return new ObjectMapper()
					.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
					.findAndRegisterModules()
					.writeValueAsString(_object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * String Utilities
	 * 
	 * @author arafkarsh
	 *
	 */
	public static class Strings {

		/**
		 * Returns True if the String is NULL or Empty
		 * 
		 * @param input
		 * @return
		 */
	    public static boolean isBlank(String input) {
	        return input == null || input.trim().isEmpty();
	    }
	    
	    public static Stream<String> blankStrings() {
	        return Stream.of(null, "", "  ");
	    }
	}
	
	/**
	 * Number Utility
	 * 
	 * @author arafkarsh
	 *
	 */
	public static class Numbers {
		
		/**
		 * Returns True if the Number is an Odd Number
		 * @param number
		 * @return
		 */
	    public static boolean isOdd(int number) {
	        return number % 2 != 0;
	    }
	}

	/**
	 * Create Cookie
	 * @param request
	 * @param _key
	 * @param _value
	 * @return
	 */
	public static Cookie createCookie(HttpServletRequest request, String _key, String _value) {
		Cookie c = new Cookie(_key, _value);
		// c.setDomain(serviceConfig.getServerHost());
		c.setSecure(true);
		c.setHttpOnly(true);
		// c.setMaxAge((int)JsonWebToken.EXPIRE_IN_EIGHT_HOUR);
		// c.setPath(request.getRequestURI());
		return c;
	}

	/**
	 * Create Cookie
	 * @param request
	 * @param _key
	 * @param _value
	 * @param _age
	 * @return
	 */
	public static Cookie createCookie(HttpServletRequest request, String _key, String _value, int _age) {
		Cookie c = new Cookie(_key, _value);
		// c.setDomain(serviceConfig.getServerHost());
		c.setSecure(true);
		c.setHttpOnly(true);
		c.setMaxAge(_age);
		// c.setPath(request.getRequestURI());
		return c;
	}

	/**
	 * Create Standard Error Response
	 * @param _inputErrors
	 * @param servicePrefix
	 * @param _httpStatus
	 * @param _errorCode
	 * @param _message
	 * @return
	 */
	public static StandardResponse createErrorResponse(Object _inputErrors, String servicePrefix,
							HttpStatus _httpStatus, String _errorCode, String _message) {

		// Initialize Standard Error Response
		StandardResponse stdResponse = new StandardResponse();
		stdResponse.initFailure(servicePrefix + _errorCode, _message);
		LinkedHashMap<String, Object> payload = new LinkedHashMap<String,Object>();

		// Add Input Errors If Available
		if(_inputErrors != null) {
			payload.put("input", _inputErrors);
		}

		// Add Error Details
		LinkedHashMap<String,Object> errorData = new LinkedHashMap<String,Object>();
		errorData.put("code", _httpStatus.value());
		errorData.put("mesg", _httpStatus.name());
		errorData.put("srv", MDC.get("Service"));
		errorData.put("reqId", MDC.get("ReqId"));
		errorData.put("http", MDC.get("Protocol"));
		errorData.put("path", MDC.get("URI"));
		payload.put("errors", errorData);
		stdResponse.setPayload(payload);

		return stdResponse;
	}

	/**
	 * For Testing ONLY
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		System.out.println("Utils.toJsonString() = "+Utils.toJsonString(new ServiceConfiguration("localhost", 9090)));
	}
}
