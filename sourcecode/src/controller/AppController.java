package controller;

import model.ArrayWrapper;
import model.MergeSort;
import model.SelectionSort;
import model.ShellSort;
import model.SortAlgorithm;
import view.DemoView;
import view.HelpView;
import view.MainMenuView;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

/**
 * Điều phối: chuyển màn hình, tạo thuật toán được chọn, kiểm tra mảng nhập vào và điều khiển chạy từng bước.
 * Chỉ làm việc với kiểu SortAlgorithm nên thêm thuật toán mới không cần sửa lớp này ngoài selectAlgorithm().
 */
public class AppController {
    public static final String SELECTION = "SELECTION";
    public static final String MERGE = "MERGE";
    public static final String SHELL = "SHELL";
    public static final int MAX_SIZE = 20;
    public static final int MAX_VALUE = 99;

    private static final String MENU = "menu";
    private static final String HELP = "help";
    private static final String DEMO = "demo";

    private final JFrame frame = new JFrame("Sorting Algorithm Demonstration");
    private final CardLayout cards = new CardLayout();
    private final MainMenuView menuView;
    private final HelpView helpView;
    private final DemoView demoView;
    private final Random random = new Random();
    private final Timer autoTimer;

    private SortAlgorithm algorithm;
    private ArrayWrapper currentArray;
    private int currentStepIndex;
    private boolean running;

    public AppController() {
        menuView = new MainMenuView(this);
        helpView = new HelpView(this);
        demoView = new DemoView(this);
        autoTimer = new Timer(600, e -> nextStep());
    }

    public void start() {
        frame.setLayout(cards);
        frame.add(menuView, MENU);
        frame.add(helpView, HELP);
        frame.add(demoView, DEMO);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });
        frame.setMinimumSize(new Dimension(820, 640));
        frame.setSize(900, 680);
        frame.setLocationRelativeTo(null);
        cards.show(frame.getContentPane(), MENU);
        frame.setVisible(true);
    }

    // ---- chuyển màn hình ----

    public void selectAlgorithm(String type) {
        switch (type) {
            case SELECTION:
                algorithm = new SelectionSort();
                break;
            case MERGE:
                algorithm = new MergeSort();
                break;
            case SHELL:
                algorithm = new ShellSort();
                break;
            default:
                throw new IllegalArgumentException("Unknown algorithm: " + type);
        }
        currentArray = null;
        running = false;
        stopAuto();
        demoView.reset(algorithm.getName());
        cards.show(frame.getContentPane(), DEMO);
    }

    public void showHelp() {
        cards.show(frame.getContentPane(), HELP);
    }

    public void backToMenu() {
        stopAuto();
        cards.show(frame.getContentPane(), MENU);
    }

    public void quit() {
        int answer = JOptionPane.showConfirmDialog(frame, "Are you sure you want to quit?", "Quit",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (answer == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // ---- tạo mảng ----

    public void createRandomArray(int size) {
        if (size < 1 || size > MAX_SIZE) {
            demoView.showError("Size must be between 1 and " + MAX_SIZE + ".");
            return;
        }
        int[] values = new int[size];
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < size; i++) {
            values[i] = random.nextInt(MAX_VALUE + 1);
            text.append(i == 0 ? "" : ", ").append(values[i]);
        }
        useArray(values);
        demoView.setInputText(text.toString());
    }

    public void createArrayFromInput(String text) {
        String trimmed = text == null ? "" : text.trim();
        if (trimmed.isEmpty()) {
            demoView.showError("Please type some numbers, for example: 5, 2, 9, 1, 7, 3");
            return;
        }
        String[] tokens = trimmed.split("[,\\s]+");
        if (tokens.length > MAX_SIZE) {
            demoView.showError("At most " + MAX_SIZE + " numbers are allowed (you typed " + tokens.length + ").");
            return;
        }
        int[] values = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            try {
                values[i] = Integer.parseInt(tokens[i]);
            } catch (NumberFormatException e) {
                demoView.showError("\"" + tokens[i] + "\" is not an integer.");
                return;
            }
            if (values[i] < 0 || values[i] > MAX_VALUE) {
                demoView.showError("Each number must be between 0 and " + MAX_VALUE + " (found " + values[i] + ").");
                return;
            }
        }
        useArray(values);
    }

    private void useArray(int[] values) {
        stopAuto();
        running = false;
        currentArray = new ArrayWrapper(values);
        demoView.showArray(values);
    }

    // ---- chạy từng bước ----

    public void startSort() {
        if (currentArray == null) {
            demoView.showError("Create an array first (type numbers or press Random).");
            return;
        }
        stopAuto();
        algorithm.setArray(currentArray);
        algorithm.run();
        running = true;
        currentStepIndex = 0;
        showCurrentStep();
    }

    public void nextStep() {
        if (!running) {
            return;
        }
        if (currentStepIndex < algorithm.getStepCount() - 1) {
            currentStepIndex++;
            showCurrentStep();
        }
        if (currentStepIndex >= algorithm.getStepCount() - 1) {
            stopAuto();
        }
    }

    public void previousStep() {
        if (running && currentStepIndex > 0) {
            currentStepIndex--;
            showCurrentStep();
        }
    }

    public void toggleAuto() {
        if (!running) {
            return;
        }
        if (autoTimer.isRunning()) {
            stopAuto();
            return;
        }
        if (currentStepIndex >= algorithm.getStepCount() - 1) {
            currentStepIndex = 0;
            showCurrentStep();
        }
        autoTimer.start();
        demoView.setPlaying(true);
    }

    /** level 1 (chậm) .. 10 (nhanh). */
    public void setSpeed(int level) {
        autoTimer.setDelay(1100 - level * 100);
    }

    private void stopAuto() {
        autoTimer.stop();
        demoView.setPlaying(false);
    }

    private void showCurrentStep() {
        demoView.showStep(algorithm.getStep(currentStepIndex), currentStepIndex, algorithm.getStepCount());
        if (currentStepIndex == algorithm.getStepCount() - 1) {
            demoView.showStatistics(algorithm.getComparisons(), algorithm.getWrites());
        }
    }
}
