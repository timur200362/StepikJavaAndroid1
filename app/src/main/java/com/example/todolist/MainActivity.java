package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotes;
    private FloatingActionButton buttonAddNote;
    private NotesAdapter notesAdapter;

    private NoteDatabase noteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteDatabase=NoteDatabase.getInstance(getApplication());//получаем экземпляр класса NoteDatabase
        initViews();

        notesAdapter = new NotesAdapter();
        notesAdapter.setOnNoteClickListener(new NotesAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
            }
        });
        recyclerViewNotes.setAdapter(notesAdapter);//применяем адаптер для recyclerView
        //recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));//как будут располагаться элементы(Добавили через xml)

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(
                        0,
                        ItemTouchHelper.LEFT
                ) {
                    @Override
//onMove когда хотим передвинуть элемент с одного места, на другое, не пригодиться, поэтому false
                    public boolean onMove(
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {//метод для свайпа
                        int position = viewHolder.getAdapterPosition();//id свайпнутого элемента
                        Note note = notesAdapter.getNotes().get(position);//по id получаем сам объект
                        noteDatabase.notesDao().remove(note.getId());//удаляем полученный объект
                        showNotes();//обновляем список
                    }
                });//для удаления свайпом
        itemTouchHelper.attachToRecyclerView(recyclerViewNotes);

        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddNoteActivity.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showNotes();//показываем обновлённый список после его изменения, т.к вызывается Resume,а не Create
    }

    private void initViews() {
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        buttonAddNote = findViewById(R.id.buttonAddNote);
    }

    private void showNotes() {
        notesAdapter.setNotes(noteDatabase.notesDao().getNotes());//передаем список всех записей
    }
}