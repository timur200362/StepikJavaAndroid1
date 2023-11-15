package com.example.todolist;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM notes")//получить записи
    LiveData<List<Note>> getNotes();

    @Insert
    Completable add(Note note);//ничего не возвращает, можно completable(rxJava)

    @Query("DELETE FROM notes WHERE id=:id")//:id-параметр метода remove
    void remove(int id);
}
