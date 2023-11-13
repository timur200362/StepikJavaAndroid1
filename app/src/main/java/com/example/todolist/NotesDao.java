package com.example.todolist;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM notes")//получить записи
    LiveData<List<Note>> getNotes();

    @Insert
    void add(Note note);

    @Query("DELETE FROM notes WHERE id=:id")//:id-параметр метода remove
    void remove(int id);
}
