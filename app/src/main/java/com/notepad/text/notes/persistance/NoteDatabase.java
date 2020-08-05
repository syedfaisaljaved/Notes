package com.notepad.text.notes.persistance;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.notepad.text.notes.Models.Notes;

@Database(entities = {Notes.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "notes_db";
    private static NoteDatabase instance;

    public static NoteDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    NoteDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public abstract NoteDao getNoteDao();
}
