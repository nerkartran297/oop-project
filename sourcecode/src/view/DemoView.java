package view;

import controller.AppController;
import model.SortStep;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

/** Màn hình demo: tạo mảng, Start, duyệt từng bước, Back. Mọi thao tác được chuyển cho AppController. */
public class DemoView extends JPanel {
    private final AppController controller;
    private final StepPanel stepPanel = new StepPanel();

    private final JLabel title = new JLabel();
    private final JTextField inputField = new JTextField(22);
    private final JSpinner sizeSpinner = new JSpinner(new SpinnerNumberModel(8, 1, AppController.MAX_SIZE, 1));
    private final JButton startButton = new JButton("Start");
    private final JButton previousButton = new JButton("Previous");
    private final JButton nextButton = new JButton("Next");
    private final JButton playButton = new JButton("Play");
    private final JSlider speedSlider = new JSlider(1, 10, 5);
    private final JLabel descriptionLabel = new JLabel(" ");
    private final JLabel statusLabel = new JLabel(" ");

    public DemoView(AppController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        add(buildTop(), BorderLayout.NORTH);
        add(stepPanel, BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);
        setStarted(false);
    }

    private JPanel buildTop() {
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton back = new JButton("Back");
        back.addActionListener(e -> controller.backToMenu());
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        row1.add(back);
        row1.add(title);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton useInput = new JButton("Use input");
        useInput.addActionListener(e -> controller.createArrayFromInput(inputField.getText()));
        inputField.addActionListener(e -> controller.createArrayFromInput(inputField.getText()));
        JButton random = new JButton("Random");
        random.addActionListener(e -> controller.createRandomArray((Integer) sizeSpinner.getValue()));
        row2.add(new JLabel("Array:"));
        row2.add(inputField);
        row2.add(useInput);
        row2.add(Box.createHorizontalStrut(16));
        row2.add(new JLabel("Size:"));
        row2.add(sizeSpinner);
        row2.add(random);

        top.add(row1);
        top.add(row2);
        return top;
    }

    private JPanel buildBottom() {
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));

        descriptionLabel.setFont(descriptionLabel.getFont().deriveFont(Font.BOLD, 15f));
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.PLAIN, 13f));
        JPanel text = new JPanel(new FlowLayout(FlowLayout.LEFT));
        text.add(descriptionLabel);
        JPanel status = new JPanel(new FlowLayout(FlowLayout.LEFT));
        status.add(statusLabel);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER));
        startButton.addActionListener(e -> controller.startSort());
        previousButton.addActionListener(e -> controller.previousStep());
        nextButton.addActionListener(e -> controller.nextStep());
        playButton.addActionListener(e -> controller.toggleAuto());
        speedSlider.addChangeListener(e -> controller.setSpeed(speedSlider.getValue()));
        controls.add(startButton);
        controls.add(previousButton);
        controls.add(nextButton);
        controls.add(playButton);
        controls.add(new JLabel("Speed:"));
        controls.add(speedSlider);

        bottom.add(text);
        bottom.add(status);
        bottom.add(controls);
        return bottom;
    }

    // ---- các hàm AppController gọi để cập nhật giao diện ----

    /** Đưa màn hình về trạng thái ban đầu cho thuật toán mới được chọn. */
    public void reset(String algorithmName) {
        title.setText(algorithmName);
        inputField.setText("");
        stepPanel.clear();
        descriptionLabel.setText("Create an array, then press Start");
        statusLabel.setText(" ");
        setPlaying(false);
        setStarted(false);
    }

    public void showArray(int[] values) {
        stepPanel.setArray(values);
        descriptionLabel.setText("Array created with " + values.length + " elements. Press Start");
        statusLabel.setText(" ");
        setPlaying(false);
        setStarted(false);
    }

    public void showStep(SortStep step, int index, int total) {
        stepPanel.setStep(step);
        descriptionLabel.setText(step.getDescription());
        statusLabel.setText("Step " + (index + 1) + " / " + total);
        setStarted(true);
        previousButton.setEnabled(index > 0);
        nextButton.setEnabled(index < total - 1);
    }

    public void showStatistics(int comparisons, int writes) {
        statusLabel.setText(statusLabel.getText() + "   |   Total comparisons: " + comparisons
                + "   Total writes: " + writes);
    }

    public void setInputText(String text) {
        inputField.setText(text);
    }

    public void setPlaying(boolean playing) {
        playButton.setText(playing ? "Pause" : "Play");
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Invalid input", JOptionPane.WARNING_MESSAGE);
    }

    private void setStarted(boolean started) {
        previousButton.setEnabled(started);
        nextButton.setEnabled(started);
        playButton.setEnabled(started);
    }
}
