package Heap;

/*
* 4种排序算法对比如下：
*
*                   平均时间复杂度    原地排序     额外空间     稳定排序
*   Insertion Sort     O(n^2)          ✅         O(1)         ✅
*     Merge Sort       O(nlogn)        ❌         O(n)         ✅
*     Quick Sort       O(nlogn)        ✅         O(logn)      ❌
*     Heap Sort        O(nlogn)        ✅         O(1)         ❌
*
* 其中：
*   - 平均时间复杂度：
*     - Insertion Sort 对近乎有序的数据是 O(n) 的复杂度。
*     - Quick Sort 因为使用了随机化的方式实现，因此只在极小概率下会退化成 O(n^2)。
*     - 3种 O(nlogn) 的排序算法复杂度在同一级别，但存在常数上的差异，Quick Sort 最快。
*
*   - 原地排序 & 额外空间：
*     - Merge Sort 不能原地排序，必须开辟新空间，因此如果对于空间敏感，Merge Sort 则不适用。
*     - Insertion Sort 和 Heap Sort 是原地排序因此需要开辟的额外空间是 O(1)，即只开辟几个变量的空间。
*     - 而 Quick Sort 比较特殊，虽然它也是原地排序，但因为采用递归实现，递归深度为 logn，因此需要开辟 logn 的栈空间来保存每次递归
*       过程中的中间变量，以便下一层结束后可以返回上一层继续使用。
*     - Merge Sort 也可以采用递归实现，深度同样为 logn，因此开辟的额外空间应该是 n + logn，但因为 n > logn，因此总体量级是 O(n)。
*
*   - 排序算法稳定性：
*     - Quick Sort 和 Heap Sort 是不稳定的。
*     - 注意稳定性和具体实现息息相关：
*       - 稳定的算法如果实现的有问题，也会变成不稳定。
*       - 不稳定的算法通过重载运算符（如 < >）也可以变得稳定。
*
*   - 是否存在平均复杂度为 O(nlogn)、对于有序的情况为 O(n)、可以原地排序、额外空间消耗为 O(1)，同时又稳定的排序算法？
*     - 理论上存在，但是现实中还没找到。
* */

public class SummaryOfSortingAlgorithms {
    /* No content */
}
