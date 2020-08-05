package com.notepad.text.notes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.notepad.text.notes.Models.Notes;
import com.notepad.text.notes.R;

import java.util.ArrayList;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> {

    private ArrayList<Notes> mNotes = new ArrayList<>();
    private OnNoteListener mOnNoteListener;

    public NotesRecyclerAdapter(ArrayList<Notes> notes, OnNoteListener onNoteListener) {
        this.mNotes = notes;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_note_list_item, parent,false);
        return new ViewHolder(view,mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.timestamp.setText(mNotes.get(position).getTimestamp());
        holder.title.setText(mNotes.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, timestamp;
        OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            title = itemView.findViewById(R.id.note_title);
            timestamp = itemView.findViewById(R.id.note_timestamp);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            onNoteListener.onNoteClick(getAdapterPosition());

        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
