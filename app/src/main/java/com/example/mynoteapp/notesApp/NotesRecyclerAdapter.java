package com.example.mynoteapp.notesApp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mynoteapp.R;
import com.example.mynoteapp.database.model.Note;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> {

    List<Note> notes;

    public NotesRecyclerAdapter(List<Note> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Note note = notes.get(position);
        final String oldContent = note.getContent();
        final String newTitle = note.getTitle();
        final String newTime = note.getTime();
        final int newId = note.getId();
        holder.title.setText(newTitle);
        holder.time.setText(newTime);
        if (onItemClickListener!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position,newTitle,newTime,newId,oldContent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (notes == null)
            return 0;
        return notes.size();
    }

    public void changeList(List<Note> notes){
        this.notes = notes;
        notifyDataSetChanged();
    }

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int pos , String title , String time , int id , String content);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
        }
    }
}
