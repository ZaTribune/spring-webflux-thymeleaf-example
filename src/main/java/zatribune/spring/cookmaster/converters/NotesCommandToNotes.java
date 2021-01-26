package zatribune.spring.cookmaster.converters;

import lombok.Synchronized;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import zatribune.spring.cookmaster.commands.NotesCommand;
import zatribune.spring.cookmaster.data.entities.Notes;
import zatribune.spring.cookmaster.data.entities.Recipe;

@Component
public class NotesCommandToNotes implements Converter<NotesCommand, Notes> {

    @Synchronized
    @Override
    public Notes convert(@Nullable NotesCommand source) {
        if (source==null)
        return null;
        final Notes notes=new Notes();
        notes.setId(new ObjectId(source.getId()));
        notes.setDescription(source.getDescription());
        return notes;
    }
}
