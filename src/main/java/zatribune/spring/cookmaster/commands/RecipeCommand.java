package zatribune.spring.cookmaster.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import zatribune.spring.cookmaster.data.entities.Difficulty;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class RecipeCommand {
    private String id;
    @NotBlank
    @Size(min = 3,max = 255)// by default hibernate on the database creates this column with a length of 255
    private String title;
    @Min(1)
    @Max(999)
    @NotNull
    private Integer prepTime;
    @Min(1)
    @Max(999)
    @NotNull
    private Integer cookTime;
    @Min(1)
    @Max(100)
    private Integer servings;
    private String source;
    @URL
    private String url;
    @NotBlank
    private String directions;
    private Difficulty difficulty;
    private String image;
    private NotesCommand notes;
    private List<IngredientCommand> ingredients=new ArrayList<>();
    // because sets can't be indexed-->problems in http requests & parameters
    private List<CategoryCommand> categories=new ArrayList<>();
}
