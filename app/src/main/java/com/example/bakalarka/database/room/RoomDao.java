package com.example.bakalarka.database.room;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface RoomDao {
    @NonNull
    @Query("SELECT * FROM RoomEntity")
    List<RoomEntity> getAll();

    @Query("DELETE FROM RoomEntity")
    void deleteAll();

    @Query("SELECT * FROM RoomEntity WHERE id=:roomId")
    RoomEntity findRoomById(int roomId);

    @Insert
    void insertAll(RoomEntity... roomEntities);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(RoomEntity roomEntity);

    @Delete
    void delete(RoomEntity roomEntity);

    @NonNull
    @Transaction
    @Query("SELECT * FROM RoomEntity")
    List<RoomEntity> getRooms();
}