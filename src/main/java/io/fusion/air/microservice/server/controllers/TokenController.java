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
package io.fusion.air.microservice.server.controllers;

import io.fusion.air.microservice.ServiceBootStrap;
import io.fusion.air.microservice.adapters.security.AuthorizationRequired;
import io.fusion.air.microservice.domain.exceptions.InputDataException;
import io.fusion.air.microservice.domain.models.StandardResponse;
import io.fusion.air.microservice.server.config.ServiceConfiguration;
import io.fusion.air.microservice.server.config.ServiceHelp;
import io.fusion.air.microservice.server.models.EchoData;
import io.fusion.air.microservice.server.models.EchoResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Refresh Token Controller for the Service
 * Generates the following
 * 1. Auth Token
 * 2. Refresh Token
 * 
 * @author arafkarsh
 * @version 1.0
 * 
 */
@Configuration
@RestController
// "/service-name/api/v1/tokens
@RequestMapping("${service.api.path}/tokens")
@RequestScope
@Tag(name = "System", description = "Token (Auth Token, Refresh Tokens")
public class TokenController extends AbstractController {

	// Set Logger -> Lookup will automatically determine the class name.
	private static final Logger log = getLogger(lookup().lookupClass());

	@Autowired
	private ServiceConfiguration serviceConfig;
	private String serviceName;

	/**
	 * Get Method Call to Check the Health of the App
	 * 
	 * @return
	 */
	@AuthorizationRequired(role = "User")
	@Operation(summary = "Generate Tokens", security = { @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Tokens Generated",
            content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
            description = "Token Generation Failed!",
            content = @Content)
    })
	@GetMapping("/generate")
	@ResponseBody
	public ResponseEntity<StandardResponse> generate(HttpServletRequest request) throws Exception {
		log.debug(name()+"|Request to Generate Tokens... ");
		StandardResponse stdResponse = createSuccessResponse("Tokens Generated!");
		return ResponseEntity.ok(stdResponse);
	}

 }

