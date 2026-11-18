package view;

import controller.AppController;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

/** Menu chính: tiêu đề, 3 thuật toán, Help, Quit. */
public class MainMenuView extends JPanel {

    public MainMenuView(AppController controller) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("Sorting Algorithm Demonstration");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 28f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subtitle = new JLabel("Choose an algorithm to start");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalGlue());
        add(title);
        add(Box.createRigidArea(new Dimension(0, 8)));
        add(subtitle);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(button("Selection Sort", e -> controller.selectAlgorithm(AppController.SELECTION)));
        add(button("Merge Sort", e -> controller.selectAlgorithm(AppController.MERGE)));
        add(button("Shell Sort", e -> controller.selectAlgorithm(AppController.SHELL)));
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(button("Help", e -> controller.showHelp()));
        add(button("Quit", e -> controller.quit()));
        add(Box.createVerticalGlue());
    }

    private JButton button(String text, java.awt.event.ActionListener listener) {
        JButton b = new JButton(text);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(260, 40));
        b.setPreferredSize(new Dimension(260, 40));
        b.addActionListener(listener);
        add(Box.createRigidArea(new Dimension(0, 8)));
        return b;
    }
}
