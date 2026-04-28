package com.tribune.demo.km.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import com.tribune.demo.km.command.RecipeCommand;
import com.tribune.demo.km.command.UnitMeasureCommand;
import com.tribune.demo.km.data.entity.Recipe;
import com.tribune.demo.km.service.RecipeService;
import com.tribune.demo.km.service.UnitMeasureService;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public record RecipesController(RecipeService recipeService, UnitMeasureService unitMeasureService) {


    /**
     * Page endpoints for HTML views (REST: GET)
     */
    @GetMapping("/recipes")
    public String getRecipesPage(Model model) {
        log.info("GET /recipes - Recipe list page");
        return "index";
    }

    @GetMapping("/recipes/{id}")
    public Mono<String> getRecipeDetail(@PathVariable String id, Model model) {
        log.info("GET /recipes/{} - Recipe detail page", id);
        return recipeService.getRecipeById(id)
                .doOnNext(recipe -> model.addAttribute("recipe", recipe))
                .then(Mono.just("index"))
                .onErrorReturn("error/404");
    }

    @GetMapping("/recipes/{id}/fragment")
    public Mono<String> getRecipeDetailFragment(@PathVariable String id, Model model) {
        log.info("GET /recipes/{}/fragment - Recipe detail fragment", id);
        return recipeService.getRecipeById(id)
                .doOnNext(recipe -> model.addAttribute("recipe", recipe))
                .then(Mono.just("recipes/showRecipe"))
                .onErrorReturn("error/404");
    }

    @GetMapping("/recipes/new")
    public Mono<String> getRecipeCreateForm(Model model) {
        log.info("GET /recipes/new - New recipe form");
        model.addAttribute("recipe", new RecipeCommand());
        return unitMeasureService.getAllUnitMeasures()
                .collectList()
                .doOnNext(unitMeasures -> model.addAttribute("unitMeasures", unitMeasures))
                .then(Mono.just("recipes/createRecipe"));
    }

    @GetMapping("/recipes/{id}/edit")
    public Mono<String> getRecipeEditForm(@PathVariable String id, Model model) {
        log.info("GET /recipes/{}/edit - Edit recipe form", id);
        return recipeService.getRecipeCommandById(id)
                .flatMap(recipeCommand ->
                        unitMeasureService.getAllUnitMeasures().collectList()
                                .doOnNext(unitMeasures -> {
                                    model.addAttribute("recipe", recipeCommand);
                                    model.addAttribute("unitMeasures", unitMeasures);
                                })
                                .then(Mono.just("recipes/createRecipe"))
                )
                .onErrorReturn("error/404");
    }

    /**
     * API endpoints for JSON data (REST: GET)
     * Note: /api/recipes is handled by RouterFunction in AppRouterFunctions for demonstration
     */


    @GetMapping(value = "/api/recipes/featured", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<List<Recipe>> getFeaturedRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        log.debug("GET /api/recipes/featured - Get featured recipes with pagination - page={}, size={}", page, size);
        return recipeService.getAllRecipes()
                .skip((long) page * size)
                .take(size)
                .collectList();
    }

    @GetMapping(value = "/api/recipes/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<List<Recipe>> searchRecipes(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "") String difficulty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("GET /api/recipes/search?search={}&difficulty={}&page={}&size={}", search, difficulty, page, size);
        return recipeService.getAllRecipes()
                .filter(recipe -> {
                    if (!search.isEmpty()) {
                        return recipe.getTitle().toLowerCase().contains(search.toLowerCase());
                    }
                    return true;
                })
                .filter(recipe -> {
                    if (!difficulty.isEmpty()) {
                        return recipe.getDifficulty().toString().equals(difficulty);
                    }
                    return true;
                })
                .skip((long) page * size)
                .take(size)
                .collectList();
    }

    @GetMapping(value = "/api/recipes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<RecipeCommand>> getRecipeById(@PathVariable String id) {
        log.debug("GET /api/recipes/{} - Get recipe by ID as JSON", id);
        return recipeService.getRecipeCommandById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/api/unit-measures", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<List<UnitMeasureCommand>> getUnitMeasures() {
        log.debug("GET /api/unit-measures - Get all unit measures");
        return unitMeasureService.getAllUnitMeasures().collectList();
    }

    /**
     * Form submission endpoints (REST: POST)
     */

    @PostMapping("/recipes")
    public Mono<String> createOrUpdateRecipe(
            @ModelAttribute("recipe") Mono<RecipeCommand> recipeCommand,
            Model model) {
        log.info("POST /recipes - Create or update recipe");
        return recipeCommand
                .flatMap(recipeService::saveRecipeCommand)
                .doOnNext(saved -> model.addAttribute("recipe", saved))
                .thenReturn("recipes/showRecipe")
                .onErrorResume(e -> {
                    log.error("Error saving recipe", e);
                    model.addAttribute("unitMeasures", unitMeasureService.getAllUnitMeasures());
                    return Mono.just("recipes/createRecipe");
                });
    }

    /**
     * Create or update recipe via JSON API
     */
    @PostMapping(value = "/api/recipes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<RecipeCommand>> saveRecipe(@RequestBody RecipeCommand recipeCommand) {
        log.info("POST /api/recipes - Save recipe: {}", recipeCommand.getTitle());
        return recipeService.saveRecipeCommand(recipeCommand)
                .map(saved -> ResponseEntity.status(recipeCommand.getId() == null ? HttpStatus.CREATED : HttpStatus.OK).body(saved))
                .onErrorResume(e -> {
                    log.error("Error saving recipe", e);
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }


    /**
     * Delete recipe via JSON API
     */
    @DeleteMapping(value = "/api/recipes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<Map<String, Object>>> deleteRecipeApi(@PathVariable String id) {
        log.info("DELETE /api/recipes/{} - Delete recipe via API", id);
        return recipeService.deleteRecipeById(id)
                .thenReturn(ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Recipe deleted successfully",
                        "id", (Object) id
                )))
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        Map.of(
                                "success", false,
                                "message", "Failed to delete recipe"
                        )
                ));
    }
}
