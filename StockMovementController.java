package com.stockalert.stockalert.controller;

import com.stockalert.stockalert.entity.ReorderAlert;
import com.stockalert.stockalert.entity.StockMovement;
import com.stockalert.stockalert.service.ReorderAlertService;
import com.stockalert.stockalert.service.StockMovementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.stockalert.stockalert.dto.FastMovingProductDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {

    private final StockMovementService stockMovementService;
    private final ReorderAlertService reorderAlertService;

    public StockMovementController(
            StockMovementService stockMovementService,
            ReorderAlertService reorderAlertService) {

        this.stockMovementService = stockMovementService;
        this.reorderAlertService = reorderAlertService;
    }

    @PostMapping("/product/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public StockMovement addMovement(
            @PathVariable Long productId,
            @Valid @RequestBody StockMovement movement) {

        StockMovement savedMovement =
                stockMovementService.addMovement(
                        productId,
                        movement
                );

        reorderAlertService.checkAndCreateAlert(productId);

        return savedMovement;
    }

    @GetMapping("/product/{productId}")
    public List<StockMovement> getMovements(
            @PathVariable Long productId) {

        return stockMovementService
                .getMovementsByProduct(productId);
    }

    @GetMapping("/product/{productId}/stock")
    public int getCurrentStock(
            @PathVariable Long productId) {

        return stockMovementService
                .getCurrentStock(productId);
    }
    @GetMapping("/fast-moving")
    public List<FastMovingProductDTO> getFastMovingProducts(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        LocalDateTime startDateTime =
                start.atStartOfDay();

        LocalDateTime endDateTime =
                end.atTime(23, 59, 59);

        return stockMovementService.getFastMovingProducts(
                startDateTime,
                endDateTime
        );
    }
}