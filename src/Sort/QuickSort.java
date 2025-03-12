package Sort;

public class QuickSort {
    public static void sort(int[] list) {
        quickSort(list, 0, list.length-1);
    }

    private static void quickSort(int[] list, int left, int right) {
        if (left >= right) {
            return;
        }
        // 左侧作为基准
        int pivot = list[right];
        int i = left - 1;
        for (int j = left; j < right; j++) {
            if (list[j] < pivot) {
                i++;
                swap(list, i, j);
            }
        }
        swap(list, i+1, right);
        quickSort(list, left, i);
        quickSort(list, i+1, right);
    }

    private static void swap(int[] list, int i, int j) {
        int tmp = list[i];
        list[i] = list[j];
        list[j] = tmp;
    }

}
