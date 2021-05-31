package com.example.bakalarka.database.room;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {RoomEntity.class}, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    public abstract RoomDao roomDao();

    private static volatile RoomDB INSTANCE;

    public static RoomDB getDatabase(@NonNull final Context context) {
        if (INSTANCE == null) {
            //context.getApplicationContext().deleteDatabase("your_database");
            synchronized (RoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDB.class, "room_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
