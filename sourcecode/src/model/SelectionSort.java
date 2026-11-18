package model;

/** Selection sort: mỗi lượt chọn phần tử nhỏ nhất của phần chưa sắp xếp rồi đưa về đầu phần đó. */
public class SelectionSort extends SortAlgorithm {

    public SelectionSort() {
        super("Selection Sort");
    }

    @Override
    public void sort() {
        int n = array.size();
        for (int i = 0; i < n - 1; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                recordStep(j, min, i, n - 1, SortStep.COMPARE,
                        "Compare a[" + j + "]=" + array.get(j) + " with the current minimum a[" + min + "]=" + array.get(min));
                if (array.compare(j, min) < 0) {
                    min = j;
                }
            }
            if (min != i) {
                int a = array.get(i);
                int b = array.get(min);
                array.swap(i, min);
                recordStep(i, min, i, n - 1, SortStep.SWAP,
                        "Swap a[" + i + "]=" + a + " with the minimum a[" + min + "]=" + b);
            }
        }
    }
}
