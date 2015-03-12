package alarmclock;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AlarmClockTest {

    public AlarmClockTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAsAlarm() {
        System.out.println("asAlarm");

        String maxTimeString = "23:59:59";
        LocalTime maxTimeAsAlarm = AlarmClock.asAlarm(maxTimeString);
        LocalTime maxTime = LocalTime.MAX.truncatedTo(ChronoUnit.SECONDS);

        assertEquals(maxTimeAsAlarm, maxTime);

        String minTimeString = "00:00:00";
        LocalTime minTimeAsAlarm = AlarmClock.asAlarm(minTimeString);
        LocalTime minTime = LocalTime.MIN.truncatedTo(ChronoUnit.SECONDS);

        assertEquals(minTimeAsAlarm, minTime);

        String randomTimeString = "03:04:10";
        LocalTime randomTime = LocalTime.of(3, 4, 10);
        LocalTime randomTimeAsAlarm = AlarmClock.asAlarm(randomTimeString);

        assertEquals(randomTime, randomTimeAsAlarm);
    }

    @Test
    public void testAsString() {
        System.out.println("asString");

        String maxTimeString = "23:59:59";
        LocalTime maxTime = LocalTime.MAX.truncatedTo(ChronoUnit.SECONDS);
        String maxTimeAsString = AlarmClock.asString(maxTime);

        Assert.assertTrue(maxTimeString.equalsIgnoreCase(maxTimeAsString));

        String minTimeString = "00:00:00";
        LocalTime minTime = LocalTime.MIN.truncatedTo(ChronoUnit.SECONDS);
        String minTimeAsString = AlarmClock.asString(minTime);

        Assert.assertTrue(minTimeString.equalsIgnoreCase(minTimeAsString));

        String randomTimeString = "03:04:10";
        LocalTime randomTime = LocalTime.of(3, 4, 10);
        String randomTimeAsString = AlarmClock.asString(randomTime);

        Assert.assertTrue(randomTimeString.equalsIgnoreCase(randomTimeAsString));
    }

    @Test
    public void testChangeAlarmState() {
        System.out.println("changeAlarmState");

        LocalTime testAlarm = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        AlarmClock alarmClock = new AlarmClock();

        alarmClock.addAlarm(testAlarm);

        boolean isActive = alarmClock.isActiveAlarm(testAlarm);
        Assert.assertTrue(isActive);

        alarmClock.changeAlarmState(testAlarm);

        boolean isInActiveAlarm = alarmClock.isInactiveAlarm(testAlarm);
        Assert.assertTrue(isInActiveAlarm);

        alarmClock.changeAlarmState(testAlarm);

        isActive = alarmClock.isActiveAlarm(testAlarm);
        Assert.assertTrue(isActive);
    }

    @Test
    public void testAddAlarm() {
        System.out.println("createNewAlarm");

        AlarmClock alarmClock = new AlarmClock();
        List<LocalTime> allAlarms = alarmClock.getAllAlarms();

        Assert.assertTrue(allAlarms.isEmpty());

        LocalTime newAlarm = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        alarmClock.addAlarm(newAlarm);

        allAlarms = alarmClock.getAllAlarms();
        Assert.assertTrue(allAlarms.contains(newAlarm));
    }

    @Test
    public void testRemoveAlarm() {
        System.out.println("deleteAlarm");

        AlarmClock alarmClock = new AlarmClock();
        List<LocalTime> allAlarms = alarmClock.getAllAlarms();

        Assert.assertTrue(allAlarms.isEmpty());

        LocalTime newAlarm = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        alarmClock.addAlarm(newAlarm);

        allAlarms = alarmClock.getAllAlarms();
        Assert.assertTrue(allAlarms.contains(newAlarm));

        alarmClock.removeAlarm(newAlarm);
        allAlarms = alarmClock.getAllAlarms();
        Assert.assertFalse(allAlarms.contains(newAlarm));
    }

    /**
     * Test of getAllAlarms method, of class AlarmClock.
     */
    @Test
    public void testGetAllAlarms() {
        System.out.println("getAllAlarms");

        AlarmClock alarmClock = new AlarmClock();
        List<LocalTime> allAlarms = alarmClock.getAllAlarms();

        Assert.assertTrue(allAlarms.isEmpty());

        LocalTime newAlarm = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        alarmClock.addAlarm(newAlarm);
        allAlarms = alarmClock.getAllAlarms();

        Assert.assertTrue(allAlarms.size() == 1);

        newAlarm = LocalTime.now().plusSeconds(1).truncatedTo(ChronoUnit.SECONDS);
        alarmClock.addAlarm(newAlarm);
        allAlarms = alarmClock.getAllAlarms();

        Assert.assertTrue(allAlarms.size() == 2);

        alarmClock.removeAlarm(newAlarm);
        allAlarms = alarmClock.getAllAlarms();

        Assert.assertTrue(allAlarms.size() == 1);
    }

    @Test
    public void testGetCurrentTime() {
        System.out.println("getCurrentTime");
        AlarmClock alarmClock = new AlarmClock();
        LocalTime currentTime;

        currentTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalTime currentTimeFromAlarmClock = alarmClock.getCurrentTime();

        Assert.assertTrue(currentTime.equals(currentTimeFromAlarmClock)
                || currentTime.plusSeconds(1).equals(currentTimeFromAlarmClock));
    }

    @Test
    public void testIsActiveAlarm() {
        System.out.println("isActiveAlarm");
        LocalTime testAlarm = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        AlarmClock alarmClock = new AlarmClock();

        alarmClock.addAlarm(testAlarm);

        boolean isActive = alarmClock.isActiveAlarm(testAlarm);
        Assert.assertTrue(isActive);

        alarmClock.changeAlarmState(testAlarm);

        isActive = alarmClock.isActiveAlarm(testAlarm);
        Assert.assertFalse(isActive);

        alarmClock.changeAlarmState(testAlarm);

        isActive = alarmClock.isActiveAlarm(testAlarm);
        Assert.assertTrue(isActive);

        testAlarm = LocalTime.now().plusSeconds(1).truncatedTo(ChronoUnit.SECONDS);
        isActive = alarmClock.isActiveAlarm(testAlarm);
        Assert.assertFalse(isActive);

        testAlarm = null;
        isActive = alarmClock.isActiveAlarm(testAlarm);
        Assert.assertFalse(isActive);
    }

    @Test
    public void testIsInactiveAlarm() {
        System.out.println("isInactiveAlarm");
        LocalTime testAlarm = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        AlarmClock alarmClock = new AlarmClock();

        alarmClock.addAlarm(testAlarm);

        boolean isInactive = alarmClock.isInactiveAlarm(testAlarm);
        Assert.assertFalse(isInactive);

        alarmClock.changeAlarmState(testAlarm);

        isInactive = alarmClock.isInactiveAlarm(testAlarm);
        Assert.assertTrue(isInactive);

        alarmClock.changeAlarmState(testAlarm);
        isInactive = alarmClock.isInactiveAlarm(testAlarm);
        Assert.assertFalse(isInactive);

        testAlarm = LocalTime.now().plusSeconds(1).truncatedTo(ChronoUnit.SECONDS);
        isInactive = alarmClock.isInactiveAlarm(testAlarm);
        Assert.assertFalse(isInactive);

        testAlarm = null;
        isInactive = alarmClock.isInactiveAlarm(testAlarm);
        Assert.assertFalse(isInactive);
    }
}
