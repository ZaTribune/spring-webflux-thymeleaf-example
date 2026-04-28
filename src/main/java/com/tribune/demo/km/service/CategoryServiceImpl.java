package com.tribune.demo.km.service;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.tribune.demo.km.command.CategoryCommand;
import com.tribune.demo.km.converter.CategoryToCategoryCommand;

import com.tribune.demo.km.data.entity.Category;
import com.tribune.demo.km.data.repository.CategoryReactiveRepository;

@Service
public record CategoryServiceImpl(CategoryReactiveRepository repository,
                                  CategoryToCategoryCommand categoryToCategoryCommand) implements CategoryService {


    @Override
    public Flux<Category> getAllCategories() {
        return repository.findAll();
    }

    @Override
    public Mono<Category> getCategoryById(String id) {
        return repository.findById(new ObjectId(id));
    }

    @Override
    public Mono<CategoryCommand> getCategoryCommandById(String id) {
        return getCategoryById(id).map(categoryToCategoryCommand::convert);
    }
}
