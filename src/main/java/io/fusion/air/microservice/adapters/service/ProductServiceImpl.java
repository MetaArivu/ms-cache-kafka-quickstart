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
package io.fusion.air.microservice.adapters.service;

import io.fusion.air.microservice.adapters.repository.ProductRepository;
import io.fusion.air.microservice.domain.entities.ProductEntity;
import io.fusion.air.microservice.domain.exceptions.DataNotFoundException;
import io.fusion.air.microservice.domain.exceptions.ResourceNotFoundException;
import io.fusion.air.microservice.domain.models.Product;
import io.fusion.air.microservice.domain.ports.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Create Product (from the DTO)
     * @param product
     * @return
     */
    public ProductEntity createProduct(Product product) {
        return createProduct(new ProductEntity(product));
    }

    /**
     * Create Product
     *
     * @param product
     * @return
     */
    @Override
    public ProductEntity createProduct(ProductEntity product) {
        return productRepository.save(product);
    }

    /**
     * Update Product (Name & Details)
     *
     * @param product
     * @return
     */
    @Override
    public ProductEntity updateProduct(ProductEntity product) {
        Optional<ProductEntity> productDb = this.productRepository.findById(product.getProductId());
        if(!productDb.isPresent()) {
            throw new DataNotFoundException("Data not found with id : " + product.getProductId());
        }
        ProductEntity productUpdate = productDb.get();
        productUpdate.setProductName(product.getProductName());
        productUpdate.setProductDetails(product.getProductDetails());
        productRepository.save(productUpdate);
        return productUpdate;
    }

    public ProductEntity updatePrice(ProductEntity product) {
        Optional<ProductEntity> productDb = this.productRepository.findById(product.getProductId());
        if(!productDb.isPresent()) {
            throw new DataNotFoundException("Data not found with id : " + product.getProductId());
        }
        ProductEntity productUpdate = productDb.get();
        productUpdate.setProductPrice(product.getProductPrice());
        productRepository.save(productUpdate);
        return productUpdate;
    }

    /**
     * Get All the Products
     * @return
     */
    @Override
    public List<ProductEntity> getAllProduct() {
        return this.productRepository.findAll();
    }

    /**
     * Get Product By Product ID
     * @param productId
     * @return
     */
    @Override
    public ProductEntity getProductById(UUID productId) {
        Optional<ProductEntity> productDb = this.productRepository.findById(productId);
        if(!productDb.isPresent()) {
            throw new DataNotFoundException("Data not found with id : " + productId);
        }
        return productDb.get();
    }

    /**
     * Delete the product
     * @param productId
     */
    @Override
    public void deleteProduct(UUID productId) {
        Optional<ProductEntity> productDb = this.productRepository.findById(productId);
        if(!productDb.isPresent()) {
            throw new DataNotFoundException("Data not found with id : " + productId);
        }
        this.productRepository.delete(productDb.get());
    }
}
