package com.example.bakalarka.database.person;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PersonEntity.class}, version = 1, exportSchema = false)
public abstract class PersonDB extends RoomDatabase {
    public abstract PersonDao personDao();

    private static volatile PersonDB INSTANCE;

    public static PersonDB getDatabase(@NonNull final Context context) {
        if (INSTANCE == null) {
            //context.getApplicationContext().deleteDatabase("person_database");
            synchronized (PersonDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PersonDB.class, "person_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
