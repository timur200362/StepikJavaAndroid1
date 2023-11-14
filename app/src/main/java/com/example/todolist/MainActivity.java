package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotes;
    private FloatingActionButton buttonAddNote;
    private NotesAdapter notesAdapter;

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel=new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                Toast.makeText(
                        MainActivity.this,
                        String.valueOf(count),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
        initViews();

        notesAdapter = new NotesAdapter();
        notesAdapter.setOnNoteClickListener(new NotesAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
                viewModel.sumCount();
            }
        });
        recyclerViewNotes.setAdapter(notesAdapter);//применяем адаптер для recyclerView
        //recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));//как будут располагаться элементы(Добавили через xml)

        viewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {//изменения приходят в onChanged
                notesAdapter.setNotes(notes);
            }
        });//подписываемся на изменения в бд

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

                        viewModel.remove(note);//удаляем заметку, обращаясь к БД через viewModel
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
    }

    private void initViews() {
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        buttonAddNote = findViewById(R.id.buttonAddNote);
    }
}