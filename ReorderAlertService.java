package com.stockalert.stockalert.service;

import com.stockalert.stockalert.entity.AlertStatus;
import com.stockalert.stockalert.entity.Product;
import com.stockalert.stockalert.entity.ReorderAlert;
import com.stockalert.stockalert.repository.ProductRepository;
import com.stockalert.stockalert.repository.ReorderAlertRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReorderAlertService {

    private final ReorderAlertRepository reorderAlertRepository;
    private final ProductRepository productRepository;
    private final StockMovementService stockMovementService;

    public ReorderAlertService(
            ReorderAlertRepository reorderAlertRepository,
            ProductRepository productRepository,
            StockMovementService stockMovementService) {

        this.reorderAlertRepository = reorderAlertRepository;
        this.productRepository = productRepository;
        this.stockMovementService = stockMovementService;
    }

    public ReorderAlert checkAndCreateAlert(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found with id: " + productId));

        int currentStock =
                stockMovementService.getCurrentStock(productId);

        if (currentStock <= product.getReorderThreshold()) {

            boolean alertAlreadyExists =
                    reorderAlertRepository
                            .existsByProductIdAndStatus(
                                    productId,
                                    AlertStatus.OPEN
                            );

            if (!alertAlreadyExists) {

                ReorderAlert alert = new ReorderAlert(
                        product,
                        currentStock,
                        product.getReorderQuantity()
                );

                return reorderAlertRepository.save(alert);
            }
        }

        return null;
    }

    public List<ReorderAlert> getAllAlerts() {
        return reorderAlertRepository.findAll();
    }

    public List<ReorderAlert> getOpenAlerts() {
        return reorderAlertRepository
                .findByStatus(AlertStatus.OPEN);
    }

    public ReorderAlert fulfillAlert(Long alertId) {

        ReorderAlert alert =
                reorderAlertRepository.findById(alertId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Reorder alert not found with id: "
                                                + alertId));

        alert.setStatus(AlertStatus.FULFILLED);
        alert.setFulfilledAt(LocalDateTime.now());

        return reorderAlertRepository.save(alert);
    }
}