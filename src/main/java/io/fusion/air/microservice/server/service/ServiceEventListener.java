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
package io.fusion.air.microservice.server.service;
 
import io.fusion.air.microservice.security.JsonWebToken;
import io.fusion.air.microservice.server.config.ServiceConfiguration;
import io.fusion.air.microservice.server.config.ServiceHelp;

import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import io.fusion.air.microservice.utils.CPU;

//Logging System
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

import static java.lang.invoke.MethodHandles.lookup;

/**
 * 
 * @author arafkarsh
 * @version 1.0
 */
@Configuration
public class ServiceEventListener {

	// Set Logger -> Lookup will automatically determine the class name.
	private static final Logger log = getLogger(lookup().lookupClass());
	
	@Autowired
	private ServiceConfiguration serviceConfig;

	@Autowired
	JsonWebToken jsonWebToken;

	@Value("${server.token.test}")
	private boolean serverTokenTest;

	// server.token.auth.expiry=300000
	@Value("${server.token.auth.expiry:300000}")
	private long tokenAuthExpiry;

	// server.token.refresh.expiry=1800000
	@Value("${server.token.refresh.expiry:1800000}")
	private long tokenRefreshExpiry;

	/**
	 * Shows Logo and Generate Test Tokens
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		log.info("Service is getting ready. Getting the CPU Stats ... ");
	    log.info(CPU.printCpuStats());
	    // System.out.println(LocalDateTime.now()+"|Service is getting ready...... ");
	    // System.out.println(LocalDateTime.now()+"|"+CPU.printCpuStats());
		showLogo();
		log.info("Generate Test Tokens = {} ", serverTokenTest);
		if(serverTokenTest) {
			generateTestToken();
		}
	}

	/**
	 * Generate Tokens for Testing Purpose Only
	 * Token 			= Expires in 5 Mins
	 * Refresh Token 	= Expires in 30 Mins
	 * This shld be disabled in Production Environment
	 * serverTestToken=false
	 */
	private void generateTestToken() {
		tokenAuthExpiry = (tokenAuthExpiry < 10) ? JsonWebToken.EXPIRE_IN_FIVE_MINS : tokenAuthExpiry;
		tokenRefreshExpiry = (tokenRefreshExpiry < 10) ? JsonWebToken.EXPIRE_IN_THIRTY_MINS : tokenRefreshExpiry;

		String subject	 = "jane.doe";
		String issuer    = serviceConfig.getServiceOrg();

		Map<String, Object> claims = new HashMap<>();
		claims.put("aud", serviceConfig.getServiceName());
		claims.put("jti", UUID.randomUUID().toString());
		claims.put("rol", "User");
		claims.put("did", "Device ID");
		claims.put("iss", issuer);
		claims.put("sub", subject);

		HashMap<String,String> tokens = jsonWebToken
										.setSubject(subject)
										.setIssuer(issuer)
										.setTokenAuthExpiry(tokenAuthExpiry)
										.setTokenRefreshExpiry(tokenRefreshExpiry)
										.addAllTokenClaims(claims)
										.addAllRefreshTokenClaims(claims)
										.generateTokens();

		String token = tokens.get("token");
		String refresh = tokens.get("refresh");
		log.info("Token Expiry in Days:or:Hours:or:Mins  {}:{}:{} ", JsonWebToken.getDays(tokenAuthExpiry),
				JsonWebToken.getHours(tokenAuthExpiry),  JsonWebToken.getMins(tokenAuthExpiry) );
		jsonWebToken.tokenStats(token, false, false);

		log.info("Refresh Token Expiry in Days:or:Hours:or:Mins  {}:{}:{} ", JsonWebToken.getDays(tokenRefreshExpiry),
				JsonWebToken.getHours(tokenRefreshExpiry),  JsonWebToken.getMins(tokenRefreshExpiry) );
		jsonWebToken.tokenStats(refresh, false, false);
	}
	
	/**
	 * Shows the Service Logo and Version Details. 
	 */
	public void showLogo() {
		String version="v0.1.0", name="NoName";
		if(serviceConfig != null) {
			version = serviceConfig.getServerVersion();
			name =serviceConfig.getServiceName();
		}
		MDC.put("Service", name);
		String logo =ServiceHelp.LOGO.replaceAll("SIGMA", name).replaceAll("VERSION", version);
		log.info(name+" Service is ready! ... .."
				+ logo
				+ "Build No. = "+serviceConfig.getBuildNumber()
				+ " :: Build Date = "+serviceConfig.getBuildDate()
				+ " :: Restart = "+ServiceHelp.getCounter() 
				+ ServiceHelp.NL + ServiceHelp.DL
				+ ServiceHelp.NL + "API URL : " + serviceConfig.apiURL()
				+ ServiceHelp.NL + ServiceHelp.DL
				);
	}
}