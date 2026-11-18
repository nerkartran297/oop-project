package view;

import model.SortStep;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Vẽ mảng dưới dạng các cột, tô màu phần tử đang so sánh / hoán đổi / ghi trong bước hiện tại.
 * Với merge sort còn vẽ thêm hàng "mảng phụ" bên dưới, thẳng cột với đoạn đang trộn.
 */
public class StepPanel extends JPanel {
    private static final Color NORMAL = new Color(0x90A4AE);
    private static final Color IN_RANGE = new Color(0x64B5F6);
    private static final Color COMPARE = new Color(0xFFB74D);
    private static final Color SWAP = new Color(0xE57373);
    private static final Color WRITE = new Color(0x66BB6A);
    private static final Color DONE = new Color(0x66BB6A);
    private static final Color AUX = new Color(0xB0BEC5);

    private SortStep currentStep;

    public StepPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(760, 340));
    }

    public void setStep(SortStep step) {
        currentStep = step;
        repaint();
    }

    /** Hiển thị một mảng chưa chạy thuật toán (chưa tô màu). */
    public void setArray(int[] values) {
        setStep(new SortStep(values, -1, -1, -1, -1, SortStep.START, ""));
    }

    public void clear() {
        currentStep = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (currentStep == null || currentStep.getValues().length == 0) {
            g2.setColor(Color.GRAY);
            g2.setFont(getFont().deriveFont(Font.ITALIC, 16f));
            String msg = "Create an array to begin";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
            g2.dispose();
            return;
        }

        int[] values = currentStep.getValues();
        int[] aux = currentStep.getAuxValues();
        int n = values.length;
        int margin = 30;
        int gap = n > 12 ? 4 : 8;
        int legendBaseline = getHeight() - 12;
        int top = 24;
        int chartBottom = legendBaseline - 30;           // đáy vùng vẽ (trên hàng chú thích)
        int barWidth = Math.max(4, (getWidth() - 2 * margin - (n - 1) * gap) / n);
        int totalWidth = n * barWidth + (n - 1) * gap;
        int left = (getWidth() - totalWidth) / 2;

        int max = 1;
        for (int v : values) {
            max = Math.max(max, v);
        }

        g2.setFont(getFont().deriveFont(Font.BOLD, n > 14 ? 11f : 14f));
        if (aux == null) {
            int bottom = chartBottom - 18;
            drawRow(g2, values, 0, left, barWidth, gap, top, bottom, max, true);
        } else {
            int rowSpace = (chartBottom - top) / 2;
            int mainBottom = top + rowSpace - 22;
            int auxTop = top + rowSpace + 6;
            int auxBottom = chartBottom - 18;
            drawRow(g2, values, 0, left, barWidth, gap, top, mainBottom, max, true);
            drawAuxRow(g2, aux, currentStep.getAuxOffset(), left, barWidth, gap, auxTop, auxBottom, max);
        }
        drawLegend(g2, legendBaseline);
        g2.dispose();
    }

    /** Hàng chính: cột i có chỉ số i. */
    private void drawRow(Graphics2D g2, int[] values, int offset, int left, int barWidth, int gap,
                         int top, int bottom, int max, boolean showIndex) {
        FontMetrics fm = g2.getFontMetrics();
        int maxBar = bottom - top - 16;
        for (int i = 0; i < values.length; i++) {
            int barHeight = Math.max(4, (int) ((double) values[i] / max * maxBar));
            int x = left + (offset + i) * (barWidth + gap);
            int y = bottom - barHeight;
            g2.setColor(mainColor(offset + i));
            g2.fillRoundRect(x, y, barWidth, barHeight, 6, 6);
            g2.setColor(Color.DARK_GRAY);
            String value = String.valueOf(values[i]);
            g2.drawString(value, x + (barWidth - fm.stringWidth(value)) / 2, y - 5);
            if (showIndex) {
                g2.setColor(Color.GRAY);
                String index = String.valueOf(offset + i);
                g2.drawString(index, x + (barWidth - fm.stringWidth(index)) / 2, bottom + 15);
            }
        }
        g2.setColor(new Color(0xCFD8DC));
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(left - 8, bottom, left + values.length * (barWidth + gap) - gap + 8, bottom);
    }

    /** Hàng mảng phụ: chỉ vẽ ở các cột thuộc đoạn đang trộn, kèm nhãn. */
    private void drawAuxRow(Graphics2D g2, int[] aux, int offset, int left, int barWidth, int gap,
                            int top, int bottom, int max) {
        FontMetrics fm = g2.getFontMetrics();
        int maxBar = bottom - top - 16;
        for (int i = 0; i < aux.length; i++) {
            int barHeight = Math.max(4, (int) ((double) aux[i] / max * maxBar));
            int x = left + (offset + i) * (barWidth + gap);
            int y = bottom - barHeight;
            g2.setColor(auxColor(i));
            g2.fillRoundRect(x, y, barWidth, barHeight, 6, 6);
            g2.setColor(Color.DARK_GRAY);
            String value = String.valueOf(aux[i]);
            g2.drawString(value, x + (barWidth - fm.stringWidth(value)) / 2, y - 5);
        }
        g2.setColor(Color.GRAY);
        g2.setFont(getFont().deriveFont(Font.ITALIC, 12f));
        g2.drawString("temporary copy of the range being merged", left, bottom + 15);
        g2.setFont(getFont().deriveFont(Font.BOLD, g2.getFont().getSize2D()));
    }

    private Color mainColor(int i) {
        String action = currentStep.getAction();
        if (SortStep.DONE.equals(action)) {
            return DONE;
        }
        boolean hasAux = currentStep.getAuxValues() != null;
        if (hasAux) {
            // merge sort: chỉ ô đang được ghi mới sáng, so sánh diễn ra ở hàng mảng phụ
            if (SortStep.WRITE.equals(action) && i == currentStep.getFirstIndex()) {
                return WRITE;
            }
        } else if (i == currentStep.getFirstIndex() || i == currentStep.getSecondIndex()) {
            return SortStep.SWAP.equals(action) ? SWAP : COMPARE;
        }
        boolean inRange = currentStep.getRangeLow() >= 0
                && i >= currentStep.getRangeLow() && i <= currentStep.getRangeHigh();
        return inRange ? IN_RANGE : NORMAL;
    }

    private Color auxColor(int i) {
        String action = currentStep.getAction();
        if (SortStep.COMPARE.equals(action)
                && (i == currentStep.getFirstIndex() || i == currentStep.getSecondIndex())) {
            return COMPARE;
        }
        if (SortStep.WRITE.equals(action) && i == currentStep.getSecondIndex()) {
            return WRITE;
        }
        return AUX;
    }

    private void drawLegend(Graphics2D g2, int baseline) {
        Object[][] items = {
                {NORMAL, "Idle"}, {IN_RANGE, "Working range"}, {COMPARE, "Comparing"},
                {SWAP, "Swapped"}, {WRITE, "Written / sorted"}};
        g2.setFont(getFont().deriveFont(Font.PLAIN, 12f));
        FontMetrics fm = g2.getFontMetrics();
        int total = 0;
        for (Object[] item : items) {
            total += 14 + 6 + fm.stringWidth((String) item[1]) + 18;
        }
        int x = Math.max(10, (getWidth() - total) / 2);
        for (Object[] item : items) {
            g2.setColor((Color) item[0]);
            g2.fillRect(x, baseline - 11, 14, 14);
            g2.setColor(Color.DARK_GRAY);
            g2.drawString((String) item[1], x + 20, baseline);
            x += 14 + 6 + fm.stringWidth((String) item[1]) + 18;
        }
    }
}
