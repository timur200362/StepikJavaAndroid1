package com.example.todolist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private NoteDatabase noteDatabase;

    public MainViewModel(@NonNull Application application){
        super(application);
        noteDatabase=NoteDatabase.getInstance(application);
    }

    public LiveData<List<Note>> getNotes(){
        return noteDatabase.notesDao().getNotes();
    }


    public void remove(Note note){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                noteDatabase.notesDao().remove(note.getId());
            }
        });
        thread.start();
    }
}
