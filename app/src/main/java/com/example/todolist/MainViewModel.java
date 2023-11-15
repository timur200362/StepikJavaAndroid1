package com.example.todolist;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {

    private NoteDatabase noteDatabase;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<List<Note>> notes=new MutableLiveData<>();

    public MainViewModel(@NonNull Application application){
        super(application);
        noteDatabase=NoteDatabase.getInstance(application);
    }

    public LiveData<List<Note>> getNotes(){
        return notes;
    }

    public void refreshList(){
        Disposable disposable=getNotesRx()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Note>>() {//при работе с Single
                    @Override
                    public void accept(List<Note> notesFromDb) throws Throwable {
                        notes.setValue(notesFromDb);
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void remove(Note note){
        Disposable disposable=removeRx(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        Log.d("MainViewModel", "Removed:"+note.getId());
                        refreshList();
                    }
                });
        compositeDisposable.add(disposable);
    }
    private Completable removeRx(Note note){//реализация Completable
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Throwable {
                noteDatabase.notesDao().remove(note.getId());
            }
        });
    }

    private Single<List<Note>> getNotesRx(){//реализация Single, если что-то не поддерживает RxJava
        return Single.fromCallable(new Callable<List<Note>>() {
            @Override
            public List<Note> call() throws Exception {
                return noteDatabase.notesDao().getNotes();
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
