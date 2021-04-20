package zatribune.spring.kitchenmaster.data.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import zatribune.spring.kitchenmaster.data.entities.*;
import zatribune.spring.kitchenmaster.data.repositories.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private final RecipeReactiveRepository recipeRepository;
    private final CategoryReactiveRepository categoryRepository;
    private final UnitMeasureReactiveRepository unitMeasureRepository;
    private final UserReactiveRepository userRepository;
    private final IngredientReactiveRepository ingredientRepository;
    private final RoleReactiveRepository roleRepository;

    @Autowired
    public DevBootstrap(RecipeReactiveRepository recipeRepository, CategoryReactiveRepository categoryRepository,
                        UnitMeasureReactiveRepository unitMeasureRepository,IngredientReactiveRepository ingredientRepository, UserReactiveRepository userRepository
            , RoleReactiveRepository roleRepository) {
        log.debug(getClass().getSimpleName()+":I'm at the Bootstrap phase.");
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.unitMeasureRepository = unitMeasureRepository;
        this.ingredientRepository=ingredientRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(@Nullable ContextRefreshedEvent contextRefreshedEvent) {
        clearOldData();
        initData();
        initAccess();
    }

    void clearOldData(){
        recipeRepository.deleteAll().subscribe();
        categoryRepository.deleteAll().subscribe();
        unitMeasureRepository.deleteAll().subscribe();
        userRepository.deleteAll().subscribe();
        roleRepository.deleteAll().subscribe();
    }


    void initData() {
        log.info("initData()");
        UnitMeasure emptyUOM = new UnitMeasure();
        emptyUOM.setDescription("");
        UnitMeasure teaspoon = new UnitMeasure();
        teaspoon.setDescription("teaspoon");
        UnitMeasure tablespoon = new UnitMeasure();
        tablespoon.setDescription("tablespoon");
        UnitMeasure cup = new UnitMeasure();
        cup.setDescription("cup");
        UnitMeasure pinch = new UnitMeasure();
        pinch.setDescription("pinch");
        UnitMeasure ounce = new UnitMeasure();
        ounce.setDescription("ounce");
        UnitMeasure dash = new UnitMeasure();
        dash.setDescription("dash");

        unitMeasureRepository.saveAll(List.of(emptyUOM, teaspoon, tablespoon, cup, pinch, ounce, dash)).subscribe();

        Category american = new Category();
        american.setDescription("American");
        american.setInfo("From the home of fast foods and bad ass recipes,.......");
        Category italian = new Category();
        italian.setDescription("Italian");
        italian.setInfo("From the home of lazania and bad ass recipes,.......");
        Category mexican = new Category();
        mexican.setDescription("Mexican");
        mexican.setInfo("From the home of spicy foods and bad ass recipes,.......");

        try {
            byte[] usaBytes = getClass().getResourceAsStream("/static/images/usa.png").readAllBytes();
            byte[] italyBytes = getClass().getResourceAsStream("/static/images/italy.png").readAllBytes();
            byte[] mexicoBytes = getClass().getResourceAsStream("/static/images/mexico.png").readAllBytes();
            Byte[] usaImage = new Byte[usaBytes.length];
            Byte[] italyImage = new Byte[italyBytes.length];
            Byte[] mexicoImage = new Byte[mexicoBytes.length];
            int i = 0;
            for (byte b : usaBytes)
                usaImage[i++] = b;
            i = 0;
            for (byte b : italyBytes)
                italyImage[i++] = b;
            i = 0;
            for (byte b : mexicoBytes)
                mexicoImage[i++] = b;

            american.setImage(usaImage);
            italian.setImage(italyImage);
            mexican.setImage(mexicoImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
            /*
             The method readAllBytes() Reads all remaining bytes from the input stream.
             This method blocks until all remaining bytes have been read and end of stream is detected,
             or an exception is thrown.
             The method subscribe() will Subscribe to this Flux and request unbounded demand.
             This version doesn't specify any consumption behavior for the events from the chain,
             especially no error handling, so other variants should usually be preferred.
             the method block() will Subscribe to this Mono and block indefinitely until a next signal is received.
            * */

        categoryRepository.saveAll(Arrays.asList(american, italian, mexican)).collectList().block();

        Recipe recipe1 = new Recipe();
        recipe1.setTitle("Perfect Guacamole");
        recipe1.setPrepTime(10);
        recipe1.setCookTime(15);
        Notes notes1 = new Notes("To slice open an avocado, cut it in half lengthwise with a sharp chef’s knife and twist apart the sides. One side will have the pit. To remove it, you can do one of two things:\n" +
                "\n" +
                "    Method #1: Gently tap the pit with your chef’s knife so the knife gets wedged into the pit. Twist your knife slightly to dislodge the pit and lift to remove. If you use this method, first protect your hand with a thick kitchen towel before proceeding.\n" +
                "    Method #2: Cut the side with the pit in half again, exposing more of the pit. Use your fingers or a spoon to remove the pit\n" +
                "\n" +
                "Once the pit is removed, just cut the avocado into chunks right inside the peel and use a spoon to scoop them out.");
        recipe1.setNotes(notes1);
        recipe1.setDifficulty(Difficulty.EASY);
        recipe1.setServings(4);

        Recipe recipe2 = new Recipe();
        recipe2.setTitle("Grilled Chicken Tacos");
        recipe2.setPrepTime(20);
        recipe2.setCookTime(25);
        Notes notes2 = new Notes("The ancho chiles I use in the marinade are named for their wide shape. They are large, have a deep reddish brown color when dried, and are mild in flavor with just a hint of heat. You can find ancho chile powder at any markets that sell Mexican ingredients, or online.\n" +
                "\n" +
                "I like to put all the toppings in little bowls on a big platter at the center of the table: avocados, radishes, tomatoes, red onions, wedges of lime, and a sour cream sauce. I add arugula, as well – this green isn’t traditional for tacos, but we always seem to have some in the fridge and I think it adds a nice green crunch to the tacos.\n" +
                "\n" +
                "Everyone can grab a warm tortilla from the pile and make their own tacos just they way they like them.\n" +
                "\n" +
                "You could also easily double or even triple this recipe for a larger party. A taco and a cold beer on a warm day? Now that’s living!");
        recipe2.setNotes(notes2);
        recipe2.setDifficulty(Difficulty.MODERATE);
        recipe2.setServings(3);

        List<Ingredient>ingredients1=List.of(
                new Ingredient(BigDecimal.valueOf(2), emptyUOM, "ripe advocates"),
                new Ingredient(BigDecimal.valueOf(0.25), teaspoon, "salt"),
                new Ingredient(BigDecimal.valueOf(1), tablespoon, "fresh lime juice or lemon juice"),
                new Ingredient(BigDecimal.valueOf(2), tablespoon, "of minced red onion or thinly sliced green onion"),
                new Ingredient(BigDecimal.valueOf(1), emptyUOM, "serrano chiles, stems and seeds removed, minced"),
                new Ingredient(BigDecimal.valueOf(2), tablespoon, "cilantro (leaves and tender stems), finely chopped"),
                new Ingredient(BigDecimal.valueOf(1), emptyUOM, "freshly grated black peppe"),
                new Ingredient(BigDecimal.valueOf(0.5), emptyUOM, "ripe tomato, seeds and pulp removed, chopped"),
                new Ingredient(BigDecimal.valueOf(0), emptyUOM, "Red radishes or jicama, to garnish"),
                new Ingredient(BigDecimal.valueOf(0), emptyUOM, "Tortilla chips, to serve"));

        List<Ingredient>ingredients2=List.of(
                new Ingredient(BigDecimal.valueOf(2), tablespoon, "ancho chili powder"),
                new Ingredient(BigDecimal.valueOf(1), teaspoon, "dried oregano"),
                new Ingredient(BigDecimal.valueOf(1), teaspoon, "dried cumin"),
                new Ingredient(BigDecimal.valueOf(1), teaspoon, "sugar"),
                new Ingredient(BigDecimal.valueOf(0.5), teaspoon, "salt"),
                new Ingredient(BigDecimal.valueOf(1), emptyUOM, "clove garlic, finely chopped"),
                new Ingredient(BigDecimal.valueOf(1), tablespoon, "finely grated orange zest"),
                new Ingredient(BigDecimal.valueOf(3), tablespoon, "fresh-squeezed orange juice"),
                new Ingredient(BigDecimal.valueOf(2), tablespoon, "olive oil"),
                new Ingredient(BigDecimal.valueOf(4), emptyUOM, "skinless, boneless chicken thighs ")
        );

        ingredientRepository.saveAll(ingredients1).subscribe();
        ingredientRepository.saveAll(ingredients2).subscribe();


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
            byte[] bytes1 = DevBootstrap.class.getResourceAsStream("/static/images/guacamole-perfect.jpg").readAllBytes();
            String string1 = Base64.getEncoder().encodeToString(bytes1);
            recipe1.setImage("data:image/png;base64," + string1);
            byte[] bytes2 = DevBootstrap.class.getResourceAsStream("/static/images/spicy-grilled-chicken-tacos.jpg").readAllBytes();
            String string2 = Base64.getEncoder().encodeToString(bytes2);
            recipe2.setImage("data:image/png;base64," + string2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        recipe1.setCategories(new HashSet<>(List.of(american,italian)));
        recipe2.setCategories(new HashSet<>(List.of(mexican)));
        recipe1.setIngredients(new HashSet<>(ingredients1));
        recipe2.setIngredients(new HashSet<>(ingredients2));

        recipeRepository.saveAll(List.of(recipe1, recipe2)).subscribe();
    }

    void initAccess() {
        log.debug("initAccess");
        Role role1 = new Role();
        role1.setName("ROLE_ADMIN");

        Role role2 = new Role();
        role2.setName("ROLE_USER");

        User user1 = new User();
        user1.setUsername("user1@gmail.com");
        //strength 12 and input= 'pass'
        user1.setPassword("$2y$12$bwUGNLEwKKGA/CleGetPIOfJGGZsB9Ymj0KOmJirJ/pn/wdSQVfie");
        user1.setPasswordConfirm("$2y$12$bwUGNLEwKKGA/CleGetPIOfJGGZsB9Ymj0KOmJirJ/pn/wdSQVfie");
        user1.setAccountNonExpired(Boolean.TRUE);
        user1.setAccountNonLocked(Boolean.TRUE);
        user1.setCredentialsNotExpired(Boolean.TRUE);
        user1.setEnabled(Boolean.TRUE);


        User user2 = new User();
        user2.setUsername("user2@gmail.com");
        //strength 12 and input= 'pass'
        user2.setPassword("$2y$12$bwUGNLEwKKGA/CleGetPIOfJGGZsB9Ymj0KOmJirJ/pn/wdSQVfie");
        user2.setPasswordConfirm("$2y$12$bwUGNLEwKKGA/CleGetPIOfJGGZsB9Ymj0KOmJirJ/pn/wdSQVfie");
        user2.setAccountNonExpired(Boolean.TRUE);
        user2.setAccountNonLocked(Boolean.TRUE);
        user2.setCredentialsNotExpired(Boolean.TRUE);
        user2.setEnabled(Boolean.TRUE);

        User user3 = new User();
        user3.setUsername("user3@gmail.com");
        //strength 12 and input= 'pass'
        user3.setPassword("$2y$12$bwUGNLEwKKGA/CleGetPIOfJGGZsB9Ymj0KOmJirJ/pn/wdSQVfie");
        user3.setPasswordConfirm("$2y$12$bwUGNLEwKKGA/CleGetPIOfJGGZsB9Ymj0KOmJirJ/pn/wdSQVfie");
        user3.setAccountNonExpired(Boolean.TRUE);
        user3.setAccountNonLocked(Boolean.TRUE);
        user3.setCredentialsNotExpired(Boolean.TRUE);
        user3.setEnabled(Boolean.TRUE);

        //save first entities
        roleRepository.saveAll(Arrays.asList(role1, role2)).collectList().block();
        //in mongo, we Cannot create a reference to an object with a NULL id.
        user1.setRoles(new HashSet<>(Collections.singletonList(role1)));
        user2.setRoles(new HashSet<>(Collections.singletonList(role2)));
        user3.setRoles(new HashSet<>(Collections.singletonList(role2)));
        //save the second entities + save the relationship
        userRepository.saveAll(List.of(user1, user2, user3)).subscribe();
    }
}
