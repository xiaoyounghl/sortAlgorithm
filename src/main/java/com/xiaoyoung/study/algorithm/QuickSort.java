package com.xiaoyoung.study.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuickSort<E> extends BaseSort<E> {

    public static void main(String[] args) {
        QuickSort<Integer> hs = new QuickSort<>();
        hs.loadComparator(Integer::compareTo).setOrder(true);
        List<Integer> arrays;
        if (false) {
            Integer[] ints = new Integer[]{82, 60, 32, 34, 612, 64, 123, 74, 234, 3, 6, 2, 61, 23, 9, 83};
//                              ints = new Integer[]{82, 60};
            arrays = Arrays.asList(ints);
        } else {
            arrays = new ArrayList<>();
            for (int i = 0; i < 500; i++) arrays.add((int) (Math.random() * 89999) + 10000);
        }
        hs.loadArrays(arrays);
        long time = System.currentTimeMillis();
        hs.sort();
        time -= System.currentTimeMillis();
        System.out.println("排序总耗时： " + time + " ms");
        printArs(hs.getResult());
        if (hs.checkOrder())
            System.out.println("排序验证通过！\n===================================");

    }

    /**
     * 思路：
     * 快速排序算法通过多次比较和交换来实现排序，其排序流程如下：
     * (1)首先设定一个分界值，通过该分界值将数组分成左右两部分。
     * (2)将大于或等于分界值的数据集中到数组右边，小于分界值的数据集中到数组的左边。此时，左边部分中各元素都小于或等于分界值，而右边部分中各元素都大于或等于分界值。
     * (3)然后，左边和右边的数据可以独立排序。对于左侧的数组数据，又可以取一个分界值，将该部分数据分成左右两部分，同样在左边放置较小值，右边放置较大值。右侧的数组数据也可以做类似处理。
     * (4)重复上述过程，可以看出，这是一个递归定义。通过递归将左侧部分排好序后，再递归排好右侧部分的顺序。当左、右两个部分各数据排序完成后，整个数组的排序也就完成了。
     *
     * @return 排序后的数组
     * @see #sort(List, int, int)
     * @see #lt(Object, Object)
     */
    public List<E> sort() {
        // 选取一个值作为 标准值(start) 一般选第一个值
        sort(arrays, 0, arrays.size() - 1);
        return arrays;
    }

    public void sort(List<E> list, int start, int end) {
        if (start < end) {
            // 取出标准值
            E std = list.get(start);
            // 记录标准值的下标位置
            int pos = start;
            // 遍历list(start,end]
            for (int i = start + 1; i <= end; i++) {
                // 升序排列：将大于或等于 std 的值放右边部分，其余的放左边部分
                // 降序排列：将小于或等于 std 的值放右边部分，其余的放左边部分
                if (asc && lt(list.get(i), std) || !asc && gt(list.get(i), std)) {
                    // 将目标值移至pos位置，pos位置的值在开始时已经缓存到fo
                    list.set(pos, list.get(i));
                    // 将pos+1的值移向i位置，给pos腾出位置
                    list.set(i, list.get(pos + 1));
                    // 将pos处的值移到pos+1处
                    list.set(++pos, std);
                }
            }
            // 将左边部分的数组再进行上述排序
            sort(list, start, pos - 1);
            // 将右边部分的数组再进行上述排序
            sort(list, pos + 1, end);
        }
    }
}