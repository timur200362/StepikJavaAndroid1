package com.example.todolist;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddNoteViewModel extends AndroidViewModel {
    private NotesDao notesDao;
    private MutableLiveData<Boolean> shouldCloseScreen=new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();//коллекция всех подписок

    public AddNoteViewModel(@NonNull Application application){
        super(application);
        notesDao = NoteDatabase.getInstance(application).notesDao();
    }
    public LiveData<Boolean> getShouldCloseScreen(){
        return shouldCloseScreen;
    }
    public void saveNote(Note note){
        Disposable disposable = notesDao.add(note)
                .subscribeOn(Schedulers.io())//переводим(верхний код) добавление в фоновый поток. io-работа с данными
                .observeOn(AndroidSchedulers.mainThread())//переводим(нижний код) в главный поток.
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        shouldCloseScreen.setValue(true);//setValue-только главный поток
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d("AddNoteViewModel", "Error saveNote");
                    }
                });
        compositeDisposable.add(disposable);//добавляем объект disposable для добавления записи в коллекцию
    }

//    private Completable addRx(Note note){
//        return Completable.fromAction(new Action() {
//            @Override
//            public void run() throws Throwable {
//                notesDao.add(note);
//            }
//        });
//    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();//отменяем подписку, если viewModel уничтожится
    }
}
