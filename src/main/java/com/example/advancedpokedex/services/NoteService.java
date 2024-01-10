package com.example.advancedpokedex.services;

import com.example.advancedpokedex.data.User;
import com.example.advancedpokedex.data.pojo.Note;
import com.example.advancedpokedex.exceptions.InternalProcessException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A service class for managing and storing notes related to Pokemon.
 */
public class NoteService {
    private String notesFilePath;

    /**
     * Constructs a new NoteService with the specified file path for storing notes.
     *
     * @param notesFilePath The file path to store and load notes from.
     */
    public NoteService(String notesFilePath) {
        this.notesFilePath = notesFilePath;
    }

    /**
     * Retrieves all notes associated with a specific Pokemon.
     *
     * @param pokemonName The name of the Pokemon for which to retrieve notes.
     * @return A List of Note objects containing all notes for the specified Pokemon.
     */
    public List<Note> readAllNotesForPokemon(String pokemonName) throws InternalProcessException {
        List<Note> pokemonNotes = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(notesFilePath))) {
            Map<String, List<Note>> allNotes = (Map<String, List<Note>>) ois.readObject();
            if (allNotes.containsKey(pokemonName)) {
                pokemonNotes.addAll(allNotes.get(pokemonName));
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new InternalProcessException(e.getMessage());
        }
        return pokemonNotes;
    }

    /**
     * Writes a new note for a specific Pokemon.
     *
     * @param pokemonName The name of the Pokemon for which to write the note.
     * @param note        The content of the note.
     * @param currentUser The User who is writing the note.
     * @param isPublic    A boolean indicating whether the note should be public or private.
     */
    public void writeNoteForPokemon(String pokemonName, String note, User currentUser, boolean isPublic)
    throws InternalProcessException{
        Note newNote = new Note(note, currentUser, isPublic);

        Map<String, List<Note>> allNotes = new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(notesFilePath))) {
            allNotes = (Map<String, List<Note>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new InternalProcessException(e.getMessage());
        }

        List<Note> notes = allNotes.computeIfAbsent(pokemonName, k -> new ArrayList<>());
        notes.add(newNote);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(notesFilePath))) {
            oos.writeObject(allNotes);
        } catch (IOException e) {
            throw new InternalProcessException(e.getMessage());
        }
    }
}

