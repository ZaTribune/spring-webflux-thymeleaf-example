package com.tribune.demo.km.converter;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.tribune.demo.km.command.NotesCommand;
import com.tribune.demo.km.data.entity.Notes;

@Component
public record NotesToNotesCommand() implements Converter<Notes, NotesCommand> {

    @Override
    public @NonNull NotesCommand convert(@Nullable Notes source) {
        final NotesCommand notesCommand = new NotesCommand();
        if (source != null) {
            if (source.getId() != null)//for test testEmptyObject()
                notesCommand.setId(source.getId().toString());
            notesCommand.setDescription(source.getDescription());
        }
        return notesCommand;
    }
}
