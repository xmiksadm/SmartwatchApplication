package com.example.bakalarka.data.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.bakalarka.activities.MainActivity;
import com.example.bakalarka.R;
import com.example.bakalarka.activities.overview.OverviewAllRoomsActivity;
import com.example.bakalarka.data.risks.ConditionsController;

public class NotificationController {

    // Zobrazenie upozornenia
    public void showNotification(@NonNull Notification notification, int risk){
        if (risk == ConditionsController.HIGH){
            notification.setPlaySound(true);
            createBuilder(notification, NotificationCompat.PRIORITY_HIGH);
        }else if (risk == ConditionsController.LOW){
            notification.setPlaySound(false);
            createBuilder(notification, NotificationCompat.PRIORITY_DEFAULT);
        }

        // Nastavenie času ktorý sa zobrazuje pri upozorneni na aktuálny čas
        notification.getBuilder().setWhen(System.currentTimeMillis());

        // Vytvorenie kanálu pre upozornenia (iba pri verzií Android 8 a novšie)
        createChannel(notification);

        // spustenie upozornenia
        notification.getNotificationManager().notify(notification.getRoomId(), notification.getBuilder().build());

        if (notification.isPlaySound()){
            playNotificationSound();
        }
    }

    public void createChannel(@NonNull Notification notification){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel"+notification.getRoomId();
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel title",
                    NotificationManager.IMPORTANCE_HIGH);
            notification.getNotificationManager().createNotificationChannel(channel);
            notification.getBuilder().setChannelId(channelId);

            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        }
    }

    public void playNotificationSound(){
        try {
            Uri sound = RingtoneManager.getActualDefaultRingtoneUri(MainActivity.context.getApplicationContext(), RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(MainActivity.context.getApplicationContext(), sound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Vytvorenie upozornenia a nastavenie parametrov
    private void createBuilder(@NonNull Notification notification, int priority){
        Intent intent = new Intent(MainActivity.context, OverviewAllRoomsActivity.class);
        intent.putExtra("page", notification.getRoomId());
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.context, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.context, "channel"+notification.getRoomId())
                .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                .setContentTitle(notification.getNotificationTitle())
                .setContentText(notification.getNotificationMessage())
                .setPriority(priority)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notification.setBuilder(builder);

        if (priority == NotificationCompat.PRIORITY_HIGH){
            notification.getBuilder().setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        }
    }
}
