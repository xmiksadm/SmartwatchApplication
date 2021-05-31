package com.example.bakalarka.data.room.person;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private final List<Illness> illnesses;
    private String name;
    private int age;

    public Person() {
        this.name = "";
        this.age = 0;
        this.illnesses = new ArrayList<>();
    }

    public Person(List<Illness> illnesses, String name, int age) {
        this.illnesses = illnesses;
        this.name = name;
        this.age = age;
    }

    public List<Illness> getIllnesses() {
        return illnesses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
