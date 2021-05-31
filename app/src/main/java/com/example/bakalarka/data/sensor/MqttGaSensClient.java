 package com.example.bakalarka.data.sensor;

 import android.content.Context;

 import androidx.annotation.NonNull;

 import com.example.bakalarka.data.room.RoomData;

 import org.eclipse.paho.android.service.MqttAndroidClient;
 import org.eclipse.paho.client.mqttv3.IMqttActionListener;
 import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
 import org.eclipse.paho.client.mqttv3.IMqttToken;
 import org.eclipse.paho.client.mqttv3.MqttCallback;
 import org.eclipse.paho.client.mqttv3.MqttClient;
 import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
 import org.eclipse.paho.client.mqttv3.MqttException;
 import org.eclipse.paho.client.mqttv3.MqttMessage;

 import java.util.Arrays;
 import java.util.List;

/*
Trieda na pripojenie ku GaSens zariadeniu pomocou MQTT protokolu

     String clientId     - unikátny string pomocou ktorého sa pripája k serveru
     String url     - cesta k serveru, začína tcp:// a končí portom
     String username     - meno pri pripájaní sa
     String password     - heslo pri pripájaní sa
     List<String> channels    - zoznam tém ku ktorým sa senzor pripája

     MqttConnectOptions connectOptions   - Nepovinné parametre pre pripojenie sa (napr. username, password)
     MqttAndroidClient client    - klient ktorým sa pripáda na server
     Map<String, Float> subscribersData  - data získané zo senzora

     Konštruktor - vyžaduje parametre
        url, context
        url, username, password, context
*/

public class MqttGaSensClient {

    @NonNull
    final String clientId;

    @NonNull
    final MqttConnectOptions connectOptions;
    @NonNull
    final MqttAndroidClient client;

    @NonNull
    final List<String> topics;

    @NonNull
    final RoomData roomData;

    // Konštruktory

    public MqttGaSensClient(@NonNull GasensSensor sensor, Context context) {
        this.clientId = MqttClient.generateClientId();

        this.topics = Arrays.asList(sensor.getTopics());
        System.out.println(topics);

        this.client = new MqttAndroidClient(context, GasensSensor.URL, clientId);
        this.connectOptions = setUpConnectionOptions();

        roomData = new RoomData();
    }

    // Nakonfigurovanie a spustenie komunikácie so serverom
    public void startMqtt() throws MqttException {

        /*
        MqttCallback
         riadi komunikáciu so serverom
         */
        client.setCallback(new MqttCallback() {

            // Po stratení pripojenia
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("connection lost");
            }

            // Po prijatí správy
            //  funkcia odosiela prijaté dáta do funkcie ktorá dáta ukladá
            @Override
            public void messageArrived(String topic, @NonNull MqttMessage message) {
                topic = changeTopic(topic);
                saveMsg(topic, Float.parseFloat(new String(message.getPayload())));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {}
        });

        // connect - pripojenie clienta na server s dodatočnými možnosťami a priradením Callbacku
        client.connect(connectOptions).setActionCallback(new IMqttActionListener() {

            // Po pripojení nastaví odber pre témy subscribes
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                System.out.println("Connection success");
                for (String x: topics){
                    subscribe(x);
                }
            }

            // Po nastaní chyby pri pripájaní
            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                System.out.println("Connection failed: "+exception);
            }
        });
    }

    @NonNull
    private String changeTopic(@NonNull String topic){
        for(String string: GasensSensor.TOPICS){
            if (topic.contains(string)){
                topic = string;
                break;
            }
        }
        return topic;
    }

    // metóda pre odoberanie témy
    // Parametre:
    //  String topic - téma ktorá má byť odoberaná
    private void subscribe(String topic){
        try {
            client.subscribe(topic, 0, null, new IMqttActionListener() {

                // Po úspešnom pripojení
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println(topic + ": subscribe successful");
                }

                // Po neúspešnom pripojení
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.out.println(topic + ": subscribe failed");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // Metóda na ukladanie dát do subscribersData
    // Parametre:
    //  String topic - kľúč pre mapu
    //  float message - hodnota zo servera
    private void saveMsg(@NonNull String topic, float message){
        switch (topic){
            case "temp":
                this.roomData.setTemperature(message);
                break;
            case "hum":
                this.roomData.setHumidity(message);
                break;
            case "pres":
                this.roomData.setPressure(message);
                break;
            case "voc":
                this.roomData.setVoc(message);
                break;
            default:
                System.out.println("Cant save message!");
                break;
        }
    }

    // Metóda pre nastavenie dodatočných nastavení (username a password)
    @NonNull
    private static MqttConnectOptions setUpConnectionOptions() {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setUserName(GasensSensor.USERNAME);
        connOpts.setPassword(GasensSensor.PASSWORD.toCharArray());
        return connOpts;
    }

    @NonNull
    public MqttAndroidClient getClient() {
        return client;
    }

    @NonNull
    public RoomData getRoomData() {
        return roomData;
    }

}
