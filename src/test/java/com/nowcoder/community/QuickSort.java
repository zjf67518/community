package com.nowcoder.community;

import java.util.Arrays;

public class QuickSort {
    public static void main(String[] args) {
        int[] nums = {12, 4, 13, 25, 92, 11, 9, 3, 2};
        sort(nums, 0, nums.length - 1);
        System.out.println(Arrays.toString(nums));
    }

    private static void sort (int[] arr, int left, int right) {
        if(left < right){
            int index = newIndex (arr, left, right);
            sort(arr, left, index-1);
            sort(arr, index+1, right);
        }
    }

    private static int newIndex (int[] arr, int left, int right) {
        int tmp = arr[left];
        while(left < right) {
            while(left < right && tmp <= arr[right]) {
                right--;
            }
            arr[left] = arr[right];
            while(left < right && tmp >= arr[left]) {
                left++;
            }
            arr[right] = arr[left];
        }
        arr[left] = tmp;
        return right;
    }

}
