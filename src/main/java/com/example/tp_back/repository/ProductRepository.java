package com.example.tp_back.repository;

import com.example.tp_back.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {


    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);


    Page<Product> findByCategorieId(Integer categorieId, Pageable pageable);


    @Query("SELECT p FROM Product p WHERE " +
           "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:categorieId IS NULL OR p.categorie.id = :categorieId)")
    Page<Product> searchProducts(@Param("name") String name,
                                  @Param("categorieId") Integer categorieId,
                                  Pageable pageable);
}
