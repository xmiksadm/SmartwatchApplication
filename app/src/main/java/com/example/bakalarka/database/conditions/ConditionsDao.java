package com.example.bakalarka.database.conditions;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ConditionsDao {
    @NonNull
    @Query("SELECT * FROM ConditionsEntity")
    List<ConditionsEntity> getAll();

    @Query("DELETE FROM ConditionsEntity")
    void deleteAll();

    @Insert
    void insertAll(ConditionsEntity... roomEntities);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ConditionsEntity conditionsEntity);

    @Delete
    void delete(ConditionsEntity conditionsEntity);

    @NonNull
    @Query("SELECT * FROM ConditionsEntity WHERE roomId=:roomId")
    List<ConditionsEntity> findConditionsByRoomId(int roomId);

}