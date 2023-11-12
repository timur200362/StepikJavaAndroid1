package com.example.todolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private ArrayList<Note> notes=new ArrayList<>();

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();//сообщает адаптеру, что данные изменились и их нужно обновить
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){//создаём View из макета, будет вызван +- 10 раз
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.note_item,//макет
                parent,//контейнер, куда вставляем все элементы
                false
        );
        return new NotesViewHolder(view);//создаем 10 объектов ViewHolder
    }
    @Override
    public void onBindViewHolder(NotesViewHolder viewHolder, int position){//какой установить текст, цвет и т.д. в элементы
        Note note=notes.get(position);//получаем объект note по позиции(id)
        viewHolder.textViewNote.setText(note.getText());//устанавливаем текст

        int colorResId;
        switch (note.getPriority()) {
            case 0:
                colorResId = android.R.color.holo_green_light;
                break;
            case 1:
                colorResId = android.R.color.holo_orange_light;
                break;
            default:
                colorResId = android.R.color.holo_red_light;
        }
        int color = ContextCompat.getColor(viewHolder.itemView.getContext(), colorResId);//получаем цвет по его id
        viewHolder.textViewNote.setBackgroundColor(color);//устанавливаем цвет в зависимости от приоритета
    }
    @Override
    public int getItemCount(){//кол-во элементов списка
        return notes.size();
    }

    class NotesViewHolder extends RecyclerView.ViewHolder{//ViewHolder хранит ссылки на все созданные элементы списка, чтобы их потом пересоздать
        private TextView textViewNote;

        public NotesViewHolder(@NonNull View itemView){
            super(itemView);
            textViewNote=itemView.findViewById(R.id.textViewNote);//вьюшка каждой записи списка
        }
    }
}
