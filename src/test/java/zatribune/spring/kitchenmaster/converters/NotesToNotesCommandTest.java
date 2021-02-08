package zatribune.spring.kitchenmaster.converters;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zatribune.spring.kitchenmaster.commands.NotesCommand;
import zatribune.spring.kitchenmaster.data.entities.Notes;

import static org.junit.jupiter.api.Assertions.*;

class NotesToNotesCommandTest {


    private NotesToNotesCommand converter;
    private final String description="a dummy notes description";

    @BeforeEach
    void setUp() {
        converter=new NotesToNotesCommand();
    }

    @Test
    void testEmptyObject(){
        assertNotNull(converter.convert(new Notes()));
    }

    @Test
    void convert() {
        Notes input=new Notes();
        input.setId(new ObjectId());
        input.setDescription(description);

        NotesCommand output=converter.convert(input);

        assertNotNull(output);
        assertEquals(input.getId().toString(),output.getId());
        assertEquals(description,output.getDescription());
    }

}