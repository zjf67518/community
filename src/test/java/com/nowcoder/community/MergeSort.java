package com.nowcoder.community;

import java.util.Arrays;

public class MergeSort {
    public static void main(String[] args) {
        int[] nums = {12,4,13,25,92,11,9,3,2};
        sort(nums, 0, nums.length-1);
        System.out.println(Arrays.toString(nums));
    }

    private static void sort (int[] arr, int left, int right) {
        int mid = left + (right - left) / 2;
        if(left < right) {
            sort(arr, left, mid);
            sort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }

    }

    private static void merge(int[] arr, int left, int mid, int right) {
        int[] tmp = new int[right - left + 1];
        int k = 0;
        int l = left;
        int r = mid + 1;
        while(l<= mid && r <= right) {
            if(arr[l] <= arr[r]) {
                tmp[k++] = arr[l++];
            } else {
                tmp[k++] = arr[r++];
            }
        }
        while(r <= right) {
            tmp[k++] = arr[r++];
        }
        while(l <= mid) {
            tmp[k++] = arr[l++];
        }
        for(int i = 0; i < tmp.length; i++) {
            arr[i + left] = tmp[i];
        }
    }

}
