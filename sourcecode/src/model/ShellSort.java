package model;

/** Shell sort với dãy khoảng cách n/2, n/4, ..., 1: insertion sort trên các phần tử cách nhau gap. */
public class ShellSort extends SortAlgorithm {

    public ShellSort() {
        super("Shell Sort");
    }

    @Override
    public void sort() {
        int n = array.size();
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                for (int j = i; j >= gap; j -= gap) {
                    recordStep(j - gap, j, SortStep.COMPARE,
                            "gap=" + gap + ": compare a[" + (j - gap) + "]=" + array.get(j - gap)
                                    + " with a[" + j + "]=" + array.get(j));
                    if (array.compare(j - gap, j) > 0) {
                        int a = array.get(j - gap);
                        int b = array.get(j);
                        array.swap(j - gap, j);
                        recordStep(j - gap, j, SortStep.SWAP,
                                "gap=" + gap + ": swap a[" + (j - gap) + "]=" + a + " with a[" + j + "]=" + b);
                    } else {
                        break;
                    }
                }
            }
        }
    }
}
