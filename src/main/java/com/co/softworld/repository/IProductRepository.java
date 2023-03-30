package com.co.softworld.repository;

import com.co.softworld.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface IProductRepository extends ReactiveMongoRepository<Product, String> {
    Mono<Product> findByName(String name);
}
