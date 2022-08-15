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
package io.fusion.air.microservice.adapters.repository;

import io.fusion.air.microservice.domain.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Example
 * Product Repository Impl
 *
 * @author arafkarsh
 */

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    /**
     * Find By Product ID
     * @param productId
     * @return
     */
    public ProductEntity findByProductId(UUID productId);

    /**
     * Return Product By Product Name
     * @param _name
     * @return
     */
    // @Query("SELECT ProductEntity FROM ProductEntity p WHERE p.productName like ?1")
    // public List<ProductEntity> fetchProductsdByName(String _name);

}
