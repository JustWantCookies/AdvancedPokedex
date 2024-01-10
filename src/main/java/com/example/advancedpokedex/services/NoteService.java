package com.example.advancedpokedex.services;

import com.example.advancedpokedex.data.User;
import com.example.advancedpokedex.data.pojo.Note;

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
    public List<Note> readAllNotesForPokemon(String pokemonName) {
        List<Note> pokemonNotes = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(notesFilePath))) {
            Map<String, List<Note>> allNotes = (Map<String, List<Note>>) ois.readObject();
            if (allNotes.containsKey(pokemonName)) {
                pokemonNotes.addAll(allNotes.get(pokemonName));
            }
        } catch (IOException | ClassNotFoundException e) {
            // Handle exceptions (e.g., file not found or deserialization error)
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
    public void writeNoteForPokemon(String pokemonName, String note, User currentUser, boolean isPublic) {
        Note newNote = new Note(note, currentUser, isPublic);

        Map<String, List<Note>> allNotes = new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(notesFilePath))) {
            allNotes = (Map<String, List<Note>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // Handle exceptions (e.g., file not found or deserialization error)
        }

        List<Note> notes = allNotes.computeIfAbsent(pokemonName, k -> new ArrayList<>());
        notes.add(newNote);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(notesFilePath))) {
            oos.writeObject(allNotes);
        } catch (IOException e) {
            // Handle exceptions (e.g., file write error)
        }
    }
}

