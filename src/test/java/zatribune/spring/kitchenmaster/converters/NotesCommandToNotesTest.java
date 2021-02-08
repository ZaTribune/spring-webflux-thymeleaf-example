package zatribune.spring.kitchenmaster.converters;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zatribune.spring.kitchenmaster.commands.NotesCommand;
import zatribune.spring.kitchenmaster.data.entities.Notes;

import static org.junit.jupiter.api.Assertions.*;

class NotesCommandToNotesTest {

    NotesCommandToNotes converter;
    String description="a dummy notes description";

    @BeforeEach
    void setUp() {
        converter=new NotesCommandToNotes();
    }

    @Test
    void testEmptyObject(){
        assertNotNull(converter.convert(new NotesCommand()));
    }

    @Test
    void convert() {
        NotesCommand input=new NotesCommand();
        input.setId(new ObjectId().toString());
        input.setDescription(description);

        Notes output=converter.convert(input);

        assertNotNull(output);
        assertEquals(input.getId(),output.getId().toString());
        assertEquals(description,output.getDescription());
    }
}