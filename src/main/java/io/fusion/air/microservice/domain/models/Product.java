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
