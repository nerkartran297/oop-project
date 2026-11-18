package view;

import controller.AppController;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Font;

/** Màn hình Help: mục đích và cách dùng chương trình. */
public class HelpView extends JPanel {

    private static final String TEXT =
            "PURPOSE\n"
                    + "This program explains three sorting algorithms on a plain int array: "
                    + "Selection Sort, Merge Sort and Shell Sort. Every step of the algorithm is shown on screen.\n\n"
                    + "HOW TO USE\n"
                    + "1. On the main menu choose a sorting algorithm.\n"
                    + "2. Create an array: type numbers separated by commas or spaces "
                    + "(1 to 20 integers, each from 0 to 99) and press \"Use input\", "
                    + "or choose a size and press \"Random\".\n"
                    + "3. Press \"Start\" to run the algorithm with that array.\n"
                    + "4. Press \"Next\" / \"Previous\" to move one step at a time, "
                    + "or \"Play\" to watch the steps automatically (the slider changes the speed).\n"
                    + "5. \"Back\" returns to the main menu at any time. \"Quit\" asks for confirmation.\n\n"
                    + "COLORS\n"
                    + "Orange: two elements being compared.   Red: elements just swapped.\n"
                    + "Green: element written (merge sort) or the sorted array at the end.\n"
                    + "Light blue: the range the algorithm is currently working on.";

    public HelpView(AppController controller) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Help");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        add(title, BorderLayout.NORTH);

        JTextArea area = new JTextArea(TEXT);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(area.getFont().deriveFont(15f));
        area.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(new JScrollPane(area), BorderLayout.CENTER);

        JButton back = new JButton("Back");
        back.addActionListener(e -> controller.backToMenu());
        JPanel south = new JPanel();
        south.add(back);
        add(south, BorderLayout.SOUTH);
    }
}
