package zatribune.spring.cookmaster.data.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import zatribune.spring.cookmaster.data.entities.*;
import zatribune.spring.cookmaster.data.repositories.CategoryRepository;
import zatribune.spring.cookmaster.data.repositories.RecipeRepository;
import zatribune.spring.cookmaster.data.repositories.UnitMeasureRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Optional;

@Slf4j
@Component
@Profile({"dev","prod"})//only active with the default profile with h2 database
public class MySQLBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private final CategoryRepository categoryRepository;
    private final UnitMeasureRepository unitMeasureRepository;

    @Autowired
    public MySQLBootstrap(CategoryRepository categoryRepository, UnitMeasureRepository unitMeasureRepository) {
        log.debug("I'm at the Bootstrap phase");
        this.categoryRepository = categoryRepository;
        this.unitMeasureRepository = unitMeasureRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (categoryRepository.count()== 0L){
            log.info("loading default Categories");
            loadCategories();
        }
        if (unitMeasureRepository.count()==0L){
            log.info("loading default Unit Of Measures");
            loadUnitMeasures();
        }
    }

    void loadCategories(){
        Category american = new Category();
        american.setDescription("American");
        Category italian = new Category();
        italian.setDescription("Italian");
        Category mexican = new Category();
        mexican.setDescription("Mexican");
        Category fastFood=new Category();
        fastFood.setDescription("Fast Food");
        categoryRepository.saveAll(Arrays.asList(american,italian,mexican));
    }
    void loadUnitMeasures(){
        UnitMeasure emptyUOM = new UnitMeasure("");
        UnitMeasure teaspoon = new UnitMeasure("teaspoon");
        UnitMeasure tablespoon = new UnitMeasure("tablespoon");
        UnitMeasure cup = new UnitMeasure("cup");
        UnitMeasure pinch = new UnitMeasure("pinch");
        UnitMeasure ounce = new UnitMeasure("ounce");
        UnitMeasure dash = new UnitMeasure("dash");
        unitMeasureRepository.saveAll(Arrays.asList(emptyUOM,teaspoon,tablespoon,cup,pinch,ounce,dash));
    }

}
