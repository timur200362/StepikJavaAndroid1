package com.example.todolist;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {

    private NoteDatabase noteDatabase;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainViewModel(@NonNull Application application) {
        super(application);
        noteDatabase = NoteDatabase.getInstance(application);
    }

    public LiveData<List<Note>> getNotes() {
        return noteDatabase.notesDao().getNotes();
    }

//    public void refreshList(){
//        Disposable disposable=getNotesRx()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<Note>>() {//при работе с Single
//                    @Override
//                    public void accept(List<Note> notesFromDb) throws Throwable {
//                        notes.setValue(notesFromDb);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Throwable {
//                        Log.d("MainViewModel", "Error refreshList");
//                    }//1ый accept-когда все ок, 2ой accept-когда 1ый падает
//                });
//        compositeDisposable.add(disposable);
//    }

    public void remove(Note note) {
        Disposable disposable = noteDatabase.notesDao().remove(note.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        Log.d("MainViewModel", "Removed:" + note.getId());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d("MainViewModel", "Error remove");
                    }
                });
        compositeDisposable.add(disposable);
    }
//    private Completable removeRx(Note note){//реализация Completable
//        return Completable.fromAction(new Action() {
//            @Override
//            public void run() throws Throwable {
//                noteDatabase.notesDao().remove(note.getId());
//            }
//        });
//    }
//
//    private Single<List<Note>> getNotesRx(){//реализация Single, если что-то не поддерживает RxJava
//        return Single.fromCallable(new Callable<List<Note>>() {
//            @Override
//            public List<Note> call() throws Exception {
//                return noteDatabase.notesDao().getNotes();
//            }
//        });
//    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
