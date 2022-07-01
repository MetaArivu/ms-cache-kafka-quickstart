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

import java.io.Serializable;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class Product implements Serializable {

    private String productId;
    private String productName;
    private String productDetails;
    private String productPrice;

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

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public String getProductPrice() {
        return productPrice;
    }
}
