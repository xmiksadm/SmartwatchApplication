package com.example.bakalarka.database.illness;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.bakalarka.data.room.person.Illness;

@Entity
public class IllnessEntity {
    @PrimaryKey(autoGenerate = true)
    int id;
    final int personId;
    final String name;

    public IllnessEntity(int personId, String name) {
        this.personId = personId;
        this.name = name;
    }

    public IllnessEntity(int personId, @NonNull Illness illness) {
        this.personId = personId;
        this.name = illness.getName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonId() {
        return personId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "IllnessEntity{" +
                "id=" + id +
                ", personId=" + personId +
                ", name='" + name + '\'' +
                '}';
    }
}
