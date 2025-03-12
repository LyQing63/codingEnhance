package Sort;

public class HeapSort {
    public static void sort(int[] list) {
        int len = list.length;
        // 变成最大堆
        for (int i = len/2-1; i>=0; i--) {
            heapify(list, len, i);
        }
        // 将最大值放到末尾，调整最大堆
        for (int i=len-1; i>0; i--) {
            swap(list, 0, i);
            heapify(list, i, 0);
        }
    }

    private static void heapify(int[] list, int n, int i) {
        int largest = i;
        int left = i * 2 + 1;
        int right = i * 2 + 2;
        if (left < n && list[left] > list[largest]) {
            largest = left;
        }
        if (right < n && list[right] > list[largest]) {
            largest = right;
        }
        if (largest != i) {
            swap(list, i, largest);
            heapify(list, n, largest);
        }
    }

    private static void swap(int[] list, int i, int j) {
        int temp = list[i];
        list[i] = list[j];
        list[j] = temp;
    }
}
