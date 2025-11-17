package com.example.repository;

import com.example.entity.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

    public List<Product> findByName(String name) {
        return list("LOWER(name) LIKE LOWER(?1)", "%" + name + "%");
    }

    public List<Product> findAvailableProducts() {
        return list("quantity > 0");
    }

    public List<Product> findByPriceRange(Double minPrice, Double maxPrice) {
        return list("price >= ?1 AND price <= ?2", minPrice, maxPrice);
    }
}
