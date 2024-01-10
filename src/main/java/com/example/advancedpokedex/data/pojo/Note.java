package com.example.advancedpokedex.data.pojo;

import com.example.advancedpokedex.data.User;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents a note associated with a Pokemon.
 */
public class Note implements Serializable {
    private String content;
    private User author;
    private Date timestamp;
    private boolean isPublic;

    /**
     * Constructs a new Note object.
     *
     * @param content   The content of the note.
     * @param author    The User who authored the note.
     * @param isPublic  A boolean indicating whether the note is public or private.
     */
    public Note(String content, User author, boolean isPublic) {
        this.content = content;
        this.author = author;
        this.timestamp = new Date();
        this.isPublic = isPublic;
    }

    /**
     * Retrieves the content of the note.
     *
     * @return The content of the note.
     */
    public String getContent() {
        return content;
    }

    /**
     * Retrieves the author of the note.
     *
     * @return The User who authored the note.
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Retrieves the timestamp when the note was created.
     *
     * @return The timestamp of note creation.
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Checks if the note is public.
     *
     * @return true if the note is public, false if it is private.
     */
    public boolean isPublic() {
        return isPublic;
    }

    @Override
    public String toString() {
        return "Note{" +
                "content='" + content + '\'' +
                ", author=" + author +
                ", timestamp=" + timestamp +
                ", isPublic=" + isPublic +
                '}';
    }
}

