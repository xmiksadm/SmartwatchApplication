package com.example.bakalarka.database.conditions;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ConditionsEntity.class}, version = 1, exportSchema = false)
public abstract class ConditionsDB extends RoomDatabase {
    public abstract ConditionsDao conditionsDao();

    private static volatile ConditionsDB INSTANCE;

    public static ConditionsDB getDatabase(@NonNull final Context context) {
        if (INSTANCE == null) {
            //context.getApplicationContext().deleteDatabase("condition_database");
            synchronized (ConditionsDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ConditionsDB.class, "condition_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
