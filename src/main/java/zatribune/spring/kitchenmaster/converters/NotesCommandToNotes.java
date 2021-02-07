package zatribune.spring.kitchenmaster.converters;

import lombok.Synchronized;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import zatribune.spring.kitchenmaster.commands.NotesCommand;
import zatribune.spring.kitchenmaster.data.entities.Notes;

@Component
public class NotesCommandToNotes implements Converter<NotesCommand, Notes> {

    @Synchronized
    @Override
    public @NonNull
    Notes convert(NotesCommand source) {
        final Notes notes = new Notes();
        if (source.getId() != null&&!source.getId().isEmpty())
            notes.setId(new ObjectId(source.getId()));
        notes.setDescription(source.getDescription());
        return notes;
    }
}
