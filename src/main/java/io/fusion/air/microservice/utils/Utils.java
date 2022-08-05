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

import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fusion.air.microservice.server.config.ServiceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

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

	public static Cookie createCookie(HttpServletRequest request, String _key, String _value) {
		Cookie c = new Cookie(_key, _value);
		// c.setDomain(serviceConfig.getServerHost());
		c.setSecure(true);
		c.setHttpOnly(true);
		// c.setMaxAge((int)JsonWebToken.EXPIRE_IN_EIGHT_HOUR);
		// c.setPath(request.getRequestURI());
		return c;
	}

	public static Cookie createCookie(HttpServletRequest request, String _key, String _value, int _age) {
		Cookie c = new Cookie(_key, _value);
		// c.setDomain(serviceConfig.getServerHost());
		c.setSecure(true);
		c.setHttpOnly(true);
		c.setMaxAge(_age);
		// c.setPath(request.getRequestURI());
		return c;
	}

	public static void main(String[] args) throws Exception {

		System.out.println("Utils.toJsonString() = "+Utils.toJsonString(new ServiceConfiguration("localhost", 9090)));
	}
}
