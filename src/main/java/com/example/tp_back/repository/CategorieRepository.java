package com.example.tp_back.repository;

import com.example.tp_back.entity.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Integer> {

    Optional<Categorie> findByName(String name);

    boolean existsByName(String name);
}
