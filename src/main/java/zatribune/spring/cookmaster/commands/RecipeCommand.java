package zatribune.spring.cookmaster.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zatribune.spring.cookmaster.data.entities.Difficulty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class RecipeCommand {
    private Long id;
    private String title;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    private String directions;
    private Difficulty difficulty;
    private String image;
    private NotesCommand notes;
    private List<IngredientCommand> ingredients=new ArrayList<>();
    // because sets can't be indexed-->problems in http requests & parameters
    private List<CategoryCommand> categories=new ArrayList<>();
}
