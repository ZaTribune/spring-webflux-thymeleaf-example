package zatribune.spring.cookmaster.converters;

import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import zatribune.spring.cookmaster.commands.NotesCommand;
import zatribune.spring.cookmaster.data.entities.Notes;

@Component
public class NotesToNotesCommand implements Converter<Notes, NotesCommand> {

    @Synchronized
    @Override
    public NotesCommand convert(@Nullable Notes source) {
        if (source == null)
            return null;
        final NotesCommand notesCommand = new NotesCommand();
        if (source.getId() != null)//for test testEmptyObject()
            notesCommand.setId(source.getId().toString());
        notesCommand.setDescription(source.getDescription());
        return notesCommand;
    }
}
