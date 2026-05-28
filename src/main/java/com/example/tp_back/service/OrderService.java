package com.example.tp_back.service;

import com.example.tp_back.entity.Order;
import com.example.tp_back.entity.Product;
import com.example.tp_back.entity.User;
import com.example.tp_back.repository.OrderRepository;
import com.example.tp_back.repository.ProductRepository;
import com.example.tp_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    // Créer une commande
    public Order createOrder(String username, Integer productId, int quantite) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé : " + productId));

        if (product.getStockQuantity() < quantite) {
            throw new RuntimeException("Stock insuffisant");
        }

        // Calcul du montant total
        float total = product.getPrice() * quantite;

        // Décrémenter le stock
        product.setStockQuantity(product.getStockQuantity() - quantite);
        productRepository.save(product);

        Order order = new Order();
        order.setUser(user);
        order.setProduit(product);
        order.setQuantite(quantite);
        order.setTotalAmount(total);
        order.setStatus(Order.OrderStatus.PENDING);

        return orderRepository.save(order);
    }

    // Voir ses propres commandes
    public List<Order> getMyOrders(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return orderRepository.findByUserId(user.getId());
    }
}
