package com.enviro.assessment.grad001.favourokwara.investment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enviro.assessment.grad001.favourokwara.investment.model.Product;

/*
 * ProductRepository interface responsible for managing Product entities.
 * Extends JpaRepository to provide CRUD operation on our entities.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Retrieve list of all products associated with the specified investor.
     * @param investorId The ID of the investor
     * @return A list of products associated with the specified investor.
     */
    List<Product> findAllByInvestorId(Long investorId);

    /**
     * Find products associated with a specified investor by its ID. 
     * @param productId  The ID of the product.
     * @param investorId The ID of the investor.
     * @return An optional with the found products.
     */
    Optional<Product> findByIdAndInvestorId(Long productId, Long investorId);
}