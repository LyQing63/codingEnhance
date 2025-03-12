package Sort;

import java.util.Arrays;
import java.util.Scanner;

/**
 * ACM模式
 * input: 9 -> n
 * input: 1,4,2,6,8,3,9,5,7 -> list
 */
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        in.nextLine(); // 读取换行符，避免影响下一行输入
        int[] list = new int[n];
        String[] strList = in.nextLine().trim().split(",");
        for (int i=0; i<n; i++) {
            list[i] = Integer.parseInt(strList[i]);
        }
        in.close();
        System.out.println("Origin: " + Arrays.toString(list));
        int[] quickSortList = Arrays.copyOf(list, n);
        QuickSort.sort(quickSortList);
        System.out.println("QuickSort: " + Arrays.toString(quickSortList));
        int[] mergeSortList = Arrays.copyOf(list, n);
        MergeSort.sort(mergeSortList);
        System.out.println("MergeSort: " + Arrays.toString(mergeSortList));
        int[] heapSortList = Arrays.copyOf(list, n);
        HeapSort.sort(heapSortList);
        System.out.println("HeapSort: " + Arrays.toString(heapSortList));
    }


}
