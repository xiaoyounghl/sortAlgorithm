package com.xiaoyoung.study.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * desc: 堆排序方法实现
 * author: xiaoyoung
 * date: 2021/07/21
 *
 * @see #checkHeap(boolean) 检查list是否符合堆排序要求 √
 * @see #treeSwap(int, int) 对二叉树指定的节点和他的子节点进行排序 √
 * @see #nodeSwap(int, int, boolean) 对数组中指定的两个数据进行排序 √
 * @see #convert(int) 转换不符合堆排序要求的数组(建堆) √
 * @see #sort() 将符合堆排序要求的数组进行排序 √
 * @see #leftNidx(int) 获取左子节点的index √
 * @see #lastNidx(int) 获取二叉树最后一个节点的index √
 * @see #getDeeps(int) 根据总节点数计算二叉树深度 √ nouse
 */
public class HeapSort<E> extends BaseSort<E> {

    private boolean asc() {
        return !asc;
    }

    public static void main(String[] args) {
        HeapSort<Integer> hs = new HeapSort<>();
        hs.loadComparator(Integer::compareTo).setOrder(false);

        boolean debug = false;

        while (true) {
            List<Integer> arrays;
            if (debug) {
                Integer[] ints = new Integer[]{82, 60};
//                              ints = new Integer[]{82, 60};
                arrays = Arrays.asList(ints);
            } else {
                arrays = new ArrayList<>();
                for (int i = 0; i < 5000; i++) arrays.add((int) (Math.random() * 89999) + 10000);
            }
//            System.out.println("原数列：");
//            printArs(arrays);
            hs.loadArrays(arrays);
            long time = System.currentTimeMillis();
            hs.sort();
            time -= System.currentTimeMillis();
            System.out.println("排序总耗时： " + time + " ms");
            printArs(hs.getResult());
            if (hs.checkOrder()) {
                System.out.println("排序验证通过！\n===================================");
//                if (debug)
                break;
            } else {
                break;
            }
        }
    }

    /**
     * 将符合堆排序要求的数组进行排序 √
     *
     * @return 排序结果
     * @see #baseCheck()
     * @see #checkHeap(boolean)
     * @see #convert(int)
     * @see #nodeSwap(int, int, boolean)
     */
    public List<E> sort() {
        baseCheck();
        int size = arrays.size();
        if (size < 2) {
            return arrays;
        } else {
            // 检查：属于堆结构 且 符合升序/降序要求
            if (!checkHeap(asc()))
                // 如果不符合则重新进行建堆操作
                convert(size);
            // 开始排序
            for (int idx = 0; idx < size; ) {
                // 根据排序方式 将头部和尾部交换
                // 利用--size来隔离已经排好序的数据
                // 因为是逆向移动 所以需要 !asc() 保证交换
                nodeSwap(0, --size, asc);
                // 头部和尾部交换之后，结构被打乱，需要重新构建堆
                convert(size);
            }
        }
        return arrays;
    }

    /**
     * 转换不符合堆排序要求的数组(建堆) √
     * <p>
     * 思路：
     * 从最后一个拥有子节点的节点开始一一排序
     * 排完一轮下来，root节点一定是最大/小的节点
     *
     * @param size 数组限制大小
     * @see #treeSwap(int, int)
     */
    private void convert(int size) {
        for (int root = lastNidx(size); root >= 0; root--)
            treeSwap(root, size);
    }

    /**
     * 检查list是否符合堆排序要求 √
     * r(i) <= r(2i) && r(i) <= r(2i+1)
     * r(i) >= r(2i) && r(i) >= r(2i+1)
     *
     * @return 检查结果
     * @see #gt(Object, Object)
     * @see #lt(Object, Object)
     * @see #lastNidx(int)
     * @see #leftNidx(int)
     */
    public boolean checkHeap(boolean asc) {
        baseCheck();
        if (arrays.size() < 2) {
            return true; // 少于俩数据 排个锤子序
        } else if (arrays.size() == 2) {
            E _0 = arrays.get(0), _1 = arrays.get(1);
            // 两个数据相同，直接抬走
            if (equals(_0, _1)) return true;
            if (asc) return lt(_0, _1); // 升序排列时，后者比前者要大
            else return gt(_0, _1); // 降序排列时，前者比后者要大
        } else { // 数据超过2个，有至少一个完整的二叉树结构
            // 0 是第一个节点*，lastNidx(size) 是最后一个节点*，遍历检查所有节点*是否通过
            // 节点指的是拥有子节点的节点
            for (int idx = 0; idx <= lastNidx(arrays.size()); idx++) {
                // 算出左子节点的index
                int leftIdx = leftNidx(idx);
                // 方便代码编写，先把数据取出来
                E root = arrays.get(idx), left = arrays.get(leftIdx);
                // 两者不相同的情况下才能比较大小
                if (notEquals(left, root)) {
                    // 升序排序时 root(父)不能大于left()
                    if (asc && gt(root, left)) return false;
                    // 降序时 不能子>父
                    if (!asc && lt(root, left)) return false;
                }
                // 有右子节点的情况下，右子节点做同样判断
                if (leftIdx + 1 < arrays.size()) {
                    E right = arrays.get(leftIdx + 1);
                    if (notEquals(right, root)) {
                        if (asc && gt(root, right)) return false;
                        if (!asc && lt(root, right)) return false;
                    }
                }
            }
            // 全部检查完无异常则通过
            return true;
        }
    }

