package com.notepad.text.notes.async;

import android.os.AsyncTask;

import com.notepad.text.notes.Models.Notes;
import com.notepad.text.notes.persistance.NoteDao;

public class DeleteAsyncTask extends AsyncTask<Notes,Void, Void> {

    private NoteDao noteDao;

    public DeleteAsyncTask(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    @Override
    protected Void doInBackground(Notes... notes) {
        noteDao.delete(notes);
        return null;
    }
}
