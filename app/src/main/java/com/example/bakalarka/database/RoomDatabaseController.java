package com.example.bakalarka.database;

import androidx.annotation.NonNull;

import com.example.bakalarka.activities.MainActivity;
import com.example.bakalarka.data.room.RoomController;
import com.example.bakalarka.data.room.person.Illness;
import com.example.bakalarka.data.room.person.Person;
import com.example.bakalarka.data.risks.Conditions;
import com.example.bakalarka.data.room.Room;
import com.example.bakalarka.data.sensor.GasensSensor;
import com.example.bakalarka.database.conditions.ConditionsDB;
import com.example.bakalarka.database.conditions.ConditionsEntity;
import com.example.bakalarka.database.illness.IllnessDB;
import com.example.bakalarka.database.illness.IllnessEntity;
import com.example.bakalarka.database.person.PersonDB;
import com.example.bakalarka.database.person.PersonEntity;
import com.example.bakalarka.database.room.RoomDB;
import com.example.bakalarka.database.room.RoomEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomDatabaseController {
    // Deleting data in DB
    public void deleteRoomsInDB(){
        for (Room room: RoomController.rooms){
            deleteConditionsInDB(getConditionsEntities(room.getId()));
            deletePersonInDB(room.getId());
            deleteRoomInDB(room.getId());
        }
    }

    public void deleteWholeRoom(int roomId){
        deleteConditionsInDB(getConditionsEntities(roomId));
        deletePersonInDB(roomId);
        deleteRoomInDB(roomId);
    }

    public void deleteRoomInDB(int roomId){
        RoomDB roomDB = RoomDB.getDatabase(MainActivity.context);
        roomDB.roomDao().delete(getRoomEntity(roomId));
    }

    public void deletePersonInDB(int roomId){
        PersonDB personDB = PersonDB.getDatabase(MainActivity.context);
        PersonEntity personEntity = getPersonEntity(roomId);
        deleteIllnessesInDB(getIllnessEntities(personEntity.getId()));
        personDB.personDao().delete(personEntity);
    }

    public void deleteIllnessesInDB(@NonNull List<IllnessEntity> illnessEntities){
        IllnessDB illnessDB = IllnessDB.getDatabase(MainActivity.context);
        for (IllnessEntity illnessEntity: illnessEntities){
            illnessDB.illnessDao().delete(illnessEntity);
        }
    }

    public void deleteConditionsInDB(@NonNull List<ConditionsEntity> conditionsEntities){
        ConditionsDB conditionsDB = ConditionsDB.getDatabase(MainActivity.context);
        for(ConditionsEntity conditionsEntity: conditionsEntities){
            conditionsDB.conditionsDao().delete(conditionsEntity);
        }
    }

    public void updateWholeRoomInDB(@NonNull Room room){
        updateConditionsInDB(room.getId(), room.getConditionsMap());
        updatePersonInDB(room.getId(), room.getPerson());
        updateRoomInDB(room);
    }

    public void updateRoomInDB(@NonNull Room room){
        deleteRoomInDB(room.getId());
        saveRoomInDB(room.getId(), room.getRoomName());
    }

    public void updatePersonInDB(int roomId, @NonNull Person person){
        updateIllnessesInDB(getPersonEntity(roomId).getId(), person.getIllnesses());
        deletePersonInDB(roomId);
        savePersonInDB(roomId, person);
    }

    public void updateIllnessesInDB(int personId, @NonNull List<Illness> illnesses){
        deleteIllnessesInDB(getIllnessEntities(personId));
        saveIllnessesInDB(personId, illnesses);
    }

    public void updateConditionsInDB(int roomId, @NonNull Map<String, Conditions> conditionsMap){
        deleteConditionsInDB(getConditionsEntities(roomId));
        saveConditionsInDB(roomId, conditionsMap);
    }

    public void saveWholeRoomInDB(@NonNull Room room){
        saveRoomInDB(room.getId(), room.getRoomName());
        saveConditionsInDB(room.getId(), room.getConditionsMap());
        savePersonInDB(room.getId(), room.getPerson());
        listRooms();
    }

    public void saveRoomInDB(int roomId, String name){
        RoomDB roomDB = RoomDB.getDatabase(MainActivity.context);
        RoomEntity roomEntity = new RoomEntity(roomId, name);
        roomDB.roomDao().insert(roomEntity);
    }

    public void saveConditionsInDB(int roomId, @NonNull Map<String, Conditions> conditionsMap){
        ConditionsDB conditionsDB = ConditionsDB.getDatabase(MainActivity.context);
        for(String condition: conditionsMap.keySet()){
            ConditionsEntity conditionsEntity = new ConditionsEntity(roomId, condition, conditionsMap.get(condition));
            conditionsDB.conditionsDao().insert(conditionsEntity);
        }
    }

    public void savePersonInDB(int roomId, @NonNull Person person){
        PersonDB personDB = PersonDB.getDatabase(MainActivity.context);
        PersonEntity personEntity = new PersonEntity(roomId, person);
        personDB.personDao().insertPerson(personEntity);
        saveIllnessesInDB(personEntity.getId(), person.getIllnesses());
    }

    public void saveIllnessesInDB(int personId, @NonNull List<Illness> illnessesList){
        for (Illness illness: illnessesList){
            saveIllnessInDB(personId, illness);
        }
    }

    public void saveIllnessInDB(int personId, Illness illness){
        IllnessDB illnessDB = IllnessDB.getDatabase(MainActivity.context);

        IllnessEntity illnessEntity = new IllnessEntity(personId, illness);
        illnessDB.illnessDao().insert(illnessEntity);
    }

    // Getting from DB
    public RoomEntity getRoomEntity(int roomId){
        RoomDB roomDB = RoomDB.getDatabase(MainActivity.context);
        return roomDB.roomDao().findRoomById(roomId);
    }

    public PersonEntity getPersonEntity(int roomId){
        PersonDB personDB = PersonDB.getDatabase(MainActivity.context);
        return personDB.personDao().findPersonByRoomId(roomId);
    }

    @NonNull
    public List<IllnessEntity> getIllnessEntities(int personId){
        IllnessDB illnessDB = IllnessDB.getDatabase(MainActivity.context);
        return illnessDB.illnessDao().findIllnessesByPersonId(personId);
    }

    @NonNull
    public List<ConditionsEntity> getConditionsEntities(int roomId){
        ConditionsDB conditionsDB = ConditionsDB.getDatabase(MainActivity.context);
        return conditionsDB.conditionsDao().findConditionsByRoomId(roomId);
    }

    @NonNull
    public List<RoomEntity> listRooms(){
        RoomDB roomDB = RoomDB.getDatabase(MainActivity.context);
        return roomDB.roomDao().getAll();
    }

    public void listPersons(){
        PersonDB personDB = PersonDB.getDatabase(MainActivity.context);
        for (PersonEntity personEntity: personDB.personDao().getAll()){
            System.out.println(personEntity.toString());
        }
        personDB.personDao().getAll();
    }

    public void listIllnesses(){
        IllnessDB illnessDB = IllnessDB.getDatabase(MainActivity.context);
        for (IllnessEntity illnessEntity: illnessDB.illnessDao().getAll()){
            System.out.println(illnessEntity.toString());
        }
        illnessDB.illnessDao().getAll();
    }

    @NonNull
    public List<Illness> createIllnessesFromDB(@NonNull List<IllnessEntity> illnessEntities){
        List<Illness> illnesses = new ArrayList<>();
        for (IllnessEntity illnessEntity: illnessEntities){
            Illness illness = new Illness(illnessEntity.getName());
            illnesses.add(illness);
        }
        return illnesses;
    }

    @NonNull
    public Map<String, Conditions> createConditionsFromDB(@NonNull List<ConditionsEntity> conditionsEntities){
        Map<String, Conditions> conditionsMap = new HashMap<>();

        for (ConditionsEntity conditionsEntity: conditionsEntities){
            Conditions conditions = new Conditions(
                    conditionsEntity.getMax(),
                    conditionsEntity.getHigh(),
                    conditionsEntity.getIdeal(),
                    conditionsEntity.getLow(),
                    conditionsEntity.getMin());
            conditionsMap.put(conditionsEntity.getCondition(), conditions);
        }
        return conditionsMap;
    }

    @NonNull
    public Person createPersonFromDB(List<Illness> illnesses, @NonNull PersonEntity personEntity){
        return new Person(illnesses, personEntity.getName(), personEntity.getAge());
    }

    @NonNull
    public Room createRoomFromDB(int roomId){
        PersonEntity personEntity = getPersonEntity(roomId);
        List<Illness> illnesses = createIllnessesFromDB(getIllnessEntities(personEntity.getId()));
        Person person = createPersonFromDB(illnesses, personEntity);
        Map<String, Conditions> conditionsMap = createConditionsFromDB(getConditionsEntities(roomId));
        GasensSensor gasensSensor = new GasensSensor(roomId);

        return new Room(gasensSensor.getId(), getRoomEntity(roomId).getName(), person, conditionsMap);
    }

    @NonNull
    public List<Room> getRoomsFromDB(){
        listIllnesses();
        List<Room> rooms = new ArrayList<>();
        for (RoomEntity roomEntity: listRooms()){
            Room room = createRoomFromDB(roomEntity.getId());
            rooms.add(room);
        }
        return rooms;
    }
}
