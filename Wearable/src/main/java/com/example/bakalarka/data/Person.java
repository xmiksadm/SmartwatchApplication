package com.example.bakalarka.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Person {

    public static List<Person> personList;

    private int id;
    private String name;
    private int age;
    private String roomName;

    public Person(int id, String name, int age, String roomName) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.roomName = roomName;
        //this.watch = watch;
    }

    public Person(Map<Integer, Person> person) {
        personList = new ArrayList<>(person.values());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public static List<Person> getPersonList() {
        return personList;
    }

    public static void setPersonList(List<Person> personList) {
        Person.personList = personList;
    }
}
