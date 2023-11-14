package com.example.todolist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private NoteDatabase noteDatabase;
    private int count=0;
    private MutableLiveData<Integer> countLD=new MutableLiveData<>();//можем устанавливать значения

    public MainViewModel(@NonNull Application application){
        super(application);
        noteDatabase=NoteDatabase.getInstance(application);
    }

    public LiveData<List<Note>> getNotes(){
        return noteDatabase.notesDao().getNotes();
    }
    public void sumCount(){
        count++;
        countLD.setValue(count);
    }
    public LiveData<Integer> getCount(){
        return countLD;//возвращаем объект типа LiveData, чтобы извне нельзя было устанавливать свои значения
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
