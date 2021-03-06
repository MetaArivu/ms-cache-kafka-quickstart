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
package io.fusion.air.microservice.domain.models;


import io.fusion.air.microservice.utils.Utils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class Product implements Serializable {

    @NotBlank(message = "The Product ID is  required.")
    private String productId;

    @NotBlank(message = "The Product Name is  required.")
    @Size(min = 3, max = 32, message = "The length of Product Name must be between 3 and 32 characters.")
    private String productName;

    @Size(min = 0, max = 128, message = "The length of Product Details must be between 0 and 128 characters.")
    private String productDetails;

    @NotNull(message = "The Price is required.")
    private String productPrice;

    @NotBlank(message = "The Product Location Zip code is required.")
    @Pattern(regexp = "^\\d{1,5}$", flags = { Flag.CASE_INSENSITIVE, Flag.MULTILINE }, message = "The Zip code is invalid.")
    private String productLocationZipCode;

    /**
     * Create Empty Product
     */
    public Product()  {
    }

    /**
     * Create Product
     * @param _pid
     * @param _prodNm
     * @param _prodDt
     * @param _prodPr
     */
    public Product(String _pid, String _prodNm, String _prodDt, String _prodPr)  {
        productId   = _pid;
        productName = _prodNm;
        productDetails = _prodDt;
        productPrice   = _prodPr;
    }

    /**
     * Get Product ID
     * @return
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Get Product Name
     * @return
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Get Product Details
     * @return
     */
    public String getProductDetails() {
        return productDetails;
    }

    /**
     * Get Product Price
     */
    public String getProductPrice() {
        return productPrice;
    }

    /**
     * Get Product Location Zip Code
     * @return
     */
    public String getProductLocationZipCode() {
        return productLocationZipCode;
    }

    /**
     * Creates a JSON String
     * @return
     */
    public String toString() {
        return Utils.toJsonString(this);
    }
}
