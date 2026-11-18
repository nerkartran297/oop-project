import model.*;

/** Test không cần JUnit: chạy 3 thuật toán trên các ca biên và kiểm tra kết quả + chuỗi bước. */
public class SortTests {
    static int failures = 0;
    static int checks = 0;

    public static void main(String[] args) {
        int[][] cases = {
                {},                              // rỗng
                {7},                             // 1 phần tử
                {1, 2, 3, 4, 5},                 // đã sắp xếp
                {9, 7, 5, 3, 1},                 // ngược
                {4, 4, 4, 4},                    // toàn trùng
                {5, 2, 9, 1, 7, 3},              // mảng trace tay của nhóm
                {3, 1, 3, 2, 1, 2, 3},           // có trùng
                {0, 99, 50, 0, 99, 1, 98, 2, 97, 3, 96, 4, 95, 5, 94, 6, 93, 7, 92, 8},
        };
        for (int[] c : cases) {
            check(new SelectionSort(), c);
            check(new MergeSort(), c);
            check(new ShellSort(), c);
        }
        randomCases();
        System.out.println(checks + " checks, " + failures + " failures");
        if (failures > 0) {
            System.exit(1);
        }
    }

    static void randomCases() {
        long seed = 12345;
        for (int t = 0; t < 300; t++) {
            seed = (seed * 1103515245L + 12345L) & 0x7fffffffL;
            int n = (int) (seed % 21);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                seed = (seed * 1103515245L + 12345L) & 0x7fffffffL;
                a[i] = (int) (seed % 100);
            }
            check(new SelectionSort(), a);
            check(new MergeSort(), a);
            check(new ShellSort(), a);
        }
    }

    static void check(SortAlgorithm alg, int[] input) {
        ArrayWrapper in = new ArrayWrapper(input);
        alg.setArray(in);
        alg.run();
        String label = alg.getName() + " " + show(input);

        int last = alg.getStepCount() - 1;
        expect(last >= 1, label + ": has START and DONE steps");
        expect(SortStep.START.equals(alg.getStep(0).getAction()), label + ": first step is START");
        expect(SortStep.DONE.equals(alg.getStep(last).getAction()), label + ": last step is DONE");
        expect(sameContent(alg.getStep(0).getValues(), input), label + ": START shows the input");

        int[] out = alg.getStep(last).getValues();
        expect(isSorted(out), label + ": result sorted -> " + show(out));
        expect(sameMultiset(out, input), label + ": result is a permutation of the input");
        expect(sameContent(in.toArray(), input), label + ": input wrapper not modified");

        for (int s = 0; s <= last; s++) {
            expect(alg.getStep(s).getValues().length == input.length, label + ": step " + s + " keeps the size");
        }

        // chạy lại sau reset() phải ra cùng kết quả và cùng số bước
        int steps = alg.getStepCount();
        alg.run();
        expect(alg.getStepCount() == steps, label + ": run() twice gives the same step count");
    }

    static void expect(boolean ok, String message) {
        checks++;
        if (!ok) {
            failures++;
            System.out.println("FAIL: " + message);
        }
    }

    static boolean isSorted(int[] a) {
        for (int i = 1; i < a.length; i++) {
            if (a[i - 1] > a[i]) {
                return false;
            }
        }
        return true;
    }

    static boolean sameContent(int[] a, int[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    /** Đếm số lần xuất hiện của từng giá trị 0..99 (đủ cho test này). */
    static boolean sameMultiset(int[] a, int[] b) {
        if (a.length != b.length) {
            return false;
        }
        int[] count = new int[100];
        for (int v : a) {
            count[v]++;
        }
        for (int v : b) {
            count[v]--;
        }
        for (int c : count) {
            if (c != 0) {
                return false;
            }
        }
        return true;
    }

    static String show(int[] a) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < a.length; i++) {
            sb.append(i == 0 ? "" : ",").append(a[i]);
        }
        return sb.append("]").toString();
    }
}
