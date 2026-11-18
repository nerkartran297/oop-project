package model;

/**
 * Ảnh chụp bất biến của một bước sắp xếp: nội dung mảng, hai chỉ số đang được xử lý,
 * khoảng đang làm việc (nếu có), loại hành động và mô tả.
 */
public class SortStep {
    public static final String START = "START";
    public static final String COMPARE = "COMPARE";
    public static final String SWAP = "SWAP";
    public static final String WRITE = "WRITE";
    public static final String DONE = "DONE";

    private final int[] values;
    private final int firstIndex;
    private final int secondIndex;
    private final int rangeLow;
    private final int rangeHigh;
    private final String action;
    private final String description;
    private final int[] auxValues;     // mảng phụ (merge sort), null nếu không dùng
    private final int auxOffset;       // phần tử auxValues[0] tương ứng chỉ số nào của mảng chính

    /** Chỉ số hoặc khoảng không dùng thì truyền -1. */
    public SortStep(int[] values, int firstIndex, int secondIndex, int rangeLow, int rangeHigh,
                    String action, String description) {
        this(values, null, 0, firstIndex, secondIndex, rangeLow, rangeHigh, action, description);
    }

    /**
     * Bước có mảng phụ. Với COMPARE, firstIndex và secondIndex là vị trí trong mảng phụ;
     * với WRITE, firstIndex là vị trí được ghi trong mảng chính, secondIndex là vị trí nguồn trong mảng phụ.
     */
    public SortStep(int[] values, int[] auxValues, int auxOffset, int firstIndex, int secondIndex,
                    int rangeLow, int rangeHigh, String action, String description) {
        this.values = copyOf(values);
        this.auxValues = auxValues == null ? null : copyOf(auxValues);
        this.auxOffset = auxOffset;
        this.firstIndex = firstIndex;
        this.secondIndex = secondIndex;
        this.rangeLow = rangeLow;
        this.rangeHigh = rangeHigh;
        this.action = action;
        this.description = description;
    }

    private static int[] copyOf(int[] source) {
        int[] result = new int[source.length];
        for (int i = 0; i < source.length; i++) {
            result[i] = source[i];
        }
        return result;
    }

    public int[] getValues() {
        return copyOf(values);
    }

    /** Mảng phụ hoặc null nếu bước này không dùng. */
    public int[] getAuxValues() {
        return auxValues == null ? null : copyOf(auxValues);
    }

    public int getAuxOffset() {
        return auxOffset;
    }

    public int getFirstIndex() {
        return firstIndex;
    }

    public int getSecondIndex() {
        return secondIndex;
    }

    public int getRangeLow() {
        return rangeLow;
    }

    public int getRangeHigh() {
        return rangeHigh;
    }

    public String getAction() {
        return action;
    }

    public String getDescription() {
        return description;
    }
}
