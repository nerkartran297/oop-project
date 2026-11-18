package model;

/**
 * Lớp trừu tượng chung của các thuật toán sắp xếp (Template Method).
 * Phần chung: giữ mảng, ghi lại từng bước, reset, chạy trọn thuật toán bằng run().
 * Phần riêng của mỗi thuật toán: sort(). Controller chỉ làm việc với kiểu SortAlgorithm (đa hình).
 */
public abstract class SortAlgorithm {
    private static final int INITIAL_CAPACITY = 64;

    protected final String name;
    protected ArrayWrapper array;          // mảng đang được sắp xếp (bản làm việc)
    private ArrayWrapper original;         // mảng đầu vào, giữ nguyên để chạy lại
    private SortStep[] steps = new SortStep[INITIAL_CAPACITY];
    private int stepCount;

    protected SortAlgorithm(String name) {
        this.name = name;
    }

    /** Đặt mảng đầu vào. Thuật toán làm việc trên bản sao nên mảng gốc không bị thay đổi. */
    public void setArray(ArrayWrapper input) {
        original = input.copy();
        reset();
    }

    /** Đưa mảng làm việc về trạng thái ban đầu và xóa các bước đã ghi. */
    public void reset() {
        array = original == null ? null : original.copy();
        steps = new SortStep[INITIAL_CAPACITY];
        stepCount = 0;
    }

    /** Chạy toàn bộ thuật toán và ghi lại mọi bước (bước đầu là START, bước cuối là DONE). */
    public final void run() {
        if (original == null) {
            throw new IllegalStateException("Array has not been set");
        }
        reset();
        recordStep(-1, -1, SortStep.START, "Initial array");
        sort();
        recordStep(-1, -1, SortStep.DONE, "The array is sorted");
    }

    /** Cài đặt thuật toán trên this.array; gọi recordStep() sau mỗi thao tác đáng chú ý. */
    public abstract void sort();

    protected void recordStep(int first, int second, String action, String description) {
        recordStep(first, second, -1, -1, action, description);
    }

    protected void recordStep(int first, int second, int low, int high, String action, String description) {
        recordStep(null, 0, first, second, low, high, action, description);
    }

    /** Ghi một bước có kèm mảng phụ (ví dụ bản sao tạm trong merge sort). */
    protected void recordStep(int[] aux, int auxOffset, int first, int second, int low, int high,
                              String action, String description) {
        if (stepCount == steps.length) {
            SortStep[] bigger = new SortStep[steps.length * 2];
            for (int i = 0; i < steps.length; i++) {
                bigger[i] = steps[i];
            }
            steps = bigger;
        }
        steps[stepCount++] = new SortStep(array.toArray(), aux, auxOffset, first, second, low, high, action, description);
    }

    public String getName() {
        return name;
    }

    public int getStepCount() {
        return stepCount;
    }

    public SortStep getStep(int index) {
        if (index < 0 || index >= stepCount) {
            throw new IndexOutOfBoundsException("Step " + index + " out of range 0.." + (stepCount - 1));
        }
        return steps[index];
    }

    public int getComparisons() {
        return array == null ? 0 : array.getComparisons();
    }

    public int getWrites() {
        return array == null ? 0 : array.getWrites();
    }
}
