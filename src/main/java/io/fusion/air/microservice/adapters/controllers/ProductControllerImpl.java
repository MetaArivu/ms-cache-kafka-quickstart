/**
 * (C) Copyright 2022 Araf Karsh Hamid
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
package io.fusion.air.microservice.adapters.controllers;

import io.fusion.air.microservice.adapters.security.AuthorizationRequired;
import io.fusion.air.microservice.domain.models.PaymentDetails;
import io.fusion.air.microservice.domain.models.PaymentStatus;
import io.fusion.air.microservice.domain.models.PaymentType;
import io.fusion.air.microservice.server.config.ServiceConfiguration;
import io.fusion.air.microservice.server.controllers.AbstractController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Product Controller for the Service
 * 
 * @author arafkarsh
 * @version 1.0
 * 
 */
@Configuration
@RestController
// "/api/v1/ms-cache"
@RequestMapping("${service.api.path}/product")
@RequestScope
@Tag(name = "Product API", description = "Ex. io.f.a.m.adapters.controllers.ProductControllerImpl")
public class ProductControllerImpl extends AbstractController {

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
    @Operation(summary = "Get the Product status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Product Status Check",
            content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
            description = "Invalid Product Reference No.",
            content = @Content)
    })
	@GetMapping("/status/{referenceNo}")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> getStatus(@PathVariable("referenceNo") String _referenceNo,
														HttpServletRequest request,
														HttpServletResponse response) throws Exception {
		log.info("|"+name()+"|Request to Product Status of Service... ");
		response.setHeader("Cache-Control", "no-cache");
		HashMap<String,Object> status = new HashMap<String,Object>();
		status.put("Code", 200);
		status.put("Status", true);
		status.put("ReferenceNo", _referenceNo);
		status.put("Message","Product Status is good!");
		return ResponseEntity.ok(status);
	}

	/**
	 * Process the Product
	 */
    @Operation(summary = "Process Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Process the payment",
            content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
            description = "Unable to process the payment",
            content = @Content)
    })
    @PostMapping("/processProducts")
    public ResponseEntity<PaymentStatus> processPayments(@RequestBody PaymentDetails _payDetails) {
		log.info("|"+name()+"|Request to process Product... ");
		PaymentStatus ps = new PaymentStatus(
				"fb908151-d249-4d30-a6a1-4705729394f4",
				LocalDateTime.now(),
				"Accepted",
				UUID.randomUUID().toString(),
				LocalDateTime.now(),
				PaymentType.CREDIT_CARD);
		return ResponseEntity.ok(ps);
    }

	/**
	 * Cancel the Product
	 */
	@Operation(summary = "Cancel Product")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Payment Cancelled",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "404",
					description = "Unable to Cancel the Payment",
					content = @Content)
	})
	@AuthorizationRequired(role = "Admin")
	@DeleteMapping("/cancel/{referenceNo}")
	public ResponseEntity<HashMap<String,Object>> cancel(@PathVariable("referenceNo") String _referenceNo) {
		log.info("|"+name()+"|Request to Cancel the Product... ");
		HashMap<String,Object> status = new HashMap<String,Object>();
		status.put("Code", 200);
		status.put("Status", true);
		status.put("ReferenceNo", _referenceNo);
		status.put("Message","Payment cancelled!");
		return ResponseEntity.ok(status);
	}

	/**
	 * Update the Product
	 */
	@Operation(summary = "Update Product")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Update the Product",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "404",
					description = "Unable to Update the Product",
					content = @Content)
	})
	@AuthorizationRequired(role = "User")
	@PutMapping("/update/{referenceNo}")
	public ResponseEntity<HashMap<String,Object>> updatePayment(@PathVariable("referenceNo") String _referenceNo) {
		log.info("|"+name()+"|Request to Update Product... "+_referenceNo);
		HashMap<String,Object> status = new HashMap<String,Object>();
		status.put("Code", 200);
		status.put("Status", true);
		status.put("ReferenceNo", _referenceNo);
		status.put("Message","Product updated!");
		return ResponseEntity.ok(status);
	}
 }