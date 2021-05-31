package com.example.bakalarka;

import com.example.bakalarka.data.Conditions;
import com.example.bakalarka.service.Adapter;
import com.example.bakalarka.service.SensorBackgroundService;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//@RunWith(AndroidJUnit4.class)
public class UnitTest {

    private Adapter adapter;
    private SensorBackgroundService sensorBackgroundService;

    @Before
    public void setUp() {

        //service.Adapter for testing room data
        adapter = new Adapter();
        Conditions conditions = new Conditions(1, 16, 20, 25, 28,
                20,30,45,60,
                960,980,1040,1060,
                200,250,400,500);

        Map<Integer, Conditions> mapConditions = new HashMap<>();
        mapConditions.put(1, conditions);
        new Conditions(mapConditions);

        //service.SensorBackgroundService for testing fall detection
        sensorBackgroundService = new SensorBackgroundService();
        double[] lastRecords = new double[30];
        lastRecords[0] = 9;
        lastRecords[1] = 35;
        lastRecords[2] = 0.9;

        sensorBackgroundService.setLastRecords(lastRecords);
        //SensorEvent sensorEvent = new SensorEvent(1);
        //sensorBackgroundService.onSensorChanged(sensorEvent);

    }

    @Test
    public void testVocVeryLow() {
        int result = adapter.checkVocColor(1f, 0);
        //assertThat(result, is(2));
        assertEquals("VOC risk should be very low!", 2, result);
    }

    @Test
    public void testVocVeryHigh() {
        int result = adapter.checkVocColor(600f, 0);
        assertEquals("VOC risk should be very high!", 2, result);
    }

    @Test
    public void testVocNormal() {
        int result = adapter.checkVocColor(300f, 0);
        assertEquals("VOC risk should be normal!", 0, result);
    }

    @Test
    public void testRoomTemperatureVeryLow() {
        int result = adapter.checkTemperatureColor(10f, 0);
        assertEquals("Room temperature risk should be very low!", 2, result);
    }

    @Test
    public void testRoomTemperatureVeryHigh() {
        int result = adapter.checkTemperatureColor(30f, 0);
        assertEquals("Room temperature risk should be very high!", 2, result);
    }

    @Test
    public void testRoomTemperatureNormal() {
        int result = adapter.checkTemperatureColor(24f, 0);
        assertEquals("Room temperature risk should be normal!", 0, result);
    }

    @Test
    public void testHumidityVeryLow() {
        int result = adapter.checkVocColor(10f, 0);
        assertEquals("VOC risk should be very low!", 2, result);
    }

    @Test
    public void testHumidityVeryHigh() {
        int result = adapter.checkVocColor(50f, 0);
        assertEquals("VOC risk should be very high!", 2, result);
    }

    @Test
    public void testHumidityNormal() {
        int result = adapter.checkHumidityColor(35f, 0);
        assertEquals("VOC risk should be normal!", 0, result);
    }

    @Test
    public void testPressureVeryLow() {
        int result = adapter.checkVocColor(950f, 0);
        assertEquals("VOC risk should be very low!", 2, result);
    }

    @Test
    public void testPressureVeryHigh() {
        int result = adapter.checkVocColor(1050f, 0);
        assertEquals("VOC risk should be very high!", 2, result);
    }

    @Test
    public void testPressureNormal() {
        int result = adapter.checkPressureColor(1000f, 0);
        assertEquals("VOC risk should be normal!", 0, result);
    }

    @Test(expected = NullPointerException.class)
    public void testFallDetected() {
        boolean fall = sensorBackgroundService.fallDetected();
        assertTrue("Fall should be detected", fall);
    }
}