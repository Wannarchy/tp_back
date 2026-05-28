package com.example.tp_back.service;

import com.example.tp_back.entity.Categorie;
import com.example.tp_back.entity.Product;
import com.example.tp_back.repository.CategorieRepository;
import com.example.tp_back.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    // Liste paginée
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // Détail produit
    public Product getProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé : " + id));
    }

    // Recherche par nom et/ou catégorie
    public Page<Product> searchProducts(String name, Integer categorieId, Pageable pageable) {
        return productRepository.searchProducts(name, categorieId, pageable);
    }

    // Créer un produit (ADMIN)
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // Modifier un produit (ADMIN)
    public Product updateProduct(Integer id, Product updated) {
        Product product = getProductById(id);
        product.setName(updated.getName());
        product.setDescription(updated.getDescription());
        product.setPrice(updated.getPrice());
        product.setStockQuantity(updated.getStockQuantity());
        product.setLienImage(updated.getLienImage());

        if (updated.getCategorie() != null && updated.getCategorie().getId() != null) {
            Categorie cat = categorieRepository.findById(updated.getCategorie().getId())
                    .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));
            product.setCategorie(cat);
        }

        return productRepository.save(product);
    }

    // Supprimer un produit (ADMIN)
    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Produit non trouvé : " + id);
        }
        productRepository.deleteById(id);
    }
}
