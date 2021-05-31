package com.example.bakalarka.database.person;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.bakalarka.data.room.person.Person;

@Entity
public class PersonEntity {
    @PrimaryKey(autoGenerate = true)
    int id;
    final int roomId;

    final String name;
    final int age;

    public PersonEntity(int roomId, String name, int age) {
        this.roomId = roomId;
        this.name = name;
        this.age = age;
    }

    public PersonEntity(int roomId, @NonNull Person person) {
        this.roomId = roomId;
        this.name = person.getName();
        this.age = person.getAge();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @NonNull
    @Override
    public String toString() {
        return "PersonEntity{" +
                "id=" + id +
                ", roomId=" + roomId +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
