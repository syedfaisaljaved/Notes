package com.notepad.text.notes.persistance;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.notepad.text.notes.Models.Notes;
import com.notepad.text.notes.async.DeleteAsyncTask;
import com.notepad.text.notes.async.InsertAsyncTask;
import com.notepad.text.notes.async.UpdateAsyncTask;

import java.util.List;

public class NoteRepository {

    private NoteDatabase noteDatabase;

    public NoteRepository(Context context) {
        noteDatabase = NoteDatabase.getInstance(context);
    }

    public void insertNoteTask(Notes notes){
        new InsertAsyncTask(noteDatabase.getNoteDao()).execute(notes);
    }

    public void updateNote(Notes notes){
         new UpdateAsyncTask(noteDatabase.getNoteDao()).execute(notes);
    }

    public LiveData<List<Notes>> retrieveNoteTask(){
        return noteDatabase.getNoteDao().getNotes();
    }

    public void deleteNote(Notes notes){
        new DeleteAsyncTask(noteDatabase.getNoteDao()).execute(notes);
    }
}
