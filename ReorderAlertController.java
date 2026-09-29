package com.stockalert.stockalert.controller;

import com.stockalert.stockalert.entity.ReorderAlert;
import com.stockalert.stockalert.service.ReorderAlertService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reorder-alerts")
public class ReorderAlertController {

    private final ReorderAlertService reorderAlertService;

    public ReorderAlertController(
            ReorderAlertService reorderAlertService) {

        this.reorderAlertService = reorderAlertService;
    }

    @GetMapping
    public List<ReorderAlert> getAllAlerts() {
        return reorderAlertService.getAllAlerts();
    }

    @GetMapping("/open")
    public List<ReorderAlert> getOpenAlerts() {
        return reorderAlertService.getOpenAlerts();
    }

    @PutMapping("/{id}/fulfill")
    public ReorderAlert fulfillAlert(
            @PathVariable Long id) {

        return reorderAlertService.fulfillAlert(id);
    }
}