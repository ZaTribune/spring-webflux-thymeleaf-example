package zatribune.spring.cookmaster.data.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import zatribune.spring.cookmaster.data.entities.Recipe;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class RecipeReactiveRepositoryIT {

    @Autowired
    RecipeReactiveRepository repository;
    String title="Hamburger ";


    @BeforeEach
    void setUp() {
         repository.deleteAll().block();
    }

    @Test
    void save(){
        Recipe recipe=new Recipe();
        recipe.setTitle(title);
        Recipe returnedRecipe=repository.save(recipe).block();
        Long count=repository.count().block();
        assertEquals(1L,count);
        assertNotNull(returnedRecipe);
        assertNotNull(returnedRecipe.getId());
    }
    @Test
    void saveAllAndFindAll(){
        Recipe r1=new Recipe();
        r1.setTitle(title+1);
        Recipe r2=new Recipe();
        r2.setTitle(title+2);
        Recipe r3=new Recipe();
        r3.setTitle(title+3);
        //saveAll()
        repository.saveAll(List.of(r1,r2,r3)).subscribe();//ids are created
        Long count=repository.count().block();
        assertEquals(3L,count);
        //findAll()
        List<Recipe>list=repository.findAll().collectList().block();
        assertNotNull(list);
        assertEquals(3,list.size());
    }

    @Test
    void findByTitle(){
        Recipe recipe=new Recipe();
        recipe.setTitle(title);
        repository.save(recipe).block();
        Recipe returnedRecipe=repository.findByTitle(title).block();
        assertNotNull(returnedRecipe);
        assertEquals(title,returnedRecipe.getTitle());
    }

}