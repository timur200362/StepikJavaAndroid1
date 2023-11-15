package com.example.todolist;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM notes")//получить записи
    List<Note> getNotes();

    @Insert
    void add(Note note);//ничего не возвращает, можно completable(rxJava)

    @Query("DELETE FROM notes WHERE id=:id")//:id-параметр метода remove
    void remove(int id);
}
