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
package io.fusion.air.microservice.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.fusion.air.microservice.domain.models.Product;
import io.fusion.air.microservice.utils.Utils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */

@Entity
@Table(name = "products_m")
public class ProductEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "productId", columnDefinition = "char(36)", unique = true)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID productId;

    @Column(name = "productName")
    private String productName;

    @Column(name = "productDetails")
    private String productDetails;

    @Column(name = "price")
    private BigDecimal productPrice;

    @Column(name = "productLocationZipCode")
    private String productLocationZipCode;

    private boolean isActive;

    /**
     * Empty Product Entity
     */
    public ProductEntity() {
    }

    /**
     * Create Product Entity
     * @param _pName
     * @param _pDetails
     * @param _pPrice
     * @param _pZipCode
     */
    public ProductEntity(String _pName, String _pDetails, BigDecimal _pPrice, String _pZipCode) {
        this.productName            = _pName;
        this.productDetails         = _pDetails;
        this.productPrice           = _pPrice;
        this.productLocationZipCode = _pZipCode;
    }

    /**
     * Create Product from the Product DTO
     * @param _product
     */
    public ProductEntity(Product _product) {
        // this.productId = Utils.getUUID(_product.getProductId());
        this.productName            = _product.getProductName();
        this.productDetails         = _product.getProductDetails();
        this.productPrice           = _product.getProductPrice();
        this.productLocationZipCode = _product.getProductLocationZipCode();
    }

    /**
     * Get Product ID
     * @return
     */
    public UUID getProductId() {
        return productId;
    }

    /**
     * Returns Product ID as String
     * @return
     */
    @JsonIgnore
    public String getProductIdAsString() {
        return productId.toString();
    }

    /**
     * Returns Product Name
     * @return
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Returns Product Details
     * @return
     */
    public String getProductDetails() {
        return productDetails;
    }

    /**
     * Returns Product Price
     * @return
     */
    public BigDecimal getProductPrice() {
        return productPrice;
    }

    /**
     * Returns Product Zip Code
     * @return
     */
    public String getProductLocationZipCode() {
        return productLocationZipCode;
    }

    // Set Methods Provided to demonstrate the Update Part of the CRUD Operations
    // Immutable Objects are always better.
    /**
     * Set the Product Name
     * @param productName
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Set the Product Details
     * @param productDetails
     */
    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    /**
     * Set the Product Price
     * @param productPrice
     */
    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }
}
