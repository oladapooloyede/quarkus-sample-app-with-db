package com.example.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product extends PanacheEntity {

    @NotBlank(message = "Product name is required")
    @Column(nullable = false)
    public String name;

    @Column(length = 1000)
    public String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    @Column(nullable = false, precision = 10, scale = 2)
    public BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Column(nullable = false)
    public Integer quantity;

    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    public Product() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Product(String name, String description, BigDecimal price, Integer quantity) {
        this();
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }
}
