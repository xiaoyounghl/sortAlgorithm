package com.xiaoyoung.study.algorithm;

import java.util.*;

/**
 * @param <E>
 * @see #orderCompare(Object, Object) 顺序比较器统一比较方法 √
 * @see #sort() 将数组进行排序 √
 * @see #printArs(List) 输出数组内容 √
 * @see #checkOrder()  检查数组是否按顺序排列 √
 */
public abstract class BaseSort<E> {

    /**
     * c.compare(a, b) > 0
     *
     * @param comparator c
     * @return this
     */
    public BaseSort<E> loadComparator(Comparator<? super E> comparator) {
        if (comparator == null)
            throw new IllegalArgumentException("排序比较器 Comparator<? super E> comparator 不存在或为空！");
        else if (this.c == comparator) return this;
        else {
            this.c = comparator;
            return this;
        }
    }

    public BaseSort<E> setOrder(boolean asc) {
        this.asc = asc;
        return this;
    }

    public BaseSort<E> loadArrays(E[] list) {
        if (list == null) {
            throw new IllegalArgumentException("BaseSort.loadArrays(E[] list); list不能为空！");
        }
        this.arrays = Arrays.asList(list);
        return this;
    }

    public BaseSort<E> loadArrays(Collection<E> collection) {
        if (collection == null)
            throw new IllegalArgumentException("待排序数据 collection 不存在或为空！");
        else if (this.arrays == collection) return this;
        else {
            this.arrays = new ArrayList<>();
            arrays.addAll(collection);
            return this;
        }
    }

    public List<E> getResult() {
        return arrays;
    }

    // 排序比较器
    protected Comparator<? super E> c;
    // 排序方式
    protected boolean asc = true;
    // 待排序数据
    protected List<E> arrays;

    abstract public List<E> sort();


    /**
     * 自检 排序比较器和数据是否存在
     */
    protected void baseCheck() {
        this.loadComparator(this.c);
        this.loadArrays(this.arrays);
    }

    /**
     * 顺序比较器统一比较方法 √
     *
     * @param a ObjectA
     * @param b ObjectB
     * @return c.compare(a, b) > 0
     */
    protected boolean orderCompare(E a, E b) {
        return c.compare(a, b) > 0;
    }

    protected boolean gt(E a, E b) {
        return orderCompare(a, b);
    }

    protected boolean lt(E a, E b) {
        return orderCompare(b, a);
    }

    protected boolean notEquals(E a, E b) {
        return !equals(a, b);
    }

    protected boolean equals(E a, E b) {
        return Objects.equals(a, b);
    }

    /**
     * 检查数组是否按顺序排列 √
     *
     * @return 检查结果
     * @see #orderCompare(Object, Object)
     */
    public boolean checkOrder() {
        baseCheck();
        int size = arrays.size();
        if (size < 2) {
            return true;
        } else {
            E curr = arrays.get(0);
            for (int idx = 1; idx < arrays.size(); idx++) {
                E next = arrays.get(idx);
                if (equals(curr, next)) continue;
                if (asc && orderCompare(curr, next)) return false;
                if (!asc && orderCompare(next, curr)) return false;
                curr = next;
            }
        }
        return true;
    }

    /**
     * 输出数组内容 √
     *
     * @param list 数组
     */
    public static <E> void printArs(List<E> list) {
        for (E obj : list) {
            if (null != obj)
                System.out.print(obj.toString() + ", ");
            else
                System.out.print("null, ");
        }
        System.out.println();
    }


}
