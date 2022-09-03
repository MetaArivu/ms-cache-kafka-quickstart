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
package io.fusion.air.microservice.adapters.service;

import io.fusion.air.microservice.adapters.repository.ProductRepository;
import io.fusion.air.microservice.domain.entities.example.ProductEntity;
import io.fusion.air.microservice.domain.exceptions.DataNotFoundException;

import io.fusion.air.microservice.domain.models.example.Product;
import io.fusion.air.microservice.domain.ports.services.ProductService;

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
     * WARNING:
     * This Method is ONLY For Demo Purpose. In Real World Scenario there should NOT be any
     * method which will return the whole data without any Conditions. Unless it's a very
     * small data set.
     *
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
        Optional<ProductEntity> productDb = productRepository.findById(productId);
        if(productDb.isPresent()) {
            return productDb.get();
        }
        throw new DataNotFoundException("Data not found with id : " + productId);
    }

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
        ProductEntity productUpdate = getProductById(product.getProductId()) ;
        productUpdate.setProductName(product.getProductName());
        productUpdate.setProductDetails(product.getProductDetails());
        productRepository.saveAndFlush(productUpdate);
        return productUpdate;
    }

    /**
     * Update the Product Price
     * @param product
     * @return
     */
    public ProductEntity updatePrice(ProductEntity product) {
        ProductEntity productUpdate = getProductById(product.getProductId()) ;
        productUpdate.setProductPrice(product.getProductPrice());
        productRepository.saveAndFlush(productUpdate);
        return productUpdate;
    }

    /**
     * De Activate Product
     *
     * @param _productId
     * @return
     */
    @Override
    public ProductEntity deActivateProduct(UUID _productId) {
        ProductEntity product = getProductById(_productId);
        product.deActivateProduct();
        productRepository.saveAndFlush(product);
        return product;
    }

    /**
     * Activate Product
     *
     * @param _productId
     * @return
     */
    @Override
    public ProductEntity activateProduct(UUID _productId) {
        ProductEntity product = getProductById(_productId);
        product.activateProduct();
        productRepository.saveAndFlush(product);
        return product;
    }

    /**
     * Delete the product
     * @param _productId
     */
    @Override
    public void deleteProduct(UUID _productId) {
        ProductEntity product = getProductById(_productId);
        productRepository.delete(product);
    }
}
