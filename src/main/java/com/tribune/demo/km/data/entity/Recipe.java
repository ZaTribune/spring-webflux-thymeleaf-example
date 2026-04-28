package com.tribune.demo.km.data.entity;

import com.tribune.demo.km.config.ObjectIdHandler;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@Document
public class Recipe {

    @Id
    @JsonSerialize(using = ObjectIdHandler.Serializer.class)
    @JsonDeserialize(using = ObjectIdHandler.Deserializer.class)
    private ObjectId id;
    private String title;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    private String directions;
    private Difficulty difficulty;
    private String image;
    private Notes notes;

    // Store ingredient and category references as objects (not IDs)
    // This way MongoDB stores them naturally
    private Set<Ingredient> ingredients = new HashSet<>();
    private Set<Category> categories = new HashSet<>();

    public Recipe addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        return this;
    }

    public Recipe addCategory(Category category) {
        this.categories.add(category);
        return this;
    }


}