    /**
     * 对二叉树指定的节点和他的子节点进行排序 √
     * <p>
     * 思路：
     * 先找出子节点
     * 如果只有左，直接调用anSwap(root, left)。
     * 如果有左右，先比较左右选出符合顺序的子节点
     * （升序则选出最小的子节点，降序反之）
     * 将根节点与选出的节点进行anSwap(root, swap)。
     *
     * @param root 指定节点作为根节点
     * @param size 限制(数组)大小 => 限制排序
     * @see #leftNidx(int)
     * @see #lastNidx(int)
     * @see #gt(Object, Object)
     * @see #lt(Object, Object)
     * @see #nodeSwap(int, int, boolean)
     * @see #treeSwap(int, int)
     */
    private void treeSwap(int root, int size) {
        // 根节点 >= 限制大小 //无需操作
        if (root >= size) return;
        // 获取左子节点
        int idx = leftNidx(root);
        // 有左子节点 // 无子节点则无需操作
        if (idx < size) {
            // 有右子节点 // 无则 默认取左节点
            if (idx + 1 < size) {
                E left = arrays.get(idx), right = arrays.get(idx + 1);
                // 左右节点数据相同时 默认取左节点
                if (notEquals(left, right)) {
                    if (asc()) { // 升序时 应该选出小的节点
                        // 右 < 左 时 idx + 1 得到右节点 idx
                        if (gt(left, right)) {
                            idx++;
                        }
                    } else { // 降序时 应该选出大的节点
                        // 右 > 左 时 idx + 1 得到右节点 idx
                        if (lt(left, right)) {
                            idx++;
                        }
                    }
                }
            }
            nodeSwap(root, idx, asc());
            // 如果子节点也有子节点，那么上一次排序可能会使其乱序
            // idx 有可能 +1 所以需要重新判断 idx < size
            if (idx <= lastNidx(size) && idx < size) {
                // 满足上述条件则需要对子节点重新排序
                treeSwap(idx, size);
            }
        }
    }

    /**
     * 对数组中指定的两个数据进行排序 √
     * <p>
     * 思路：
     *
     * @param root 基准数据的 index
     * @param swap 对比数据的 indexs
     * @see #gt(Object, Object)
     * @see #lt(Object, Object)
     */
    private void nodeSwap(int root, int swap, boolean asc) {
        // 根节点总是在子节点前面
        if (root > swap) {
            int tmp = root;
            root = swap;
            swap = tmp;
        }
        // 两数据相同时无需交换
        if (equals(arrays.get(swap), arrays.get(root))) return;
        // 升序排列 时 子数据 > 根数据
        if (asc && lt(arrays.get(root), arrays.get(swap))) return;
        // 降序排列 时 根数据 > 子数据
        if (!asc && gt(arrays.get(root), arrays.get(swap))) return;
        // 否则需要互换数据
        E tmp = arrays.get(swap);
        arrays.set(swap, arrays.get(root));
        arrays.set(root, tmp);
    }

    /**
     * 获取左子节点的index √
     *
     * @param root 父节点index
     * @return 2 * root + 1
     */
    public static int leftNidx(int root) {
        return 2 * root + 1;
    }

    /**
     * 获取二叉树最后一个节点的index √
     *
     * @param size 总节点数
     * @return size < 2 ? 0 : size / 2 - 1
     */
    public static int lastNidx(int size) {
        assert size != 0;
        return size < 2 ? 0 : size / 2 - 1;
    }

    /**
     * 根据总节点数计算二叉树高度(深度) √
     *
     * @param size 总节点数
     * @return deep dark fan...
     */
    public static int getDeeps(int size) {
        int result = 1;
        if (size < 2) return size;
        else while ((size /= 2) > 1) result += 1;
        return result + 1;
    }

}
