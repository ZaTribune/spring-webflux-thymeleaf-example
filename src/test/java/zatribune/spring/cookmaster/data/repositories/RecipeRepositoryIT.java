package zatribune.spring.cookmaster.data.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zatribune.spring.cookmaster.data.entities.Recipe;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class RecipeRepositoryIT {

    @Autowired
    RecipeRepository recipeRepository;
    @BeforeEach
    void setUp() {
    }

    @Test
    void findRecipeByTitle() {
        Optional<Recipe> recipe= recipeRepository.findRecipeByTitle("Perfect Guacamole");
        recipe.ifPresent(u->assertEquals("Perfect Guacamole",u.getTitle()));
    }
}