package com.co.softworld.repository;

import com.co.softworld.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IProductRepository extends ReactiveMongoRepository<Product, String> {

}
