package com.co.softworld.service;

import com.co.softworld.model.Category;
import reactor.core.publisher.Mono;

public interface ICategoryService {
    Mono<Category> findByName(String name);
}