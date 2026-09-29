package com.stockalert.stockalert.repository;

import com.stockalert.stockalert.dto.FastMovingProductDTO;
import com.stockalert.stockalert.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StockMovementRepository
        extends JpaRepository<StockMovement, Long> {

    List<StockMovement> findByProductId(Long productId);


    // Calculate current stock
    @Query("""
           SELECT COALESCE(SUM(
               CASE
                   WHEN s.movementType =
                        com.stockalert.stockalert.entity.MovementType.PURCHASE
                        THEN s.quantity

                   WHEN s.movementType =
                        com.stockalert.stockalert.entity.MovementType.RETURN
                        THEN s.quantity

                   WHEN s.movementType =
                        com.stockalert.stockalert.entity.MovementType.SALE
                        THEN -s.quantity

                   WHEN s.movementType =
                        com.stockalert.stockalert.entity.MovementType.DAMAGE
                        THEN -s.quantity

                   ELSE 0
               END
           ), 0)
           FROM StockMovement s
           WHERE s.product.id = :productId
           """)
    Integer calculateCurrentStock(
            @Param("productId") Long productId);


    // Find fast-moving products
    @Query("""
           SELECT new com.stockalert.stockalert.dto.FastMovingProductDTO(
               s.product.id,
               s.product.name,
               s.product.sku,
               SUM(s.quantity)
           )
           FROM StockMovement s
           WHERE s.movementType =
                 com.stockalert.stockalert.entity.MovementType.SALE
           AND s.movementDate >= :startDate
           AND s.movementDate <= :endDate
           GROUP BY s.product.id, s.product.name, s.product.sku
           ORDER BY SUM(s.quantity) DESC
           """)
    List<FastMovingProductDTO> findFastMovingProducts(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}