package com.example.bakalarka.database.illness;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IllnessDao {
    @NonNull
    @Query("SELECT * FROM IllnessEntity")
    List<IllnessEntity> getAll();

    @Query("DELETE FROM IllnessEntity")
    void deleteAll();

    @Insert
    void insertAll(IllnessEntity... illnessEntities);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(IllnessEntity illnessEntity);

    @Delete
    void delete(IllnessEntity illnessEntity);

    @NonNull
    @Query("SELECT * FROM IllnessEntity WHERE personId=:personId")
    List<IllnessEntity> findIllnessesByPersonId(int personId);

}