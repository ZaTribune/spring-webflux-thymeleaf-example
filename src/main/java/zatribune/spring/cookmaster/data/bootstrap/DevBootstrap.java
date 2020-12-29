package zatribune.spring.cookmaster.data.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import zatribune.spring.cookmaster.data.entities.*;
import zatribune.spring.cookmaster.data.repositories.CategoryRepository;
import zatribune.spring.cookmaster.data.repositories.RecipeRepository;
import zatribune.spring.cookmaster.data.repositories.UnitOfMeasureRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    public DevBootstrap(RecipeRepository recipeRepository, CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        log.debug("I'm at the Bootstrap phase");
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        initData();
    }

    void initData() {
        Optional<UnitOfMeasure> emptyUOM = unitOfMeasureRepository.findUnitOfMeasureByDescription("");
        Optional<UnitOfMeasure> teaspoon = unitOfMeasureRepository.findUnitOfMeasureByDescription("teaspoon");
        Optional<UnitOfMeasure> tablespoon = unitOfMeasureRepository.findUnitOfMeasureByDescription("tablespoon");
        Optional<UnitOfMeasure> cup = unitOfMeasureRepository.findUnitOfMeasureByDescription("cup");
        Optional<UnitOfMeasure> pinch = unitOfMeasureRepository.findUnitOfMeasureByDescription("pinch");
        Optional<UnitOfMeasure> ounce = unitOfMeasureRepository.findUnitOfMeasureByDescription("ounce");
        Optional<UnitOfMeasure> dash = unitOfMeasureRepository.findUnitOfMeasureByDescription("dash");

        Optional<Category> american = categoryRepository.findCategoriesByDescription("American");
        Optional<Category> italian = categoryRepository.findCategoriesByDescription("Italian");
        Optional<Category> mexican = categoryRepository.findCategoriesByDescription("Mexican");

        Recipe recipe1 = new Recipe();
        recipe1.setTitle("Perfect Guacamole");
        recipe1.setPrepTime(10);
        recipe1.setCookTime(15);
        recipe1.setNotes(new Notes("To slice open an avocado, cut it in half lengthwise with a sharp chef’s knife and twist apart the sides. One side will have the pit. To remove it, you can do one of two things:\n" +
                "\n" +
                "    Method #1: Gently tap the pit with your chef’s knife so the knife gets wedged into the pit. Twist your knife slightly to dislodge the pit and lift to remove. If you use this method, first protect your hand with a thick kitchen towel before proceeding.\n" +
                "    Method #2: Cut the side with the pit in half again, exposing more of the pit. Use your fingers or a spoon to remove the pit\n" +
                "\n" +
                "Once the pit is removed, just cut the avocado into chunks right inside the peel and use a spoon to scoop them out."));
        recipe1.setDifficulty(Difficulty.EASY);
        HashSet<Category> recipe1Categories = new HashSet<>();
        american.ifPresent(recipe1Categories::add);
        italian.ifPresent(recipe1Categories::add);
        recipe1.setCategories(recipe1Categories);

        Recipe recipe2 = new Recipe();
        recipe2.setTitle("Grilled Chicken Tacos");
        recipe2.setPrepTime(20);
        recipe2.setCookTime(25);
        recipe2.setNotes(new Notes("The ancho chiles I use in the marinade are named for their wide shape. They are large, have a deep reddish brown color when dried, and are mild in flavor with just a hint of heat. You can find ancho chile powder at any markets that sell Mexican ingredients, or online.\n" +
                "\n" +
                "I like to put all the toppings in little bowls on a big platter at the center of the table: avocados, radishes, tomatoes, red onions, wedges of lime, and a sour cream sauce. I add arugula, as well – this green isn’t traditional for tacos, but we always seem to have some in the fridge and I think it adds a nice green crunch to the tacos.\n" +
                "\n" +
                "Everyone can grab a warm tortilla from the pile and make their own tacos just they way they like them.\n" +
                "\n" +
                "You could also easily double or even triple this recipe for a larger party. A taco and a cold beer on a warm day? Now that’s living!"));
        recipe2.setDifficulty(Difficulty.MODERATE);
        HashSet<Category> recipe2Categories = new HashSet<>();
        mexican.ifPresent(recipe2Categories::add);
        recipe2.setCategories(recipe2Categories);

        if (emptyUOM.isPresent() && teaspoon.isPresent() && tablespoon.isPresent()) {
            recipe1.addIngredient(new Ingredient(BigDecimal.valueOf(2), emptyUOM.get(), "ripe advocates"))
                    .addIngredient(new Ingredient(BigDecimal.valueOf(0.25), teaspoon.get(), "salt"))
                    .addIngredient(new Ingredient(BigDecimal.valueOf(1), tablespoon.get(), "fresh lime juice or lemon juice"))
                    .addIngredient(new Ingredient(BigDecimal.valueOf(2), tablespoon.get(), "of minced red onion or thinly sliced green onion"))
                    .addIngredient(new Ingredient(BigDecimal.valueOf(1), emptyUOM.get(), "serrano chiles, stems and seeds removed, minced"))
                    .addIngredient(new Ingredient(BigDecimal.valueOf(2), tablespoon.get(), "cilantro (leaves and tender stems), finely chopped"))
                    .addIngredient(new Ingredient(BigDecimal.valueOf(1), emptyUOM.get(), "freshly grated black peppe"))
                    .addIngredient(new Ingredient(BigDecimal.valueOf(0.5), emptyUOM.get(), "ripe tomato, seeds and pulp removed, chopped"))
                    .addIngredient(new Ingredient(BigDecimal.valueOf(0), emptyUOM.get(), "Red radishes or jicama, to garnish"))
                    .addIngredient(new Ingredient(BigDecimal.valueOf(0), emptyUOM.get(), "Tortilla chips, to serve"));

            recipe2.addIngredient(new Ingredient(BigDecimal.valueOf(2), tablespoon.get(), "ancho chili powder"))
                    .addIngredient(new Ingredient(BigDecimal.valueOf(1), teaspoon.get(), "dried oregano"))
                    .addIngredient(new Ingredient(BigDecimal.valueOf(1), teaspoon.get(), "dried cumin"))
                    .addIngredient(new Ingredient(BigDecimal.valueOf(1), teaspoon.get(), "sugar"))
                    .addIngredient(new Ingredient(BigDecimal.valueOf(0.5), teaspoon.get(), "salt"))
                    .addIngredient(new Ingredient(BigDecimal.valueOf(1), emptyUOM.get(), "clove garlic, finely chopped"))
                    .addIngredient(new Ingredient(BigDecimal.valueOf(1), tablespoon.get(), "finely grated orange zest"))
                    .addIngredient(new Ingredient(BigDecimal.valueOf(3), tablespoon.get(), "fresh-squeezed orange juice"))
                    .addIngredient(new Ingredient(BigDecimal.valueOf(2), tablespoon.get(), "olive oil"))
                    .addIngredient(new Ingredient(BigDecimal.valueOf(4), emptyUOM.get(), "skinless, boneless chicken thighs "));

        }

        recipe1.setDirections("1 Cut the avocado, remove flesh: Cut the avocados in half. Remove the pit. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel an Avocado.) Place in a bowl.\n" +
                "\n" +
                "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)\n" +
                "\n" +
                "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" +
                "\n" +
                "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n" +
                "\n" +
                "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
                "\n" +
                "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.\n" +
                "\n" +
                "4 Serve: Serve immediately, or if making a few hours ahead, place plastic wrap on the surface of the guacamole and press down to cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.\n" +
                "\n");

        recipe2.setDirections("First, I marinate the chicken briefly in a spicy paste of ancho chile powder, oregano, cumin, and sweet orange juice while the grill is heating. You can also use this time to prepare the taco toppings.\n" +
                "\n" +
                "Grill the chicken, then let it rest while you warm the tortillas. Now you are ready to assemble the tacos and dig in. The whole meal comes together in about 30 minutes!\n" +
                "\n" +
                "Spicy Grilled Chicken TacosThe ancho chiles I use in the marinade are named for their wide shape. They are large, have a deep reddish brown color when dried, and are mild in flavor with just a hint of heat. You can find ancho chile powder at any markets that sell Mexican ingredients, or online.\n" +
                "\n" +
                "I like to put all the toppings in little bowls on a big platter at the center of the table: avocados, radishes, tomatoes, red onions, wedges of lime, and a sour cream sauce. I add arugula, as well – this green isn’t traditional for tacos, but we always seem to have some in the fridge and I think it adds a nice green crunch to the tacos.\n" +
                "\n" +
                "Everyone can grab a warm tortilla from the pile and make their own tacos just they way they like them.\n" +
                "\n" +
                "You could also easily double or even triple this recipe for a larger party. A taco and a cold beer on a warm day? Now that’s living!\n");

        try {
            byte[] bytes1 = DevBootstrap.class.getResourceAsStream("/images/guacamole-perfect.jpg").readAllBytes();
            String string1 = Base64.getEncoder().encodeToString(bytes1);
            recipe1.setImage(string1);
            byte[] bytes2 = DevBootstrap.class.getResourceAsStream("/images/spicy-grilled-chicken-tacos.jpg").readAllBytes();
            String string2 = Base64.getEncoder().encodeToString(bytes2);
            recipe2.setImage(string2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        recipeRepository.saveAll(Arrays.asList(recipe1,recipe2));
    }
}
