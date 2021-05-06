package com.example.room.roomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDuo {

    @Insert
    void saveRecord(User user);

    @Query("select*from User")
    List<User> readRecord();

    @Delete
    void deleteRecord(User user);

    @Update
    void updateRecord(User user);
}
