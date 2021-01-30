package zatribune.spring.cookmaster.services;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import zatribune.spring.cookmaster.commands.IngredientCommand;
import zatribune.spring.cookmaster.data.entities.Ingredient;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceImplTest {

    @Mock
    IngredientService ingredientService;
    ObjectId id;
    String description="dummy description";

    @BeforeEach
    void setUp(){
        id=new ObjectId();
    }

    @Test
    void getIngredientById() {
        IngredientCommand ingredientCommand=new IngredientCommand();
        ingredientCommand.setId(id.toString());
        ingredientCommand.setDescription(description);
        when(ingredientService.getIngredientById(id.toString())).thenReturn(Mono.just(ingredientCommand));
        Mono<IngredientCommand>ingredientMono=ingredientService.getIngredientById(id.toString());
        verify(ingredientService,times(1)).getIngredientById(id.toString());
        assertNotNull(ingredientMono);
        assertEquals(id.toString(), Objects.requireNonNull(ingredientMono.block()).getId());
    }



}