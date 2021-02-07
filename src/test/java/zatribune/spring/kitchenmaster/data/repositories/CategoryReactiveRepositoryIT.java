package zatribune.spring.kitchenmaster.data.repositories;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import zatribune.spring.kitchenmaster.data.entities.Category;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@DataMongoTest
class CategoryReactiveRepositoryIT {


    @Autowired
    CategoryReactiveRepository reactiveRepository;
    ObjectId id;
    String description;
    String info;


    @BeforeEach
    void setup(){
        reactiveRepository.deleteAll().block();
        id=new ObjectId();
        description="Egyptian";
        info="Watch out for the calories";
    }


    @Test
    void save(){
        Category category=new Category();
        category.setDescription(description);
        category.setInfo(info);
        reactiveRepository.save(category).block();
        Long count=reactiveRepository.count().block();
        assertEquals(1L,count);
        log.info("size : {}",count);
    }
    @Test
    void saveAllAndFindAll(){
        Category category=new Category();
        category.setDescription(description);
        category.setInfo(info);
        Category category2=new Category();
        category.setDescription(description);
        category.setInfo(info);
        Category category3=new Category();
        category.setDescription(description);
        category.setInfo(info);
        //saveAll
        reactiveRepository.saveAll(Arrays.asList(category,category2,category3)).subscribe();
        Long resultSize=reactiveRepository.count().block();
        assertEquals(3, resultSize);
        //findAll
        assertEquals(3L,reactiveRepository.findAll().count().block());
        log.info("size {}",resultSize);
    }
    @Test
    void findByDescription(){
        Category category=new Category();
        category.setDescription(description);
        reactiveRepository.save(category).block();

        Category resultCategory=reactiveRepository.findByDescription(description).block();
        assertNotNull(resultCategory);
        assertEquals(description,resultCategory.getDescription());
    }
}