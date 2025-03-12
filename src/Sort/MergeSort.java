package Sort;

public class MergeSort {
    public static void sort(int[] list) {
        doMerge(list, 0, list.length-1);
    }

    private static void doMerge(int[] list, int left, int right) {
        if (left >= right) {
            return;
        }
        int mid = (left + right)/2;
        doMerge(list, left, mid);
        doMerge(list, mid+1, right);
        merge(list, left, mid, right);
    }

    private static void merge(int[] list, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid+1, k=0;
        while (i<=mid && j<=right) {
            if (list[i] >= list[j]) {
                temp[k++] = list[j++];
            } else {
                temp[k++] = list[i++];
            }
        }
        while (i<=mid) temp[k++] = list[i++];
        while (j<=right) temp[k++] = list[j++];

        System.arraycopy(temp, 0, list, left, right-left+1);
    }
}
