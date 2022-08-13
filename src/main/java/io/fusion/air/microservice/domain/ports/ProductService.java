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
package io.fusion.air.microservice.domain.ports;

import io.fusion.air.microservice.domain.entities.ProductEntity;
import io.fusion.air.microservice.domain.models.Product;

import java.util.List;
import java.util.UUID;


/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public interface ProductService {

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
     * Delete the product
     * @param id
     */
    public void deleteProduct(UUID id);
}
