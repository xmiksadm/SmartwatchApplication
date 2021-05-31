package com.example.bakalarka.database.illness;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {IllnessEntity.class}, version = 1, exportSchema = false)
public abstract class IllnessDB extends RoomDatabase {
    public abstract IllnessDao illnessDao();

    private static volatile IllnessDB INSTANCE;

    public static IllnessDB getDatabase(@NonNull final Context context) {
        if (INSTANCE == null) {
            //context.getApplicationContext().deleteDatabase("illness_database");
            synchronized (IllnessDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            IllnessDB.class, "illness_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
