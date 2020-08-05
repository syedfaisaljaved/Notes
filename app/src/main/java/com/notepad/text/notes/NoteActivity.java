package com.notepad.text.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.notepad.text.notes.Models.Notes;
import com.notepad.text.notes.persistance.NoteRepository;
import com.notepad.text.notes.utils.Utility;

public class NoteActivity extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, View.OnClickListener, TextWatcher {

    private static final String TAG = "NoteActivity";
    private static final int EDIT_MODE_ENABLED = 1;
    private static final int EDIT_MODE_DISABLED = 0;

    //UI
    private LinedEditText linedEditText;
    private EditText mEditTitle;
    private TextView mViewTitle;
    private RelativeLayout mCheckContainer, mBackArrowContainer;
    private ImageView mCheck, mBackArrow;

    //VARS
    private boolean mIsNewNote;
    private Notes initialNote;
    private Notes finalNote;
    private GestureDetector gestureDetector;
    private int mMode;
    private NoteRepository noteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        linedEditText = findViewById(R.id.note_text);
        mEditTitle = findViewById(R.id.note_edit_title);
        mViewTitle = findViewById(R.id.note_text_title);
        mCheckContainer = findViewById(R.id.check_container);
        mBackArrowContainer = findViewById(R.id.back_arrow_container);
        mCheck = findViewById(R.id.toolbar_check);
        mBackArrow = findViewById(R.id.toolbar_back_arrow);

        noteRepository = new NoteRepository(this);

        if (getIncomingIntent()){
            //this is a new note (Edit mode)
            setNewNoteProperties();
            enableEditMode();
        }else {
            //this is NOT a new note (View mode)
            setNoteProperties();
            disableContentInteraction();
        }

        setListeners();
    }

    private void hideSoftKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null){
            view = new View(this);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    private void enableEditMode(){
        mBackArrowContainer.setVisibility(View.GONE);
        mCheckContainer.setVisibility(View.VISIBLE);
        mViewTitle.setVisibility(View.GONE);
        mEditTitle.setVisibility(View.VISIBLE);
        mMode = EDIT_MODE_ENABLED;
        enableContentInteraction();
    }

    private void disableEditMode(){
        mBackArrowContainer.setVisibility(View.VISIBLE);
        mCheckContainer.setVisibility(View.GONE);
        mViewTitle.setVisibility(View.VISIBLE);
        mEditTitle.setVisibility(View.GONE);
        mMode = EDIT_MODE_DISABLED;
        disableContentInteraction();

        String temp = linedEditText.getText().toString();
        temp = temp.replace("\n","");
        temp = temp.replace(" ", "");
        if (temp.length()>0){
            finalNote.setTitle(mEditTitle.getText().toString());
            finalNote.setContent(linedEditText.getText().toString());
            String timestamp = Utility.getCurrentTimeStamp();
            finalNote.setTimestamp(timestamp);

            if (!finalNote.getContent().equals(initialNote.getContent()) || !finalNote.getTitle().equals(initialNote.getTitle())){
                saveChanges();
            }
        }
    }

    private void setListeners(){
        linedEditText.setOnTouchListener(this);
        gestureDetector = new GestureDetector(this,this);
        mViewTitle.setOnClickListener(this);
        mCheck.setOnClickListener(this);
        mBackArrow.setOnClickListener(this);
        mEditTitle.addTextChangedListener(this);

    }

    private void disableContentInteraction(){
        linedEditText.setKeyListener(null);
        linedEditText.setFocusable(false);
        linedEditText.setFocusableInTouchMode(false);
        linedEditText.setCursorVisible(false);
        linedEditText.clearFocus();
    }

    private void enableContentInteraction(){
        linedEditText.setKeyListener(new EditText(this).getKeyListener());
        linedEditText.setFocusable(true);
        linedEditText.setFocusableInTouchMode(true);
        linedEditText.setCursorVisible(true);
        linedEditText.requestFocus();
    }

    private boolean getIncomingIntent(){
        if (getIntent().hasExtra("selected_note")){
            initialNote = getIntent().getParcelableExtra("selected_note");
            finalNote = getIntent().getParcelableExtra("selected_note");
//            Log.d(TAG, "getIncomingIntent: "+ note.toString());

            mMode = EDIT_MODE_DISABLED;
            mIsNewNote = false;
            return false;
        }
        mMode = EDIT_MODE_ENABLED;
        mIsNewNote = true;
        return true;
    }

    private void saveChanges(){
        if (mIsNewNote){
            saveNewNote();
        }else {
            updateNote();
        }
    }

    private void saveNewNote(){
        noteRepository.insertNoteTask(finalNote);
    }

    private void updateNote(){
        noteRepository.updateNote(finalNote);
    }

    private void setNoteProperties(){
        mViewTitle.setText(initialNote.getTitle());
        mEditTitle.setText(initialNote.getTitle());
        linedEditText.setText(initialNote.getContent());
    }

    private void setNewNoteProperties(){
        mViewTitle.setText("Note Title");
        mEditTitle.setText("Note Title");

        initialNote = new Notes();
        finalNote = new Notes();
        initialNote.setTitle("Note Title");
        finalNote.setTitle("Note Title");
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        Log.d(TAG, "onDoubleTap: Double Tapped");
        enableEditMode();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.toolbar_check:
                hideSoftKeyboard();
                disableEditMode();
                break;
            case R.id.note_text_title:
                enableEditMode();
                mEditTitle.requestFocus();
                mEditTitle.setSelection(mEditTitle.length());
                break;
            case R.id.toolbar_back_arrow:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mMode == EDIT_MODE_ENABLED){
            onClick(mCheck);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mode",mMode);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMode= savedInstanceState.getInt("mode");
        if (mMode == EDIT_MODE_ENABLED){
            enableEditMode();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mViewTitle.setText(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
