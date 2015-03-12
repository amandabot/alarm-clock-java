package alarmclock;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            AlarmClock alarmClock = new AlarmClock();
            AlarmClockGUI userInterface = new AlarmClockGUI(alarmClock);
            alarmClock.addObserver(userInterface);
        });
    }
}
