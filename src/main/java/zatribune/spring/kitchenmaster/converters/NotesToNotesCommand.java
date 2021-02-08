package zatribune.spring.kitchenmaster.converters;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import zatribune.spring.kitchenmaster.commands.NotesCommand;
import zatribune.spring.kitchenmaster.data.entities.Notes;

@Component
public class NotesToNotesCommand implements Converter<Notes, NotesCommand> {

    @Synchronized
    @Override
    public @NonNull
    NotesCommand convert(@Nullable Notes source) {
        final NotesCommand notesCommand = new NotesCommand();
        if (source!=null) {
            if (source.getId() != null)//for test testEmptyObject()
                notesCommand.setId(source.getId().toString());
            notesCommand.setDescription(source.getDescription());
        }
        return notesCommand;
    }
}
