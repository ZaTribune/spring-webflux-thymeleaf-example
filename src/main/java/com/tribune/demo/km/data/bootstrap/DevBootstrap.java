package com.tribune.demo.km.data.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import com.tribune.demo.km.data.entity.*;
import com.tribune.demo.km.data.repository.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Profile("dev")
@Component
public record DevBootstrap(RecipeReactiveRepository recipeRepository,
                           CategoryReactiveRepository categoryRepository,
                           UnitMeasureReactiveRepository unitMeasureRepository,
                           UserReactiveRepository userRepository,
                           IngredientReactiveRepository ingredientRepository,
                           RoleReactiveRepository roleRepository) implements CommandLineRunner {


    @Override
    public void run(String @NonNull ... args) {
        clearOldData();
        initData();
        initAccess();
    }

    void clearOldData() {
        recipeRepository.deleteAll().block();
        categoryRepository.deleteAll().block();
        unitMeasureRepository.deleteAll().block();
        userRepository.deleteAll().block();
        roleRepository.deleteAll().block();
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

        unitMeasureRepository.saveAll(List.of(emptyUOM, teaspoon, tablespoon, cup, pinch, ounce, dash)).collectList().block();

        Category american = new Category();
        american.setDescription("American");
        american.setInfo("From the home of fast foods and diabetic recipes,.......");
        Category italian = new Category();
        italian.setDescription("Italian");
        italian.setInfo("From the home of pizza,.......");
        Category mexican = new Category();
        mexican.setDescription("Mexican");
        mexican.setInfo("From the home of spicy foods,.......");

        try {
            byte[] usaBytes = getClass().getResourceAsStream("/static/images/usa.png").readAllBytes();
            byte[] italyBytes = getClass().getResourceAsStream("/static/images/italy.png").readAllBytes();
            byte[] mexicoBytes = getClass().getResourceAsStream("/static/images/mexico.png").readAllBytes();

            american.setImage(usaBytes);
            italian.setImage(italyBytes);
            mexican.setImage(mexicoBytes);
        } catch (IOException e) {
            log.error("Failed to load category image", e);
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
        // Create all 17 recipes
        Recipe recipe1 = createGuacamoleRecipe(teaspoon, tablespoon, emptyUOM, american, italian);
        Recipe recipe2 = createChickenTacosRecipe(teaspoon, tablespoon, emptyUOM, mexican);
        Recipe recipe3 = createPastaCarbonaraRecipe(teaspoon, tablespoon, cup, emptyUOM);
        Recipe recipe4 = createBeefStirFryRecipe(teaspoon, tablespoon, cup, emptyUOM);
        Recipe recipe5 = createSalmonTeriyakiRecipe(teaspoon, tablespoon, cup, emptyUOM);
        Recipe recipe6 = createMargheritaPizzaRecipe(teaspoon, tablespoon, cup, emptyUOM);
        Recipe recipe7 = createThaiGreenCurryRecipe(teaspoon, tablespoon, cup, emptyUOM);
        Recipe recipe8 = createHamburgerRecipe(teaspoon, cup, emptyUOM);
        Recipe recipe9 = createPadThaiRecipe(tablespoon, cup, ounce, emptyUOM);
        Recipe recipe10 = createRisottoRecipe(tablespoon, cup, emptyUOM);
        Recipe recipe11 = createFajitasRecipe(teaspoon, tablespoon, emptyUOM);
        Recipe recipe12 = createSushiRollsRecipe(teaspoon, tablespoon, emptyUOM);
        Recipe recipe13 = createVegetarianBowlRecipe(teaspoon, tablespoon, cup, emptyUOM);
        Recipe recipe14 = createSpaghettiCarbonara2Recipe(teaspoon, tablespoon, cup, emptyUOM);
        Recipe recipe15 = createChickenAlfredoRecipe(teaspoon, tablespoon, cup, emptyUOM);
        Recipe recipe16 = createBeefTacoRecipe(teaspoon, tablespoon, cup, emptyUOM);
        Recipe recipe17 = createShrimpScampiRecipe(teaspoon, tablespoon, cup, emptyUOM);

        recipeRepository.saveAll(List.of(recipe1, recipe2, recipe3, recipe4, recipe5, recipe6, recipe7, recipe8, recipe9, recipe10, recipe11, recipe12, recipe13, recipe14, recipe15, recipe16, recipe17))
                .collectList().block();
    }

    private Recipe createGuacamoleRecipe(UnitMeasure teaspoon, UnitMeasure tablespoon, UnitMeasure emptyUOM, Category american, Category italian) {
        List<Ingredient> ingredients = List.of(
                new Ingredient(BigDecimal.valueOf(2), emptyUOM, "ripe advocates"),
                new Ingredient(BigDecimal.valueOf(0.25), teaspoon, "salt"),
                new Ingredient(BigDecimal.valueOf(1), tablespoon, "fresh lime juice or lemon juice"),
                new Ingredient(BigDecimal.valueOf(2), tablespoon, "of minced red onion or thinly sliced green onion"),
                new Ingredient(BigDecimal.valueOf(1), emptyUOM, "serrano chillies, stems and seeds removed, minced"),
                new Ingredient(BigDecimal.valueOf(2), tablespoon, "cilantro (leaves and tender stems), finely chopped"),
                new Ingredient(BigDecimal.valueOf(1), emptyUOM, "freshly grated black pepper"),
                new Ingredient(BigDecimal.valueOf(0.5), emptyUOM, "ripe tomato, seeds and pulp removed, chopped"),
                new Ingredient(BigDecimal.valueOf(0), emptyUOM, "Red radishes or jicama, to garnish"),
                new Ingredient(BigDecimal.valueOf(0), emptyUOM, "Tortilla chips, to serve"));

        ingredientRepository.saveAll(ingredients).collectList().block();

        Recipe recipe = new Recipe();
        recipe.setTitle("Perfect Guacamole");
        recipe.setPrepTime(10);
        recipe.setCookTime(15);
        Notes notes = new Notes("""
                To slice open an avocado, cut it in half lengthwise with a sharp chef's knife and twist apart the sides.
                One side will have the pit. To remove it, you can do one of two things:
                    Method #1: Gently tap the pit with your chef's knife so the knife gets wedged into the pit.
                    Twist your knife slightly to dislodge the pit and lift to remove.
                    If you use this method, first protect your hand with a thick kitchen towel before proceeding.
                    Method #2: Cut the side with the pit in half again, exposing more of the pit. Use your fingers or a spoon to remove the pit
                Once the pit is removed, just cut the avocado into chunks right inside the peel and use a spoon to scoop them out.""");
        recipe.setNotes(notes);
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setServings(4);
        recipe.setDirections("""
                1 Cut the avocado, remove flesh: Cut the avocados in half. Remove the pit.
                Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel an Avocado.) Place in a bowl.
                2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)
                3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice.
                The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.
                Add the chopped onion, cilantro, black pepper, and chillies. Chili peppers vary individually in their hotness.
                So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.
                Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.
                Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.
                4 Serve: Serve immediately, or if making a few hours ahead, place plastic wrap on the surface of the guacamole and press down to cover it and to prevent air reaching it. 
                (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.
                """);

        try {
            byte[] bytes = DevBootstrap.class.getResourceAsStream("/static/images/guacamole-perfect.jpg").readAllBytes();
            String string = Base64.getEncoder().encodeToString(bytes);
            recipe.setImage("data:image/png;base64," + string);
        } catch (Exception e) {
            log.error("Failed to load recipe image", e);
        }

        recipe.setCategories(new HashSet<>(List.of(american, italian)));
        recipe.setIngredients(new HashSet<>(ingredients));

        return recipe;
    }

    private Recipe createChickenTacosRecipe(UnitMeasure teaspoon, UnitMeasure tablespoon, UnitMeasure emptyUOM, Category mexican) {
        List<Ingredient> ingredients = List.of(
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

        ingredientRepository.saveAll(ingredients).collectList().block();

        Recipe recipe = new Recipe();
        recipe.setTitle("Grilled Chicken Tacos");
        recipe.setPrepTime(20);
        recipe.setCookTime(25);
        Notes notes = new Notes("""
                The ancho chillies I use in the marinade are named for their wide shape.
                They are large, have a deep reddish brown color when dried, and are mild in flavor with just a hint of heat.
                You can find ancho chillie powder at any markets that sell Mexican ingredients, or online.
                I like to put all the toppings in little bowls on a big platter at the center of the table: avocados, radishes, tomatoes, red onions, wedges of lime, and a sour cream sauce.
                I add arugula, as well – this green isn’t traditional for tacos, but we always seem to have some in the fridge and I think it adds a nice green crunch to the tacos.
                Everyone can grab a warm tortilla from the pile and make their own tacos just they way they like them.
                You could also easily double or even triple this recipe for a larger party. A taco and a cold beer on a warm day? Now that’s living!""");
        recipe.setNotes(notes);
        recipe.setDifficulty(Difficulty.MODERATE);
        recipe.setServings(3);
        recipe.setDirections("""
                First, I marinate the chicken briefly in a spicy paste of ancho chilly powder, oregano, cumin, and sweet orange juice while the grill is heating.
                You can also use this time to prepare the taco toppings.
                Grill the chicken, then let it rest while you warm the tortillas. Now you are ready to assemble the tacos and dig in.
                The whole meal comes together in about 30 minutes!
                Spicy Grilled Chicken TacosThe ancho chillies I use in the marinade are named for their wide shape.
                They are large, have a deep reddish brown color when dried, and are mild in flavor with just a hint of heat.
                You can find ancho chilly powder at any markets that sell Mexican ingredients, or online.
                I like to put all the toppings in little bowls on a big platter at the center of the table: avocados, radishes, tomatoes, red onions, wedges of lime, and a sour cream sauce.
                I add arugula, as well – this green isn't traditional for tacos, but we always seem to have some in the fridge and I think it adds a nice green crunch to the tacos.
                Everyone can grab a warm tortilla from the pile and make their own tacos just they way they like them.
                You could also easily double or even triple this recipe for a larger party. A taco and a cold beer on a warm day? Now that's living!
                """);

        try {
            byte[] bytes = DevBootstrap.class.getResourceAsStream("/static/images/spicy-grilled-chicken-tacos.jpg").readAllBytes();
            String string = Base64.getEncoder().encodeToString(bytes);
            recipe.setImage("data:image/png;base64," + string);
        } catch (Exception e) {
            log.error("Failed to load recipe image", e);
        }

        recipe.setCategories(new HashSet<>(List.of(mexican)));
        recipe.setIngredients(new HashSet<>(ingredients));

        return recipe;
    }

    private Recipe createPastaCarbonaraRecipe(UnitMeasure teaspoon, UnitMeasure tablespoon, UnitMeasure cup, UnitMeasure emptyUOM) {
        List<Ingredient> ingredients = List.of(
                new Ingredient(BigDecimal.valueOf(1), emptyUOM, "pound spaghetti"),
                new Ingredient(BigDecimal.valueOf(6), emptyUOM, "ounce pancetta, diced"),
                new Ingredient(BigDecimal.valueOf(4), emptyUOM, "eggs"),
                new Ingredient(BigDecimal.valueOf(2), cup, "Pecorino Romano, grated"),
                new Ingredient(BigDecimal.valueOf(2), teaspoon, "black pepper"),
                new Ingredient(BigDecimal.valueOf(1), teaspoon, "salt")
        );
        return buildRecipe("Pasta Carbonara", ingredients, 15, 20, 4, Difficulty.EASY,
                "1. Cook pasta in salted water\n2. Fry pancetta until crispy\n3. Mix eggs and cheese in a bowl\n4. Toss hot pasta with egg mixture off heat\n5. Add pancetta and pepper\n6. Serve immediately",
                "Roman classic - timing is crucial to get creamy sauce without scrambling eggs", "pasta-carbonara.jpg");
    }

    private Recipe createBeefStirFryRecipe(UnitMeasure teaspoon, UnitMeasure tablespoon, UnitMeasure cup, UnitMeasure emptyUOM) {
        List<Ingredient> ingredients = List.of(
                new Ingredient(BigDecimal.valueOf(1.5), emptyUOM, "pound beef sirloin, sliced thin"),
                new Ingredient(BigDecimal.valueOf(3), tablespoon, "soy sauce"),
                new Ingredient(BigDecimal.valueOf(1), tablespoon, "sesame oil"),
                new Ingredient(BigDecimal.valueOf(2), emptyUOM, "garlic cloves, minced"),
                new Ingredient(BigDecimal.valueOf(1), tablespoon, "ginger, minced"),
                new Ingredient(BigDecimal.valueOf(3), cup, "bell peppers, sliced"),
                new Ingredient(BigDecimal.valueOf(2), cup, "broccoli florets"),
                new Ingredient(BigDecimal.valueOf(2), tablespoon, "vegetable oil")
        );
        return buildRecipe("Beef Stir Fry", ingredients, 20, 15, 4, Difficulty.MODERATE,
                "1. Heat wok until smoking\n2. Cook beef in batches, set aside\n3. Stir fry vegetables\n4. Return beef and add sauce\n5. Toss and serve over rice",
                "Quick Asian weeknight meal - prep all ingredients before cooking", "beef-stir-fry.jpg");
    }

    private Recipe createSalmonTeriyakiRecipe(UnitMeasure teaspoon, UnitMeasure tablespoon, UnitMeasure cup, UnitMeasure emptyUOM) {
        List<Ingredient> ingredients = List.of(
                new Ingredient(BigDecimal.valueOf(4), emptyUOM, "6oz salmon fillets"),
                new Ingredient(BigDecimal.valueOf(0.5), cup, "soy sauce"),
                new Ingredient(BigDecimal.valueOf(0.25), cup, "mirin"),
                new Ingredient(BigDecimal.valueOf(2), tablespoon, "honey"),
                new Ingredient(BigDecimal.valueOf(2), emptyUOM, "garlic cloves, minced"),
                new Ingredient(BigDecimal.valueOf(1), tablespoon, "ginger, grated"),
                new Ingredient(BigDecimal.valueOf(1), teaspoon, "sesame seeds"),
                new Ingredient(BigDecimal.valueOf(2), emptyUOM, "green onions, sliced")
        );
        return buildRecipe("Salmon Teriyaki", ingredients, 15, 20, 2, Difficulty.MODERATE,
                "1. Mix teriyaki sauce ingredients\n2. Preheat oven to 400F\n3. Place salmon on foil\n4. Brush with sauce\n5. Bake 12-15 minutes\n6. Garnish with sesame and green onions",
                "Elegant and healthy Japanese-inspired dish", "salmon-teriyaki.jpg");
    }

    private Recipe createMargheritaPizzaRecipe(UnitMeasure teaspoon, UnitMeasure tablespoon, UnitMeasure cup, UnitMeasure emptyUOM) {
        List<Ingredient> ingredients = List.of(
                new Ingredient(BigDecimal.valueOf(1), emptyUOM, "pizza dough ball"),
                new Ingredient(BigDecimal.valueOf(1), cup, "tomato sauce"),
                new Ingredient(BigDecimal.valueOf(8), emptyUOM, "ounce fresh mozzarella"),
                new Ingredient(BigDecimal.valueOf(0.5), cup, "Parmesan, grated"),
                new Ingredient(BigDecimal.valueOf(15), emptyUOM, "fresh basil leaves"),
                new Ingredient(BigDecimal.valueOf(2), tablespoon, "olive oil"),
                new Ingredient(BigDecimal.valueOf(1), teaspoon, "sea salt")
        );
        return buildRecipe("Margherita Pizza", ingredients, 30, 15, 4, Difficulty.MODERATE,
                "1. Preheat oven to 475F\n2. Stretch dough to fit pizza pan\n3. Spread tomato sauce\n4. Add fresh mozzarella pieces\n5. Bake 12-15 minutes until crust is golden\n6. Top with fresh basil and drizzle of olive oil",
                "Simple Italian pizza - quality ingredients make this dish", "margherita-pizza.jpg");
    }

    private Recipe createThaiGreenCurryRecipe(UnitMeasure teaspoon, UnitMeasure tablespoon, UnitMeasure cup, UnitMeasure emptyUOM) {
        List<Ingredient> ingredients = List.of(
                new Ingredient(BigDecimal.valueOf(1), tablespoon, "green curry paste"),
                new Ingredient(BigDecimal.valueOf(1), cup, "coconut milk"),
                new Ingredient(BigDecimal.valueOf(1), emptyUOM, "pound chicken breast, cubed"),
                new Ingredient(BigDecimal.valueOf(2), emptyUOM, "bell peppers, sliced"),
                new Ingredient(BigDecimal.valueOf(1), cup, "Thai basil"),
                new Ingredient(BigDecimal.valueOf(3), tablespoon, "fish sauce"),
                new Ingredient(BigDecimal.valueOf(1), tablespoon, "lime juice"),
                new Ingredient(BigDecimal.valueOf(2), tablespoon, "oil")
        );

        return buildRecipe("Thai Green Curry", ingredients, 25, 20, 4, Difficulty.HARD,
                "1. Heat oil and fry curry paste\n2. Add coconut milk gradually\n3. Add chicken and simmer\n4. Add peppers and cook until tender\n5. Add basil, fish sauce, and lime juice\n6. Serve with jasmine rice",
                "Aromatic and creamy Thai curry - adjust spice level to taste", "thai-green-curry.jpg");
    }

    private Recipe createHamburgerRecipe(UnitMeasure teaspoon, UnitMeasure cup, UnitMeasure emptyUOM) {
        List<Ingredient> ingredients = List.of(
                new Ingredient(BigDecimal.valueOf(1), emptyUOM, "pound ground beef"),
                new Ingredient(BigDecimal.valueOf(0.25), cup, "breadcrumbs"),
                new Ingredient(BigDecimal.valueOf(1), emptyUOM, "egg"),
                new Ingredient(BigDecimal.valueOf(4), emptyUOM, "hamburger buns"),
                new Ingredient(BigDecimal.valueOf(4), emptyUOM, "ounce cheddar cheese slices"),
                new Ingredient(BigDecimal.valueOf(4), emptyUOM, "lettuce leaves"),
                new Ingredient(BigDecimal.valueOf(4), emptyUOM, "tomato slices"),
                new Ingredient(BigDecimal.valueOf(0.5), teaspoon, "salt and pepper")
        );
        return buildRecipe("Classic Hamburger", ingredients, 15, 10, 4, Difficulty.EASY,
                "1. Mix beef with breadcrumbs and egg\n2. Form 4 patties gently\n3. Grill 3-4 minutes per side for medium\n4. Place cheese on last minute\n5. Toast buns and assemble with toppings",
                "American backyard classic - don't overwork the meat", "classic-hamburger.jpg");
    }

    private Recipe createPadThaiRecipe(UnitMeasure tablespoon, UnitMeasure cup, UnitMeasure ounce, UnitMeasure emptyUOM) {
        List<Ingredient> ingredients = List.of(
                new Ingredient(BigDecimal.valueOf(8), ounce, "rice noodles"),
                new Ingredient(BigDecimal.valueOf(0.5), emptyUOM, "pound shrimp"),
                new Ingredient(BigDecimal.valueOf(3), tablespoon, "tamarind paste"),
                new Ingredient(BigDecimal.valueOf(3), tablespoon, "fish sauce"),
                new Ingredient(BigDecimal.valueOf(2), tablespoon, "palm sugar"),
                new Ingredient(BigDecimal.valueOf(3), emptyUOM, "garlic cloves, minced"),
                new Ingredient(BigDecimal.valueOf(2), cup, "bean sprouts"),
                new Ingredient(BigDecimal.valueOf(3), emptyUOM, "green onions, chopped")
        );
        return buildRecipe("Pad Thai", ingredients, 20, 15, 4, Difficulty.MODERATE,
                "1. Soak rice noodles in warm water\n2. Heat wok and stir-fry garlic and shrimp\n3. Add drained noodles\n4. Mix sauce and add to wok\n5. Toss in bean sprouts and green onions\n6. Serve with lime and crushed peanuts",
                "Thai street food favorite - balance of sweet, sour, salty, spicy", "pad-thai.jpg");
    }

    private Recipe createRisottoRecipe(UnitMeasure tablespoon, UnitMeasure cup, UnitMeasure emptyUOM) {
        List<Ingredient> ingredients = List.of(
                new Ingredient(BigDecimal.valueOf(1.5), cup, "arborio rice"),
                new Ingredient(BigDecimal.valueOf(4), cup, "vegetable broth, warm"),
                new Ingredient(BigDecimal.valueOf(1), emptyUOM, "onion, diced"),
                new Ingredient(BigDecimal.valueOf(0.5), cup, "white wine"),
                new Ingredient(BigDecimal.valueOf(0.5), emptyUOM, "saffron strands"),
                new Ingredient(BigDecimal.valueOf(0.5), cup, "Parmesan, grated"),
                new Ingredient(BigDecimal.valueOf(2), tablespoon, "butter"),
                new Ingredient(BigDecimal.valueOf(2), tablespoon, "olive oil")
        );
        return buildRecipe("Risotto Milanese", ingredients, 15, 30, 4, Difficulty.HARD,
                "1. Heat broth in separate pot\n2. Sauté onion in olive oil\n3. Toast rice for 1-2 minutes\n4. Add wine and stir until absorbed\n5. Add broth one ladle at a time, stirring constantly\n6. When rice is creamy, remove from heat\n7. Stir in butter, cheese, and saffron",
                "Creamy Italian rice dish requiring patience and constant attention", "risotto-milanese.jpg");
    }

    private Recipe createFajitasRecipe(UnitMeasure teaspoon, UnitMeasure tablespoon, UnitMeasure emptyUOM) {
        List<Ingredient> ingredients = List.of(
                new Ingredient(BigDecimal.valueOf(1.5), emptyUOM, "pound chicken breast, sliced"),
                new Ingredient(BigDecimal.valueOf(2), emptyUOM, "bell peppers, sliced"),
                new Ingredient(BigDecimal.valueOf(1), emptyUOM, "onion, sliced"),
                new Ingredient(BigDecimal.valueOf(3), tablespoon, "lime juice"),
                new Ingredient(BigDecimal.valueOf(2), tablespoon, "olive oil"),
                new Ingredient(BigDecimal.valueOf(8), emptyUOM, "flour tortillas"),
                new Ingredient(BigDecimal.valueOf(1), teaspoon, "cumin"),
                new Ingredient(BigDecimal.valueOf(1), teaspoon, "chili powder")
        );
        return buildRecipe("Chicken Fajitas", ingredients, 20, 15, 4, Difficulty.EASY,
                "1. Mix marinade with cumin, chili powder, and lime juice\n2. Marinate chicken for 15 minutes\n3. Heat cast iron skillet\n4. Cook chicken until done, remove\n5. Cook peppers and onions\n6. Serve with warm tortillas and toppings",
                "Interactive Mexican dining experience - build your own", "chicken-fajitas.jpg");
    }

    private Recipe createSushiRollsRecipe(UnitMeasure teaspoon, UnitMeasure tablespoon, UnitMeasure emptyUOM) {
        List<Ingredient> ingredients = List.of(
                new Ingredient(BigDecimal.valueOf(2), emptyUOM, "cups sushi rice"),
                new Ingredient(BigDecimal.valueOf(4), emptyUOM, "nori sheets"),
                new Ingredient(BigDecimal.valueOf(0.5), emptyUOM, "pound imitation crab"),
                new Ingredient(BigDecimal.valueOf(1), emptyUOM, "cucumber, julienned"),
                new Ingredient(BigDecimal.valueOf(1), emptyUOM, "avocado, sliced"),
                new Ingredient(BigDecimal.valueOf(3), tablespoon, "rice vinegar"),
                new Ingredient(BigDecimal.valueOf(3), tablespoon, "soy sauce"),
                new Ingredient(BigDecimal.valueOf(1), teaspoon, "wasabi")
        );
        return buildRecipe("California Sushi Rolls", ingredients, 30, 0, 4, Difficulty.HARD,
                "1. Season sushi rice with vinegar and sugar\n2. Place nori on bamboo mat\n3. Spread thin layer of rice\n4. Add fillings in the middle\n5. Roll tightly using mat\n6. Slice with sharp, wet knife\n7. Serve with soy sauce and wasabi",
                "Beginner-friendly inside-out rolls - practice makes perfect", "california-sushi-rolls.jpg");
    }

    private Recipe createVegetarianBowlRecipe(UnitMeasure teaspoon, UnitMeasure tablespoon, UnitMeasure cup, UnitMeasure emptyUOM) {
        List<Ingredient> ingredients = List.of(
                new Ingredient(BigDecimal.valueOf(2), cup, "quinoa, cooked"),
                new Ingredient(BigDecimal.valueOf(1), emptyUOM, "sweet potato, roasted"),
                new Ingredient(BigDecimal.valueOf(1), emptyUOM, "chickpeas"),
                new Ingredient(BigDecimal.valueOf(1), cup, "kale, chopped"),
                new Ingredient(BigDecimal.valueOf(2), tablespoon, "tahini dressing")
        );
        ingredientRepository.saveAll(ingredients).collectList().block();
        return buildRecipe("Veggie Power Bowl", ingredients, 15, 20, 2, Difficulty.EASY,
                "1. Cook quinoa\n2. Roast vegetables\n3. Assemble bowl\n4. Drizzle with tahini",
                "Healthy and nutritious bowl", "vegetarian-bowl.jpg");
    }

    private Recipe createSpaghettiCarbonara2Recipe(UnitMeasure teaspoon, UnitMeasure tablespoon, UnitMeasure cup, UnitMeasure emptyUOM) {
        List<Ingredient> ingredients = List.of(
                new Ingredient(BigDecimal.valueOf(1), emptyUOM, "pound spaghetti"),
                new Ingredient(BigDecimal.valueOf(6), emptyUOM, "ounce guanciale"),
                new Ingredient(BigDecimal.valueOf(4), emptyUOM, "eggs"),
                new Ingredient(BigDecimal.valueOf(1), cup, "Pecorino Romano")
        );
        ingredientRepository.saveAll(ingredients).collectList().block();
        return buildRecipe("Classic Spaghetti Carbonara", ingredients, 10, 20, 4, Difficulty.EASY,
                "1. Cook pasta\n2. Render guanciale\n3. Mix eggs\n4. Combine and serve",
                "Traditional Roman pasta", "pasta-carbonara.jpg");
    }

    private Recipe createChickenAlfredoRecipe(UnitMeasure teaspoon, UnitMeasure tablespoon, UnitMeasure cup, UnitMeasure emptyUOM) {
        List<Ingredient> ingredients = List.of(
                new Ingredient(BigDecimal.valueOf(1), emptyUOM, "pound fettuccine"),
                new Ingredient(BigDecimal.valueOf(2), emptyUOM, "chicken breasts"),
                new Ingredient(BigDecimal.valueOf(1), cup, "heavy cream"),
                new Ingredient(BigDecimal.valueOf(0.5), cup, "butter"),
                new Ingredient(BigDecimal.valueOf(1), cup, "Parmesan cheese")
        );
        ingredientRepository.saveAll(ingredients).collectList().block();
        return buildRecipe("Chicken Alfredo", ingredients, 20, 25, 4, Difficulty.MODERATE,
                "1. Cook pasta\n2. Grill chicken\n3. Make hollandaise Base\n4. Combine all",
                "Creamy pasta classic", "chicken-alfredo.jpg");
    }

    private Recipe createBeefTacoRecipe(UnitMeasure teaspoon, UnitMeasure tablespoon, UnitMeasure cup, UnitMeasure emptyUOM) {
        List<Ingredient> ingredients = List.of(
                new Ingredient(BigDecimal.valueOf(1), emptyUOM, "pound ground beef"),
                new Ingredient(BigDecimal.valueOf(8), emptyUOM, "corn tortillas"),
                new Ingredient(BigDecimal.valueOf(1), emptyUOM, "head lettuce"),
                new Ingredient(BigDecimal.valueOf(0.5), cup, "cheddar cheese")
        );
        ingredientRepository.saveAll(ingredients).collectList().block();
        return buildRecipe("Beef Tacos", ingredients, 10, 15, 4, Difficulty.EASY,
                "1. Brown meat\n2. Warm tortillas\n3. Assemble tacos\n4. Add toppings",
                "Simple street tacos", "beef-taco.jpg");
    }

    private Recipe createShrimpScampiRecipe(UnitMeasure teaspoon, UnitMeasure tablespoon, UnitMeasure cup, UnitMeasure emptyUOM) {
        List<Ingredient> ingredients = List.of(
                new Ingredient(BigDecimal.valueOf(1.5), emptyUOM, "pound shrimp"),
                new Ingredient(BigDecimal.valueOf(1), emptyUOM, "pound linguine"),
                new Ingredient(BigDecimal.valueOf(4), tablespoon, "butter"),
                new Ingredient(BigDecimal.valueOf(4), emptyUOM, "garlic cloves"),
                new Ingredient(BigDecimal.valueOf(0.5), cup, "white wine")
        );
        ingredientRepository.saveAll(ingredients).collectList().block();
        return buildRecipe("Shrimp Scampi", ingredients, 15, 15, 4, Difficulty.MODERATE,
                "1. Cook pasta\n2. Sauté garlic\n3. Add shrimp\n4. Deglaze with wine",
                "Classic Italian seafood pasta", "shrimp-scampi.jpg");
    }

    // ==================== Helper Methods ====================
    private Recipe buildRecipe(String title, List<Ingredient> ingredients, int prepTime, int cookTime,
                               int servings, Difficulty difficulty, String directions,
                               String notesText, String imageName) {
        ingredientRepository.saveAll(ingredients).collectList().block();

        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        recipe.setPrepTime(prepTime);
        recipe.setCookTime(cookTime);
        recipe.setServings(servings);
        recipe.setDifficulty(difficulty);
        recipe.setDirections(directions);
        recipe.setNotes(new Notes(notesText));
        loadRecipeImage(recipe, imageName);
        recipe.setCategories(new HashSet<>(List.of(new Category())));
        recipe.setIngredients(new HashSet<>(ingredients));
        return recipe;
    }

    private void loadRecipeImage(Recipe recipe, String imageName) {
        try {
            byte[] imageBytes = DevBootstrap.class.getResourceAsStream("/static/images/" + imageName).readAllBytes();
            String base64String = Base64.getEncoder().encodeToString(imageBytes);
            recipe.setImage("data:image/png;base64," + base64String);
        } catch (Exception e) {
            log.debug("Recipe image not available: {}", imageName);
        }
    }

    void initAccess() {
        // This avoids DBRef resolution issues with reactive MongoDB

        User user1 = new User();
        user1.setUsername("user1@gmail.com");
        //strength 12 and input= 'pass'
        user1.setPassword("$2y$12$bwUGNLEwKKGA/CleGetPIOfJGGZsB9Ymj0KOmJirJ/pn/wdSQVfie");
        user1.setPasswordConfirm("$2y$12$bwUGNLEwKKGA/CleGetPIOfJGGZsB9Ymj0KOmJirJ/pn/wdSQVfie");
        user1.setAccountNonExpired(Boolean.TRUE);
        user1.setAccountNonLocked(Boolean.TRUE);
        user1.setCredentialsNotExpired(Boolean.TRUE);
        user1.setEnabled(Boolean.TRUE);
        user1.setRoleNames(new HashSet<>(Collections.singletonList("ROLE_ADMIN")));

        User user2 = new User();
        user2.setUsername("user2@gmail.com");
        //strength 12 and input= 'pass'
        user2.setPassword("$2y$12$bwUGNLEwKKGA/CleGetPIOfJGGZsB9Ymj0KOmJirJ/pn/wdSQVfie");
        user2.setPasswordConfirm("$2y$12$bwUGNLEwKKGA/CleGetPIOfJGGZsB9Ymj0KOmJirJ/pn/wdSQVfie");
        user2.setAccountNonExpired(Boolean.TRUE);
        user2.setAccountNonLocked(Boolean.TRUE);
        user2.setCredentialsNotExpired(Boolean.TRUE);
        user2.setEnabled(Boolean.TRUE);
        user2.setRoleNames(new HashSet<>(Collections.singletonList("ROLE_USER")));

        User user3 = new User();
        user3.setUsername("user3@gmail.com");
        //strength 12 and input= 'pass'
        user3.setPassword("$2y$12$bwUGNLEwKKGA/CleGetPIOfJGGZsB9Ymj0KOmJirJ/pn/wdSQVfie");
        user3.setPasswordConfirm("$2y$12$bwUGNLEwKKGA/CleGetPIOfJGGZsB9Ymj0KOmJirJ/pn/wdSQVfie");
        user3.setAccountNonExpired(Boolean.TRUE);
        user3.setAccountNonLocked(Boolean.TRUE);
        user3.setCredentialsNotExpired(Boolean.TRUE);
        user3.setEnabled(Boolean.TRUE);
        user3.setRoleNames(new HashSet<>(Collections.singletonList("ROLE_USER")));

        // Save users with role names
        userRepository.saveAll(List.of(user1, user2, user3)).collectList().block();

        // Delete the role collection as it's no longer needed for authentication
        // Roles can now be stored as simple string references in User documents
        roleRepository.deleteAll().block();
    }
}
