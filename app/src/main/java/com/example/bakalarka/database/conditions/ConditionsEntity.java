package com.example.bakalarka.database.conditions;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.bakalarka.data.risks.Conditions;

@Entity
public class ConditionsEntity {
    @PrimaryKey(autoGenerate = true)
    int id;
    final int roomId;

    final String condition;

    final float max;
    final float high;
    final float ideal;
    final float low;
    final float min;

    public ConditionsEntity(int roomId, String condition, float max, float high, float ideal, float low, float min) {
        this.roomId = roomId;
        this.condition = condition;
        this.max = max;
        this.high = high;
        this.ideal = ideal;
        this.low = low;
        this.min = min;
    }

    public ConditionsEntity(int id, String condition, @NonNull Conditions conditions) {
        this.roomId = id;
        this.condition = condition;
        this.max = conditions.getMax();
        this.high = conditions.getHigh();
        this.ideal = conditions.getIdeal();
        this.low = conditions.getLow();
        this.min = conditions.getMin();
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

    public String getCondition() {
        return condition;
    }

    public float getMax() {
        return max;
    }

    public float getHigh() {
        return high;
    }

    public float getIdeal() {
        return ideal;
    }

    public float getLow() {
        return low;
    }

    public float getMin() {
        return min;
    }

}
