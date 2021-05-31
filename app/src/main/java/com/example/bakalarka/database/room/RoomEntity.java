package com.example.bakalarka.database.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RoomEntity {
    @PrimaryKey
    final
    int id;

    final String name;

    public RoomEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /*
    @Relation(
            parentColumn = "id",
            entityColumn = "roomId"
    )
    PersonEntity person;

    @Relation(
            parentColumn = "id",
            entityColumn = "roomId"
    )
    ConditionsEntity conditions;*/

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return "RoomEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
