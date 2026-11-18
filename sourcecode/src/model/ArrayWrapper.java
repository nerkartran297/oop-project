package model;

/**
 * Bọc một mảng int[] thuần (không dùng ArrayList hay lớp collection nào của Java).
 * Mọi thuật toán chỉ làm việc với mảng thông qua lớp này, nên có thể đếm số lần so sánh/ghi
 * và kiểm tra chỉ số ở một chỗ duy nhất (đóng gói).
 */
public class ArrayWrapper {
    private final int[] data;
    private int comparisons;
    private int writes;

    /** Tạo mảng mới bằng cách sao chép values (không giữ tham chiếu tới mảng bên ngoài). */
    public ArrayWrapper(int[] values) {
        data = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            data[i] = values[i];
        }
    }

    public int size() {
        return data.length;
    }

    public int get(int index) {
        checkIndex(index);
        return data[index];
    }

    public void set(int index, int value) {
        checkIndex(index);
        data[index] = value;
        writes++;
    }

    /** So sánh a[i] với a[j]: trả về số âm, 0 hoặc số dương. Tính là 1 lần so sánh. */
    public int compare(int i, int j) {
        checkIndex(i);
        checkIndex(j);
        return compareValues(data[i], data[j]);
    }

    /** So sánh hai giá trị bất kỳ (dùng khi giá trị nằm ngoài mảng, ví dụ mảng phụ của merge sort). */
    public int compareValues(int a, int b) {
        comparisons++;
        if (a < b) {
            return -1;
        }
        return a > b ? 1 : 0;
    }

    public void swap(int i, int j) {
        checkIndex(i);
        checkIndex(j);
        int temp = data[i];
        data[i] = data[j];
        data[j] = temp;
        writes += 2;
    }

    /** Bản sao độc lập, bộ đếm bắt đầu lại từ 0. */
    public ArrayWrapper copy() {
        return new ArrayWrapper(data);
    }

    /** Bản sao nội dung dưới dạng int[] để chụp ảnh từng bước. */
    public int[] toArray() {
        int[] result = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = data[i];
        }
        return result;
    }

    public int getComparisons() {
        return comparisons;
    }

    public int getWrites() {
        return writes;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= data.length) {
            throw new IndexOutOfBoundsException("Index " + index + " out of range 0.." + (data.length - 1));
        }
    }
}
