package com.co.softworld.service;

import com.co.softworld.model.Category;
import com.co.softworld.repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public Mono<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }
}
