package com.stockalert.stockalert.service;

import com.stockalert.stockalert.entity.Product;
import com.stockalert.stockalert.entity.StockMovement;
import com.stockalert.stockalert.repository.ProductRepository;
import com.stockalert.stockalert.repository.StockMovementRepository;
import org.springframework.stereotype.Service;
import com.stockalert.stockalert.dto.FastMovingProductDTO;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final ProductRepository productRepository;

    public StockMovementService(
            StockMovementRepository stockMovementRepository,
            ProductRepository productRepository) {

        this.stockMovementRepository = stockMovementRepository;
        this.productRepository = productRepository;
    }

    public StockMovement addMovement(
            Long productId,
            StockMovement movement) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found with id: "
                                        + productId));

        int currentStock =
                getCurrentStock(productId);

        int change = movement.getQuantity();

        switch (movement.getMovementType()) {

            case SALE:
            case DAMAGE:

                if (currentStock - change < 0) {
                    throw new RuntimeException(
                            "Insufficient stock. Current stock: "
                                    + currentStock
                                    + ", requested: "
                                    + change
                    );
                }

                break;

            case PURCHASE:
            case RETURN:
                break;
        }
        movement.setProduct(product);
        movement.setMovementDate(LocalDateTime.now());
        return stockMovementRepository.save(movement);
    }

    public List<StockMovement> getMovementsByProduct(
            Long productId) {

        if (!productRepository.existsById(productId)) {
            throw new RuntimeException(
                    "Product not found with id: " + productId);
        }

        return stockMovementRepository
                .findByProductId(productId);
    }

    public int getCurrentStock(Long productId) {

        if (!productRepository.existsById(productId)) {
            throw new RuntimeException(
                    "Product not found with id: " + productId);
        }

        return stockMovementRepository
                .calculateCurrentStock(productId);
    }
    public List<FastMovingProductDTO> getFastMovingProducts(
            LocalDateTime startDate,
            LocalDateTime endDate) {

        return stockMovementRepository.findFastMovingProducts(
                startDate,
                endDate
        );
    }
}