package com.stockalert.stockalert.dto;

public class FastMovingProductDTO {

    private Long productId;
    private String productName;
    private String sku;
    private Long totalSales;

    public FastMovingProductDTO(
            Long productId,
            String productName,
            String sku,
            Long totalSales) {

        this.productId = productId;
        this.productName = productName;
        this.sku = sku;
        this.totalSales = totalSales;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getSku() {
        return sku;
    }

    public Long getTotalSales() {
        return totalSales;
    }
}