package com.example.resource;

import com.example.entity.Product;
import com.example.repository.ProductRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.List;

@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductRepository productRepository;

    @GET
    public List<Product> getAllProducts() {
        return productRepository.listAll();
    }

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") Long id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Product not found\"}")
                    .build();
        }
        return Response.ok(product).build();
    }

    @GET
    @Path("/search")
    public List<Product> searchProducts(@QueryParam("name") String name) {
        if (name == null || name.isEmpty()) {
            return productRepository.listAll();
        }
        return productRepository.findByName(name);
    }

    @GET
    @Path("/available")
    public List<Product> getAvailableProducts() {
        return productRepository.findAvailableProducts();
    }

    @GET
    @Path("/price-range")
    public List<Product> getProductsByPriceRange(
            @QueryParam("min") @DefaultValue("0") Double minPrice,
            @QueryParam("max") @DefaultValue("999999") Double maxPrice) {
        return productRepository.findByPriceRange(minPrice, maxPrice);
    }

    @POST
    @Transactional
    public Response createProduct(@Valid Product product) {
        productRepository.persist(product);
        return Response.status(Response.Status.CREATED).entity(product).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateProduct(@PathParam("id") Long id, @Valid Product updatedProduct) {
        Product product = productRepository.findById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Product not found\"}")
                    .build();
        }

        product.name = updatedProduct.name;
        product.description = updatedProduct.description;
        product.price = updatedProduct.price;
        product.quantity = updatedProduct.quantity;
        product.updatedAt = LocalDateTime.now();

        return Response.ok(product).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteProduct(@PathParam("id") Long id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Product not found\"}")
                    .build();
        }

        productRepository.delete(product);
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public Long countProducts() {
        return productRepository.count();
    }
}
