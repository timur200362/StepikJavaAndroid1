package com.example.todolist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class AddNoteViewModel extends AndroidViewModel {
    private NotesDao notesDao;
    private MutableLiveData<Boolean> shouldCloseScreen=new MutableLiveData<>();

    public AddNoteViewModel(@NonNull Application application){
        super(application);
        notesDao=NoteDatabase.getInstance(application).notesDao();
    }
    public LiveData<Boolean> getShouldCloseScreen(){
        return shouldCloseScreen;
    }
    public void saveNote(Note note){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                notesDao.add(note);
                shouldCloseScreen.postValue(true);
            }
        });
        thread.start();
    }
}
