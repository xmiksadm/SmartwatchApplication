package com.example.bakalarka.data.risks;

import androidx.annotation.NonNull;

import com.example.bakalarka.data.notifications.NotificationController;
import com.example.bakalarka.data.room.Room;
import com.example.bakalarka.data.notifications.Notification;

import java.util.HashMap;
import java.util.Map;

public class ConditionsController {
    public static final int HIGH = 2;
    public static final int LOW = 1;
    public static final int NONE = 0;
    public static final String RISKS_KEY_TMP = "Teplota", RISKS_KEY_HUM = "Vlhkosť", RISKS_KEY_PRES = "Tlak", RISKS_KEY_VOC = "VOC";

    @NonNull
    final NotificationController notificationController;

    public ConditionsController() {
        this.notificationController = new NotificationController();
    }

    @NonNull
    public Map<String, Integer> getRisks(@NonNull Room room) {
        Map<String, Integer> risks = new HashMap<>();
        risks.put(RISKS_KEY_TMP, checkConditions(room.getRoomData().getTemperature(), room.getConditionsMap().get(Conditions.TEMPERATURE_CONDITIONS)));
        risks.put(RISKS_KEY_HUM, checkConditions(room.getRoomData().getHumidity(), room.getConditionsMap().get(Conditions.HUMIDITY_CONDITIONS)));
        risks.put(RISKS_KEY_PRES, checkConditions(room.getRoomData().getPressure(), room.getConditionsMap().get(Conditions.PRESSURE_CONDITIONS)));
        risks.put(RISKS_KEY_VOC, checkConditions(room.getRoomData().getVoc(), room.getConditionsMap().get(Conditions.VOC_CONDITIONS)));
        return risks;
    }

    public int getRiskLevel(@NonNull Room room){
        Map<String, Integer> risks = getRisks(room);
        boolean lowRisk = false;
        for (String key: risks.keySet()){
            if (risks.get(key) == HIGH){
                return HIGH;
            }else if (risks.get(key) == LOW){
                lowRisk = true;
            }
        }

        if (lowRisk){
            return LOW;
        }else{
            return NONE;
        }
    }

    public void createNotification(@NonNull Notification notification, @NonNull Map<String, Integer> risks){
        StringBuilder highRisksMessage = new StringBuilder("Vysoké riziko: ");
        StringBuilder lowRisksMessage = new StringBuilder("Nízke riziko: ");
        boolean highRisk = false, lowRisk = false;
        for (String key: risks.keySet()){
            if (risks.get(key) == HIGH){
                highRisksMessage.append(key).append(", ");
                highRisk = true;
            }else if (risks.get(key) == LOW){
                lowRisksMessage.append(key).append(", ");
                lowRisk = true;
            }
        }

        String message = highRisksMessage + "\n" + lowRisksMessage;
        notification.setNotificationMessage(message);
        if (highRisk){
            notificationController.showNotification(notification, ConditionsController.HIGH);
        }else if (lowRisk){
            notificationController.showNotification(notification, ConditionsController.LOW);
        }
    }

    public int checkConditions(float value, @NonNull Conditions condition){
        if (value > condition.getMax()){
            return HIGH;
        }else if (value > condition.getHigh()){
            return LOW;
        }else if (value > condition.getLow()){
            return NONE;
        }else if (value > condition.getMin()){
            return LOW;
        }else{
            return HIGH;
        }
    }
}
