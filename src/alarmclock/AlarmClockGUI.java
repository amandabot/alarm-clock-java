package alarmclock;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class AlarmClockGUI implements Observer {

    private static final String ALARM_ACTIVATED_MESSAGE = "Alarm Activated: %s";
    private static final String ALARM_OFF_LABEL = "Alarm Off";
    private static final String ALARM_ON_LABEL = "Alarm On";
    private static final String ALARM_TIME_LABEL = "Alarm Time";
    private static final String CURRENT_TIME_LABEL = "Current Time";
    private static final String DEFAULT_TIME_LABEL = "00:00:00";
    private static final String DELETE_ALARM_CONFIRMATION = "Confirm deleting alarm: %s";
    private static final String DELETE_LABEL = "Delete";
    private static final String DIALOG_TITLE = "Add/Edit Alarm";
    private static final String EDIT_LABEL = "Edit";
    private static final String EMPTY_STRING = "";
    private static final String EXISTING_ALARM_WARNING = "Alarm already exists.";
    private static final String LIST_LABEL = "List";
    private static final String NEW_ALARM_LABEL = "New Alarm";
    private static final String NO_ALARMS_LABEL = "--:--:--";
    private static final String NO_ALARMS_TO_DELETE_WARNING = "No alarms to delete";
    private static final String REMAINING_TIME_LABEL = "Remaining Time";
    private static final String TIMER_LABEL = "Timer";
    private static final String WINDOW_TITLE = "Alarm Clock";

    private final AlarmDialog addEditAlarmDialog = createDialog();
    private final AlarmClock alarmClock;
    private final JComboBox<LocalTime> alarmSelectionList = new JComboBox<>();
    private JLabel currentAlarm, currentTime;
    private final ActionListener guiEventDelegator = getGuiActionListener();
    private JButton onOffButton;
    private JLabel remainingTime;
    private final JFrame window = new JFrame(WINDOW_TITLE);

    public AlarmClockGUI(AlarmClock alarmClock) {
        this.alarmClock = alarmClock;
        setWindowOptions(window);
        createAndStartTimer();
    }

    @Override
    public void update(Observable o, Object arg) {
        LocalTime alarm = (LocalTime) arg;

        if (alarmClock.isActiveAlarm(alarm)) {
            JOptionPane.showMessageDialog(
                    window,
                    String.format(ALARM_ACTIVATED_MESSAGE, alarm),
                    EMPTY_STRING,
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void addAlarm() {
        addEditAlarmDialog.setCurrentAlarm(LocalTime.now());
        addEditAlarmDialog.setVisible(true);

        LocalTime newAlarm = addEditAlarmDialog.getCurrentAlarm();

        if (addEditAlarmDialog.isAlarmUpdated()) {
            if (alarmClock.addAlarm(newAlarm)) {
                alarmSelectionList.addItem(newAlarm);
                alarmSelectionList.setSelectedItem(newAlarm);
            } else {
                JOptionPane.showMessageDialog(window, EXISTING_ALARM_WARNING);
            }
        }
    }

    private void changeSelectedAlarm() {

        if (0 == alarmSelectionList.getItemCount()) {
            currentAlarm.setText(NO_ALARMS_LABEL);
            return;
        }

        LocalTime selectedAlarm = (LocalTime) alarmSelectionList.getSelectedItem();
        if (null != selectedAlarm) {
            currentAlarm.setText(AlarmClock.asString(selectedAlarm));
            setAlarmStatus(selectedAlarm);
        }
    }

    private JPanel createAlarmList() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        alarmSelectionList.setRenderer(new AlarmClockGUI.AlarmListCellRenderer());
        alarmSelectionList.addActionListener(guiEventDelegator);
        alarmSelectionList.setActionCommand(LIST_LABEL);
        panel.add(alarmSelectionList);
        return panel;
    }

    private void createAndStartTimer() {
        Timer timer = new Timer(1000, guiEventDelegator);
        timer.setActionCommand(TIMER_LABEL);
        timer.setInitialDelay(0);
        timer.start();
    }

    private JButton createButton(String buttonName, ActionListener al) {
        JButton b = new JButton(buttonName);
        b.setActionCommand(buttonName);
        b.addActionListener(al);
        return b;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));

        buttonPanel.add(createButton(NEW_ALARM_LABEL, guiEventDelegator));
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        buttonPanel.add(Box.createHorizontalGlue());

        buttonPanel.add(createButton(EDIT_LABEL, guiEventDelegator));
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        buttonPanel.add(Box.createHorizontalGlue());

        onOffButton = createButton(ALARM_OFF_LABEL, guiEventDelegator);
        onOffButton.setPreferredSize(new Dimension(88, 26)); //Keeps the size constant if the text is changed

        buttonPanel.add(onOffButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 5)));

        buttonPanel.add(createButton(DELETE_LABEL, guiEventDelegator));
        return buttonPanel;
    }

    private AlarmDialog createDialog() {
        AlarmDialog dialog = new AlarmDialog(window, DIALOG_TITLE);
        dialog.pack();
        dialog.setLocationRelativeTo(window);
        return dialog;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraintsBuilder()
                .gridx(0).gridy(0)
                .fill(GridBagConstraints.HORIZONTAL)
                .weightx(1.0).weighty(0.5)
                .insets(new Insets(5, 8, 5, 9))
                .build();
        mainPanel.add(createTimePanel(), c);
        c = new GridBagConstraintsBuilder()
                .gridx(0).gridy(1)
                .fill(GridBagConstraints.HORIZONTAL)
                .weightx(1.0).weighty(0.3)
                .insets(new Insets(0, 10, 0, 11))
                .build();
        mainPanel.add(createAlarmList(), c);
        c = new GridBagConstraintsBuilder()
                .gridx(0).gridy(2)
                .fill(GridBagConstraints.HORIZONTAL)
                .weightx(1.0).weighty(0.3)
                .insets(new Insets(8, 10, 5, 10))
                .build();
        mainPanel.add(createButtonPanel(), c);
        return mainPanel;
    }

    private JPanel createTimePanel() {
        JPanel timePanel = new JPanel(new GridBagLayout());

        remainingTime = new JLabel(DEFAULT_TIME_LABEL, SwingConstants.CENTER);
        remainingTime.setFont(
                remainingTime.getFont().deriveFont(36.0f));
        remainingTime.setOpaque(true);
        remainingTime.setBorder(BorderFactory.createTitledBorder(REMAINING_TIME_LABEL));

        currentTime = new JLabel(
                AlarmClock.asString(LocalTime.now()),
                SwingConstants.CENTER);
        currentTime.setFont(
                currentTime.getFont().deriveFont(16.0f));
        currentTime.setOpaque(false);
        currentTime.setBorder(BorderFactory.createTitledBorder(CURRENT_TIME_LABEL));

        currentAlarm = new JLabel(DEFAULT_TIME_LABEL, SwingConstants.CENTER);
        currentAlarm.setFont(
                currentAlarm.getFont().deriveFont(16.0f));
        currentAlarm.setOpaque(false);
        currentAlarm.setBorder(BorderFactory.createTitledBorder(ALARM_TIME_LABEL));

        GridBagConstraints c = new GridBagConstraintsBuilder()
                .gridx(0).gridy(0)
                .gridwidth(1).gridheight(2)
                .fill(GridBagConstraints.BOTH)
                .weightx(1.0).weighty(1.0)
                .build();
        timePanel.add(remainingTime, c);

        c = new GridBagConstraintsBuilder()
                .gridx(1).gridy(0)
                .gridwidth(1).gridheight(1)
                .fill(GridBagConstraints.BOTH)
                .weightx(1.0).weighty(1.0)
                .build();
        timePanel.add(currentTime, c);

        c = new GridBagConstraintsBuilder()
                .gridx(1).gridy(1)
                .gridwidth(1).gridheight(1)
                .fill(GridBagConstraints.BOTH)
                .weightx(1.0).weighty(1.0)
                .build();
        timePanel.add(currentAlarm, c);
        return timePanel;
    }

    private void removeAlarm() {

        if (0 == alarmSelectionList.getItemCount()) {
            JOptionPane.showMessageDialog(
                    window,
                    NO_ALARMS_TO_DELETE_WARNING,
                    EMPTY_STRING,
                    JOptionPane.PLAIN_MESSAGE);
            return;
        }

        LocalTime selectedAlarm = (LocalTime) alarmSelectionList.getSelectedItem();

        if (null != selectedAlarm) {
            int dialogResponse = JOptionPane.showConfirmDialog(
                    window,
                    String.format(DELETE_ALARM_CONFIRMATION, selectedAlarm),
                    EMPTY_STRING,
                    JOptionPane.YES_NO_OPTION);

            if (JOptionPane.YES_OPTION == dialogResponse) {
                alarmSelectionList.removeItem(selectedAlarm);
                alarmClock.removeAlarm(selectedAlarm);
                changeSelectedAlarm();
            }
        }
    }

    private void editAlarm() {

        if (alarmSelectionList.getItemCount() > 0) {

            LocalTime originalAlarm = (LocalTime) alarmSelectionList.getSelectedItem();

            addEditAlarmDialog.setCurrentAlarm(originalAlarm);
            addEditAlarmDialog.setVisible(true);

            LocalTime editedAlarm = addEditAlarmDialog.getCurrentAlarm();

            if (addEditAlarmDialog.isAlarmUpdated() && !originalAlarm.equals(editedAlarm)) {

                boolean isInactive = alarmClock.isInactiveAlarm(originalAlarm);
                alarmClock.removeAlarm(originalAlarm);
                alarmSelectionList.removeItem(originalAlarm);
                alarmClock.addAlarm(editedAlarm);

                if (isInactive) {
                    alarmClock.changeAlarmState(editedAlarm);
                }
                alarmSelectionList.addItem(editedAlarm);
                alarmSelectionList.setSelectedItem(editedAlarm);
            }
        }
    }

    private void setAlarmStatus(LocalTime alarm) {
        String alarmStatus = alarmClock.isActiveAlarm(alarm) ? ALARM_ON_LABEL : ALARM_OFF_LABEL;
        onOffButton.setText(alarmStatus);
    }

    private ActionListener getGuiActionListener() {
        return event -> {
            switch (event.getActionCommand()) {
                case TIMER_LABEL:
                    updateTimeDisplay();
                    break;
                case EDIT_LABEL:
                    editAlarm();
                    break;
                case ALARM_OFF_LABEL:
                    toggleAlarmState();
                    break;
                case NEW_ALARM_LABEL:
                    addAlarm();
                    break;
                case DELETE_LABEL:
                    removeAlarm();
                    break;
                case LIST_LABEL:
                    changeSelectedAlarm();
                    break;
                default:
                    break;
            }
        };
    }

    private void setWindowOptions(JFrame window) {
        window.add(createMainPanel());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setLocationRelativeTo(null); //Centers frame. Must follow pack()
        window.setResizable(false);
        window.setVisible(true);
    }

    private void toggleAlarmState() {

        if (alarmSelectionList.getItemCount() > 0) {
            LocalTime selectedAlarm = (LocalTime) alarmSelectionList.getSelectedItem();
            alarmClock.changeAlarmState(selectedAlarm);
            setAlarmStatus(selectedAlarm);
        }
    }

    private void updateTimeDisplay() {
        currentTime.setText(AlarmClock.asString(alarmClock.getCurrentTime()));

    }

    static class AlarmListCellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            if (value instanceof LocalTime) {
                value = AlarmClock.asString((LocalTime) value);
            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }
}
