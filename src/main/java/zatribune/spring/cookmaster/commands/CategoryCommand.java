package zatribune.spring.cookmaster.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zatribune.spring.cookmaster.data.entities.Recipe;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class CategoryCommand {
    String id;
    String description;
    String info;
    Byte[] image;
    Set<RecipeCommand>recipes;
}
