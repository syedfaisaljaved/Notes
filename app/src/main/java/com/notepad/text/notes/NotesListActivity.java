package com.notepad.text.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.notepad.text.notes.Models.Notes;
import com.notepad.text.notes.adapters.NotesRecyclerAdapter;
import com.notepad.text.notes.persistance.NoteRepository;
import com.notepad.text.notes.utils.VerticalSpacingItemDecorator;

import java.util.ArrayList;
import java.util.List;

public class NotesListActivity extends AppCompatActivity implements NotesRecyclerAdapter.OnNoteListener, View.OnClickListener {

    private static final String TAG = "NotesListActivity";

    //ui components
    private RecyclerView mRecyclerview;

    //vars
    private ArrayList<Notes> mNotes = new ArrayList<>();
    private NotesRecyclerAdapter mRecyclerAdapter;
    private NoteRepository noteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerview = findViewById(R.id.recyclerview);
        findViewById(R.id.fab).setOnClickListener(this);

        noteRepository = new NoteRepository(this);
        retrieveNotes();
        initRecyclerView();
//        insertFakeNotes();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Notes");

    }

    private void retrieveNotes(){
        noteRepository.retrieveNoteTask().observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> notes) {
                if (notes.size() > 0){
                    mNotes.clear();
                }
                mNotes.addAll(notes);
                mRecyclerAdapter.notifyDataSetChanged();
             }
        });
    }

    private void initRecyclerView(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerview.addItemDecoration(itemDecorator);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(mRecyclerview);
        mRecyclerAdapter = new NotesRecyclerAdapter(mNotes,this);
        mRecyclerview.setAdapter(mRecyclerAdapter);

    }

    @Override
    public void onNoteClick(int position) {

        Intent intent = new Intent(this,NoteActivity.class);
        intent.putExtra("selected_note", mNotes.get(position));
        startActivity(intent);
        Toast.makeText(this,"clicked "+position,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);
    }

    private ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            deleteNote(mNotes.get(viewHolder.getAdapterPosition()));
        }
    };

    private void deleteNote(Notes note){
        mNotes.remove(note);
        mRecyclerAdapter.notifyDataSetChanged();

        noteRepository.deleteNote(note);
    }
}
