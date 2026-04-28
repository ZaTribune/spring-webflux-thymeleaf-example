package com.tribune.demo.km.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.tribune.demo.km.command.CategoryCommand;
import com.tribune.demo.km.data.entity.Category;

public interface CategoryService {
    Flux<Category> getAllCategories();
    Mono<Category> getCategoryById(String id);
    Mono<CategoryCommand> getCategoryCommandById(String id);
}
