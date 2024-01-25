package com.enviro.assessment.grad001.favourokwara.investment.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enviro.assessment.grad001.favourokwara.investment.model.WithdrawalNotice;

/**
 * WithdrawalNoticeRepository interface responsible for managing withdrawal notice entities.
 * Extends JpaRepository to provide CRUD operation on our entities.
 */
@Repository
public interface WithdrawalNoticeRepository extends JpaRepository<WithdrawalNotice, Long> {

    /**
     * Retrieves a list of withdrawal notices for a specific product within a specified date range. 
     * @param productId The ID of the product for which withdrawal notices are retrieved.
     * @param start     The start date of the date ranges.
     * @param end       The end date of the date range.
     * @return A list of withdrawal notices matching the criteria.
     */
    List<WithdrawalNotice> findByProductIdAndCreatedDateBetween(Long productId, LocalDateTime start, LocalDateTime end);
}