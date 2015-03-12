package alarmclock;

import java.awt.event.ActionEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import javax.swing.Timer;

public class AlarmClock extends Observable {

    public final static LocalTime asAlarm(String text) {
        return LocalTime.parse(text, DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public final static String asString(LocalTime alarm) {
        return alarm.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    private final List<LocalTime> activeAlarms = new ArrayList<>();
    private final List<LocalTime> allAlarms = new ArrayList<>();
    private final Timer clock;
    private final List<LocalTime> inactiveAlarms = new ArrayList<>();

    public AlarmClock() {
        clock = new Timer(1000, (ActionEvent e) -> {
            updateClock();
        });
        adjustTimerToSystemClock(clock);
        clock.start();
    }

    public final boolean addAlarm(LocalTime alarm) {
        boolean wasAdded = false;
        LocalTime time = removeSubSecondPrecision(alarm);

        if (!allAlarms.contains(time)) {
            activeAlarms.add(time);
            allAlarms.add(time);
            allAlarms.sort((LocalTime time1, LocalTime time2) -> time1.compareTo(time2));
            wasAdded = true;
        }
        return wasAdded;
    }

    public final void changeAlarmState(LocalTime alarm) {
        if (activeAlarms.contains(alarm)) {
            activeAlarms.remove(alarm);
            inactiveAlarms.add(alarm);
        } else if (inactiveAlarms.contains(alarm)) {
            inactiveAlarms.remove(alarm);
            activeAlarms.add(alarm);
        }
    }

    public final void removeAlarm(LocalTime alarm) {
        activeAlarms.remove(alarm);
        inactiveAlarms.remove(alarm);
        allAlarms.remove(alarm);
    }

    public final List<LocalTime> getAllAlarms() {
        return new ArrayList<>(allAlarms);
    }

    public final LocalTime getCurrentTime() {
        return removeSubSecondPrecision(LocalTime.now());
    }

    public final boolean isActiveAlarm(LocalTime alarm) {
        return activeAlarms.contains(alarm);
    }

    public final boolean isInactiveAlarm(LocalTime alarm) {
        return inactiveAlarms.contains(alarm);
    }

    private void activateAlarm(LocalTime alarm) {
        setChanged();
        notifyObservers(alarm);
    }

    private void adjustTimerToSystemClock(Timer clock) {
        clock.setInitialDelay(1000 - LocalTime.now().getNano() / 1000000);
    }

    private LocalTime removeSubSecondPrecision(LocalTime time) {
        return time.truncatedTo(ChronoUnit.SECONDS);
    }

    private void updateClock() {
        LocalTime currentTime = getCurrentTime();
        if (isActiveAlarm(currentTime)) {
            activateAlarm(currentTime);
        }
    }
}
