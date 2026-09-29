package com.stockalert.stockalert.repository;

import com.stockalert.stockalert.entity.AlertStatus;
import com.stockalert.stockalert.entity.ReorderAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReorderAlertRepository
        extends JpaRepository<ReorderAlert, Long> {

    boolean existsByProductIdAndStatus(
            Long productId,
            AlertStatus status
    );

    List<ReorderAlert> findByStatus(AlertStatus status);

    List<ReorderAlert> findByProductId(Long productId);
}