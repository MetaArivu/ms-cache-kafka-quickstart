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
package io.fusion.air.microservice.adapters.controllers.secured;

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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Payment Controller (Secured) for the Service
 *
 * All the calls in this package will be secured with JWT Token.
 * 
 * @author arafkarsh
 * @version 1.0
 * 
 */
@CrossOrigin
@Configuration
@RestController
// "/ms-cache/api/v1"
@RequestMapping("${service.api.path}/payment")
@RequestScope
@Tag(name = "Payments API", description = "Ex. io.f.a.m.adapters.controllers.secured.PaymentControllerImpl")
public class PaymentControllerImpl extends AbstractController {

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
    @Operation(summary = "Check the Payment status", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Payment Status Check",
            content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
            description = "Invalid Payment Reference No.",
            content = @Content)
    })
	@GetMapping("/status/{referenceNo}")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> getStatus(@PathVariable("referenceNo") String _referenceNo,
														HttpServletRequest request) throws Exception {
		log.info("|"+name()+"|Request to Payment Status of Service... ");
		// Response Object
		HashMap<String,Object> status = new HashMap<String,Object>();
		status.put("Code", 200);
		status.put("Status", true);
		status.put("ReferenceNo", _referenceNo);
		status.put("Message","Payment Status is good!");
		// Additional Headers
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");
		// Return the Response
		return new ResponseEntity<Map<String,Object>>(status, headers, HttpStatus.OK);
	}

	/**
	 * Process the Payments
	 */
    @Operation(summary = "Process Payments", security = { @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Process the payment",
            content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
            description = "Unable to process the payment",
            content = @Content)
    })
    @PostMapping("/processPayments")
    public ResponseEntity<PaymentStatus> processPayments(@RequestBody PaymentDetails _payDetails) {
		log.info("|"+name()+"|Request to process payments... ");
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
	 * Cancel the Payment
	 */
	@Operation(summary = "Cancel Payment", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Payment Cancelled",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "404",
					description = "Unable to Cancel the Payment",
					content = @Content)
	})
	@DeleteMapping("/cancel/{referenceNo}")
	public ResponseEntity<HashMap<String,Object>> cancel(@PathVariable("referenceNo") String _referenceNo) {
		log.info("|"+name()+"|Request to Cancel the payments... ");
		HashMap<String,Object> status = new HashMap<String,Object>();
		status.put("Code", 200);
		status.put("Status", true);
		status.put("ReferenceNo", _referenceNo);
		status.put("Message","Payment cancelled!");
		return ResponseEntity.ok(status);
	}

	/**
	 * Update the Payment
	 */
	@Operation(summary = "Update Payment", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Update the Payment",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "404",
					description = "Unable to Update the Payment",
					content = @Content)
	})
	@PutMapping("/update/{referenceNo}")
	public ResponseEntity<HashMap<String,Object>> updatePayment(@PathVariable("referenceNo") String _referenceNo) {
		log.info("|"+name()+"|Request to Update Payment... "+_referenceNo);
		HashMap<String,Object> status = new HashMap<String,Object>();
		status.put("Code", 200);
		status.put("Status", true);
		status.put("ReferenceNo", _referenceNo);
		status.put("Message","Payment updated!");
		return ResponseEntity.ok(status);
	}
 }