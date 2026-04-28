package com.tribune.demo.km.controller;


import com.tribune.demo.km.data.entity.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import com.tribune.demo.km.command.CategoryCommand;
import com.tribune.demo.km.converter.CategoryToCategoryCommand;
import com.tribune.demo.km.service.CategoryService;

import java.util.List;
import java.util.Map;


@Slf4j
@Controller
public record CategoriesController(CategoryService categoryService,
                                   CategoryToCategoryCommand categoryToCategoryCommand) {


    @GetMapping("/categories")
    public String getCategoriesPage() {
        log.info("GET /categories - Category list page");
        return "index";
    }

    @GetMapping("/categories/{id}")
    public Mono<String> getCategoryDetail(@PathVariable String id, Model model) {
        log.info("GET /categories/{} - Category detail page", id);
        return categoryService.getCategoryById(id)
                .doOnNext(category -> model.addAttribute("category", category))
                .then(Mono.just("index"))
                .onErrorReturn("error/404");
    }

    @GetMapping("/categories/{id}/fragment")
    public Mono<String> getCategoryDetailFragment(@PathVariable String id, Model model) {
        log.info("GET /categories/{}/fragment - Category detail fragment", id);
        return categoryService.getCategoryById(id)
                .doOnNext(category -> model.addAttribute("category", category))
                .then(Mono.just("categories/showCategory"))
                .onErrorReturn("error/404");
    }

    @GetMapping("/categories/new")
    public String getCategoryCreateForm(Model model) {
        log.info("GET /categories/new - New category form");
        model.addAttribute("category", new CategoryCommand());
        return "categories/createCategory";
    }

    @GetMapping("/categories/{id}/edit")
    public Mono<String> getCategoryEditForm(@PathVariable String id, Model model) {
        log.info("GET /categories/{}/edit - Edit category form", id);
        return categoryService.getCategoryById(id)
                .map(categoryToCategoryCommand::convert)
                .doOnNext(categoryCommand -> model.addAttribute("category", categoryCommand))
                .then(Mono.just("categories/createCategory"))
                .onErrorReturn("error/404");
    }

    /**
     * API endpoints for JSON data (REST: GET)
     */
    @GetMapping(value = "/api/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<Category>> getCategoryById(@PathVariable String id) {
        log.debug("GET /api/categories/{} - Get category by ID as JSON", id);
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/api/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<List<Category>>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("GET /api/categories - Get all categories with pagination - page={}, size={}", page, size);
        return categoryService.getAllCategories()
                .skip((long) page * size)
                .take(size)
                .collectList()
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of()));
    }


    /**
     * Form submission endpoints (REST: POST)
     */
    @PostMapping("/categories")
    public Mono<String> createOrUpdateCategory(
            @ModelAttribute("category") Mono<CategoryCommand> categoryCommand) {
        log.info("POST /categories - Create or update category");
        return categoryCommand
                .flatMap(cmd -> {
                    // TODO: Implement save logic when CategoryService is updated
                    log.warn("Category save not yet implemented");
                    return Mono.just(cmd);
                })
                .thenReturn("categories/listCategories");
    }

    /**
     * Delete endpoint (REST: DELETE)
     */
    @DeleteMapping("/categories/{id}")
    @ResponseBody
    public Mono<ResponseEntity<Map<String, Object>>> deleteCategory(@PathVariable String id) {
        log.info("DELETE /categories/{} - Delete category", id);
        // TODO: Implement delete logic when CategoryService is updated
        log.warn("Category delete not yet implemented");
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                Map.of("success", false, "message", "Category deletion not yet implemented")
        ));
    }
}
