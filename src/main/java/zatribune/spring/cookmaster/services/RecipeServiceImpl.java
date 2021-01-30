package zatribune.spring.cookmaster.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import zatribune.spring.cookmaster.commands.RecipeCommand;
import zatribune.spring.cookmaster.converters.RecipeCommandToRecipe;
import zatribune.spring.cookmaster.converters.RecipeToRecipeCommand;
import zatribune.spring.cookmaster.data.entities.Recipe;
import zatribune.spring.cookmaster.data.repositories.RecipeReactiveRepository;

// this is a concretion
@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeReactiveRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    @Autowired
    public RecipeServiceImpl(RecipeReactiveRepository recipeRepository,
                             RecipeCommandToRecipe recipeCommandToRecipe,
                             RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand=recipeToRecipeCommand;
    }

    @Override
    public Flux<Recipe> getAllRecipes() {
        log.debug("I'm getting recipes from the RecipeService");
        return recipeRepository.findAll();

    }

    @Override
    public Mono<Recipe> getRecipeById(String id) {
      return recipeRepository.findById(id);
    }

    @Override
    public Mono<RecipeCommand> getRecipeCommandById(String id) {
        return getRecipeById(id).map(recipeToRecipeCommand::convert);
    }

    @Override
    public Mono<Recipe> getRecipeByTitle(String title) {
       return recipeRepository.findByTitle(title);
    }


    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipeCommand) {
        return recipeRepository.save(recipeCommandToRecipe.convert(recipeCommand)).map(recipeToRecipeCommand::convert);
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
        recipeRepository.delete(recipe);
    }

    @Override
    public void deleteRecipeById(String id) {
        recipeRepository.deleteById(id);
    }
}
