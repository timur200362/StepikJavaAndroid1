package com.example.todolist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
        notesDao.add(note)
                .subscribeOn(Schedulers.io())//переводим(верхний код) добавление в фоновый поток. io-работа с данными
                .observeOn(AndroidSchedulers.mainThread())//переводим(нижний код) в главный поток.
                .subscribe(new Action() {
            @Override
            public void run() throws Throwable {
                shouldCloseScreen.setValue(true);//setValue-только главный поток
            }
        });
    }
}
