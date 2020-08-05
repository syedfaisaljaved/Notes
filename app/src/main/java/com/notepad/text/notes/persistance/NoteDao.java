package com.notepad.text.notes.persistance;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.notepad.text.notes.Models.Notes;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insertNotes(Notes... notes);

    @Query("SELECT * FROM notes")
    LiveData<List<Notes>> getNotes();

    @Query("SELECT * FROM notes WHERE title LIKE :title")
    List<Notes> getNotesWithCustomQuery(String title);

    @Delete
    int delete(Notes... notes);

    @Update
    int update(Notes... notes);
}
