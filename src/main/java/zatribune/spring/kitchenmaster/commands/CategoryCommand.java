package zatribune.spring.kitchenmaster.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
