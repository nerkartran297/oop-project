package model;

/** Merge sort đệ quy (top-down): chia đôi, sắp xếp từng nửa rồi trộn hai nửa đã sắp xếp. */
public class MergeSort extends SortAlgorithm {

    public MergeSort() {
        super("Merge Sort");
    }

    @Override
    public void sort() {
        if (array.size() > 1) {
            mergeSort(0, array.size() - 1);
        }
    }

    private void mergeSort(int left, int right) {
        if (left >= right) {
            return;
        }
        int mid = (left + right) / 2;
        mergeSort(left, mid);
        mergeSort(mid + 1, right);
        merge(left, mid, right);
    }

    /** Trộn a[left..mid] và a[mid+1..right] (đã sắp xếp) dùng mảng phụ thuần int[]. */
    private void merge(int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        for (int k = 0; k < temp.length; k++) {
            temp[k] = array.get(left + k);
        }
        int i = 0;                      // vị trí trong nửa trái của temp
        int j = mid - left + 1;         // vị trí trong nửa phải của temp
        int leftEnd = mid - left;
        int rightEnd = right - left;
        int k = left;
        while (i <= leftEnd && j <= rightEnd) {
            recordStep(temp, left, i, j, left, right, SortStep.COMPARE,
                    "Merge [" + left + ".." + right + "]: compare " + temp[i] + " (left half) with " + temp[j] + " (right half)");
            if (array.compareValues(temp[i], temp[j]) <= 0) {
                array.set(k, temp[i]);
                recordStep(temp, left, k, i, left, right, SortStep.WRITE,
                        "Write " + temp[i] + " from the left half to a[" + k + "]");
                i++;
            } else {
                array.set(k, temp[j]);
                recordStep(temp, left, k, j, left, right, SortStep.WRITE,
                        "Write " + temp[j] + " from the right half to a[" + k + "]");
                j++;
            }
            k++;
        }
        while (i <= leftEnd) {
            array.set(k, temp[i]);
            recordStep(temp, left, k, i, left, right, SortStep.WRITE,
                    "Copy the remaining " + temp[i] + " from the left half to a[" + k + "]");
            i++;
            k++;
        }
        while (j <= rightEnd) {
            array.set(k, temp[j]);
            recordStep(temp, left, k, j, left, right, SortStep.WRITE,
                    "Copy the remaining " + temp[j] + " from the right half to a[" + k + "]");
            j++;
            k++;
        }
    }
}
