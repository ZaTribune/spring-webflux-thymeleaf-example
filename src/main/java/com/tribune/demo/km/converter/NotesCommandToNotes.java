package com.tribune.demo.km.converter;

import org.bson.types.ObjectId;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;

import org.springframework.stereotype.Component;
import com.tribune.demo.km.command.NotesCommand;
import com.tribune.demo.km.data.entity.Notes;

@Component
public record NotesCommandToNotes() implements Converter<NotesCommand, Notes> {

    @Override
    public @NonNull Notes convert(NotesCommand source) {
        final Notes notes = new Notes();
        if (source.getId() != null && !source.getId().isEmpty()) notes.setId(new ObjectId(source.getId()));
        notes.setDescription(source.getDescription());
        return notes;
    }
}
