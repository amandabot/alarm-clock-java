package alarmclock;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class AlarmDialog extends JDialog {

    private static final String ALARM_STRING_FORMAT = "%02d:%02d:%02d";
    private static final String CANCEL_LABEL = "Cancel";
    private static final String CURRENT_ALARM_LABEL = "Current Alarm: %s";
    private static final String DEFAULT_ALARM_LABEL = "Current Alarm: 00:00:00";
    private static final String HOUR_LABEL = "Hour:";
    private static final String MINUTES_LABEL = "Minutes:";
    private static final String SAVE_LABEL = "Save";
    private static final String SECONDS_LABEL = "Seconds:";

    private final JComboBox<Integer> alarmHour = new JComboBox<>();
    private final JComboBox<Integer> alarmMinute = new JComboBox<>();
    private final JComboBox<Integer> alarmSeconds = new JComboBox<>();
    private boolean alarmUpdated = false;
    private final JLabel currentAlarmLabel = new JLabel(DEFAULT_ALARM_LABEL);

    public AlarmDialog(JFrame window, String title) {
        super(window, title, true);
        createLayout();
        setCurrentAlarm(LocalTime.now());
        updateAlarmDisplay();
    }

    public final LocalTime getCurrentAlarm() {
        return AlarmClock.asAlarm(String.format(
                ALARM_STRING_FORMAT,
                alarmHour.getSelectedItem(),
                alarmMinute.getSelectedItem(),
                alarmSeconds.getSelectedItem()));
    }

    public final void setCurrentAlarm(LocalTime alarm) {
        alarmHour.setSelectedItem(alarm.getHour());
        alarmMinute.setSelectedItem(alarm.getMinute());
        alarmSeconds.setSelectedItem(alarm.getSecond());
    }

    public final boolean isAlarmUpdated() {
        return alarmUpdated;
    }

    private JPanel createAlarmSelectors() {
        for (int x = 0; x < 24; x++) {
            alarmHour.addItem(x);
        }

        for (int x = 0; x < 60; x++) {
            alarmMinute.addItem(x);
            alarmSeconds.addItem(x);
        }

        setAlarmActionListeners(new JComboBox[]{alarmHour, alarmMinute, alarmSeconds});
        JPanel panel = new JPanel();
        panel.add(new JLabel(HOUR_LABEL));
        panel.add(alarmHour);
        panel.add(new JLabel(MINUTES_LABEL));
        panel.add(alarmMinute);
        panel.add(new JLabel(SECONDS_LABEL));
        panel.add(alarmSeconds);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.add(currentAlarmLabel);
        panel.add(getNewButton(SAVE_LABEL));
        panel.add(getNewButton(CANCEL_LABEL));
        return panel;
    }

    private void createLayout() {
        setLayout(new GridLayout(2, 1));
        add(createAlarmSelectors());
        add(createButtonPanel());
    }

    private void setAlarmActionListeners(JComboBox[] boxes) {
        Arrays.stream(boxes).forEach(box -> {
            box.addActionListener(event -> {
                updateAlarmDisplay();
            });
        });
    }

    private ActionListener getButtonListener() {
        return event -> {
            alarmUpdated = SAVE_LABEL.equals(event.getActionCommand());
            setVisible(false);
        };
    }

    private JButton getNewButton(String buttonName) {
        JButton button = new JButton(buttonName);
        button.addActionListener(getButtonListener());
        button.setActionCommand(buttonName);
        return button;
    }

    private void updateAlarmDisplay() {
        currentAlarmLabel.setText(String.format(CURRENT_ALARM_LABEL, AlarmClock.asString(getCurrentAlarm())));
    }
}
