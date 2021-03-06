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
import io.fusion.air.microservice.domain.models.*;
import io.fusion.air.microservice.domain.ports.CountryService;
import io.fusion.air.microservice.server.config.ServiceConfiguration;
import io.fusion.air.microservice.server.controllers.AbstractController;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Product Controller for the Service
 *
 * Only Selected Methods will be secured in this packaged - which are Annotated with
 * @AuthorizationRequired
 * @Operation(summary = "Cancel Product", security = { @SecurityRequirement(name = "bearer-key") })
 * 
 * @author arafkarsh
 * @version 1.0
 * 
 */
@Validated
@CrossOrigin
@Configuration
@RestController
// "/ms-cache/api/v1"
@RequestMapping("${service.api.path}/product")
@RequestScope
@Tag(name = "Product API", description = "Ex. io.f.a.m.adapters.controllers.ProductControllerImpl")
public class ProductControllerImpl extends AbstractController {

	// Set Logger -> Lookup will automatically determine the class name.
	private static final Logger log = getLogger(lookup().lookupClass());
	
	@Autowired
	private ServiceConfiguration serviceConfig;
	private String serviceName;

	@Autowired
	CountryService countryServiceImpl;

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
	public ResponseEntity<StandardResponse> getStatus(@PathVariable("referenceNo") String _referenceNo,
														HttpServletRequest request,
														HttpServletResponse response) throws Exception {
		log.info("|"+name()+"|Request to Product Status of Service... ");
		response.setHeader("Cache-Control", "no-cache");

		StandardResponse stdResponse = new StandardResponse();
		stdResponse.init(true, "200", "Data Fetch Success!");
		HashMap<String,Object> status = new HashMap<String,Object>();
		status.put("ReferenceNo", _referenceNo);
		status.put("Message","Product Status is good!");
		stdResponse.setPayload(status);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * Get Method Call to Check the Health of the App
	 *
	 * @return
	 */
	@Operation(summary = "Get All the Products")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "List All the Product",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "404",
					description = "Invalid Product Reference No.",
					content = @Content)
	})
	@GetMapping("/all/")
	@ResponseBody
	public ResponseEntity<StandardResponse> getAllProducts(HttpServletRequest request,
													  HttpServletResponse response) throws Exception {
		log.info("|"+name()+"|Request to Product Status of Service... ");
		response.setHeader("Cache-Control", "no-cache");

		StandardResponse stdResponse = new StandardResponse();
		stdResponse.init(true, "200", "Data Fetch Success!");

		ArrayList<String> products = new ArrayList<String>();
		products.add("iPhone 11");
		products.add("iPhone 12");
		products.add("iPhone 13");
		stdResponse.setPayload(products);
		return ResponseEntity.ok(stdResponse);
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
    public ResponseEntity<StandardResponse> processPayments(@RequestBody PaymentDetails _payDetails) {
		log.info("|"+name()+"|Request to process Product... "+_payDetails);
		StandardResponse stdResponse = new StandardResponse();
		stdResponse.init(true, "200", "Processing Success!");
		PaymentStatus ps = new PaymentStatus(
				"fb908151-d249-4d30-a6a1-4705729394f4",
				LocalDateTime.now(),
				"Accepted",
				UUID.randomUUID().toString(),
				LocalDateTime.now(),
				PaymentType.CREDIT_CARD);
		stdResponse.setPayload(ps);
		return ResponseEntity.ok(stdResponse);
    }

	/**
	 * Create the Product
	 */
	@Operation(summary = "Create Product")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Create the Product",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "404",
					description = "Unable to Create the Product",
					content = @Content)
	})
	@PostMapping("/create")
	public ResponseEntity<StandardResponse> createProduct(@Valid @RequestBody Product _product) {
		log.info("|"+name()+"|Request to Create Product... "+_product);
		StandardResponse stdResponse = new StandardResponse();
		stdResponse.init(true, "200", "Product Created");
		stdResponse.setPayload(_product);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * Cancel the Product
	 */
	@AuthorizationRequired(role = "Admin")
	@Operation(summary = "Cancel Product", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Payment Cancelled",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "404",
					description = "Unable to Cancel the Payment",
					content = @Content)
	})
	@DeleteMapping("/cancel/{referenceNo}")
	public ResponseEntity<StandardResponse> cancel(@PathVariable("referenceNo") String _referenceNo) {
		log.info("|"+name()+"|Request to Cancel the Product... ");
		StandardResponse stdResponse = new StandardResponse();
		stdResponse.init(true, "200", "Cancelled!");
		HashMap<String,Object> status = new HashMap<String,Object>();
		status.put("ReferenceNo", _referenceNo);
		status.put("Message","Payment cancelled!");
		stdResponse.setPayload(status);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * Update the Product
	 */
	@AuthorizationRequired(role = "User")
	@Operation(summary = "Update Product", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Update the Product",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "404",
					description = "Unable to Update the Product",
					content = @Content)
	})
	@PutMapping("/update/{referenceNo}")
	public ResponseEntity<StandardResponse> updatePayment(@PathVariable("referenceNo") String _referenceNo) {
		log.info("|"+name()+"|Request to Update Product... "+_referenceNo);
		StandardResponse stdResponse = new StandardResponse();
		stdResponse.init(true, "200", "Updated!");
		HashMap<String,Object> status = new HashMap<String,Object>();
		status.put("ReferenceNo", _referenceNo);
		status.put("Message","Product updated!");
		stdResponse.setPayload(status);
		return ResponseEntity.ok(stdResponse);
	}
 }