package zatribune.spring.cookmaster.data.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"notes","ingredients","categories"})
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    @Lob
    private String directions;
    @Enumerated(value =EnumType.STRING)//overriding the default behaviour for enumerations
    private Difficulty difficulty;
    @Lob
    private String image;
    @OneToOne(cascade = CascadeType.ALL)
    private Notes notes;
    // orphanRemoval is the opposite of cascade as it enables a child entity to be removed
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "recipe",fetch = FetchType.EAGER,orphanRemoval = true)
    private Set<Ingredient> ingredients=new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "recipe_category",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category>categories=new HashSet<>();

    public Recipe(Long recipeId) {
        this.id=recipeId;
    }

    public void setNotes(Notes notes) {
        notes.setRecipe(this);
        this.notes = notes;
    }

    public Recipe addIngredient(Ingredient ingredient){
        ingredient.setRecipe(this);
        this.ingredients.add(ingredient);
        return this;
    }


}
