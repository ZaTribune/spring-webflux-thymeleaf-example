package zatribune.spring.cookmaster.data.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zatribune.spring.cookmaster.data.bootstrap.DevBootstrap;
import zatribune.spring.cookmaster.data.entities.Recipe;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
// will only load data in data-h2.sql file
@DataMongoTest
@ExtendWith(SpringExtension.class)
class RecipeRepositoryIT {

    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UnitMeasureRepository unitMeasureRepository;
    @BeforeEach
    void setUp() {
        recipeRepository.deleteAll();
        categoryRepository.deleteAll();
        unitMeasureRepository.deleteAll();
        DevBootstrap devBootstrap=new DevBootstrap(recipeRepository,categoryRepository,unitMeasureRepository);
        devBootstrap.onApplicationEvent(null);
    }

    @Test
    void findRecipeByTitle() {
        Optional<Recipe> recipe= recipeRepository.findRecipeByTitle("Perfect Guacamole");
        recipe.ifPresent(u->assertEquals("Perfect Guacamole",u.getTitle()));
        assertFalse(recipe.isEmpty());
    }

    @Test
    void findAll(){
        Set<Recipe> recipes= StreamSupport.stream(recipeRepository.findAll().spliterator(),false)
                .collect(Collectors.toSet());
        assertNotNull(recipes);
        assertNotEquals(0, recipes.size());
    }

}