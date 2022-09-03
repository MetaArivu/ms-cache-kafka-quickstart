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
package io.fusion.air.microservice.domain.ports.services;

import io.fusion.air.microservice.domain.entities.example.ProductEntity;
import io.fusion.air.microservice.domain.models.example.Product;

import java.util.List;
import java.util.UUID;


/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public interface ProductService {

    /**
     * Get All the Products
     * @return
     */
    public List<ProductEntity> getAllProduct();

    /**
     * Get Product By Product ID
     * @param productId
     * @return
     */
    public ProductEntity getProductById(UUID productId);

    /**
     * Create Product
     * @param product
     * @return
     */
    public ProductEntity createProduct(ProductEntity product);

    /**
     * Create Product (from DTO)
     * @param product
     * @return
     */
    public ProductEntity createProduct(Product product);

    /**
     * Update Product Name & Details
     * @param product
     * @return
     */
    public ProductEntity updateProduct(ProductEntity product);

    /**
     * Update the Product Price
     * @param product
     * @return
     */
    public ProductEntity updatePrice(ProductEntity product);

    /**
     * De Activate Product
     * @param _productId
     * @return
     */
    public ProductEntity deActivateProduct(UUID _productId);

    /**
     * Activate Product
     * @param _productId
     * @return
     */
    public ProductEntity activateProduct(UUID _productId);

    /**
     * Delete the product (Permanently Deletes the Product)
     * @param id
     */
    public void deleteProduct(UUID id);
}
