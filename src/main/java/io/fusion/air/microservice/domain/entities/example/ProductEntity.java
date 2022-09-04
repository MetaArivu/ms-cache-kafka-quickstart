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
package io.fusion.air.microservice.domain.entities.example;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.fusion.air.microservice.domain.entities.core.AbstractBaseEntity;
import io.fusion.air.microservice.domain.models.example.Product;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */

@Entity
@Table(name = "products_m")
public class ProductEntity extends AbstractBaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "productId", columnDefinition = "char(36)", unique = true)
    @Type(type = "org.hibernate.type.UUIDCharType")
    // @Size(min = 36, max = 36, message = "The length of Product ID Name must be 36 characters.")
    // @Pattern(regexp = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$", message = "Invalid UUID")
    private UUID productId;

    @NotBlank(message = "The Product Name is required.")
    @Size(min = 3, max = 32, message = "The length of Product Name must be 3-32 characters.")
    @Column(name = "productName")
    private String productName;

    @NotNull(message = "The Product Details is  required.")
    @Size(min = 5, max = 64, message = "The length of Product Description must be 5-64 characters.")
    @Column(name = "productDetails")
    private String productDetails;

    @NotNull(message = "The Price is required.")
    @Column(name = "price")
    private BigDecimal productPrice;

    @NotBlank(message = "The Product ZipCode is required.")
    @Column(name = "productLocationZipCode")
    @Pattern(regexp = "^[0-9]{5}\\b", message = "Zip Code Must be 5 Digits")
    private String productLocationZipCode;

    /**
     * Empty Product Entity
     */
    public ProductEntity() {
    }

    /**
     * Create Product from the Product DTO
     * @param _product
     */
    public ProductEntity(Product _product) {
        this(_product.getProductName(), _product.getProductDetails(),
                _product.getProductPrice(), _product.getProductLocationZipCode());
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
        // this.isActive               = true;
        // this.auditLog               = new AuditLog();
        initAudit();
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

    /**
     * De-Activate Product
     */
    @JsonIgnore
    public void deActivateProduct() {
        deActivate();
    }

    /**
     * Activate Product
     */
    @JsonIgnore
    public void activateProduct() {
        activate();
    }
}
