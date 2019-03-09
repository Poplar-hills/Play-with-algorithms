package MinimumSpanningTree;

/*
* Prim 算法：
*
* - 用途：寻找最小生成树，但能提供比 Lazy Prim 更优的性能：
*   - Lazy Prim 是 O(ElogE)
*   - Prim 是 O(ElogV)
*
* - 原理：
*   - 在 Lazy Prim 算法过程中：
*     1. 每次将新顶点加入切分内侧时，都会将与该顶点相连的所有横切边加入堆中，但实际上我们需要的只是其中权值最小的那条，而非全部。
*     2. 随着切分不断扩大，一些已经进入堆中的边不再是横切边（切分内侧顶点之间的边）。
*     这两点也是使得 Lazy Prim 需要将所有边都加入堆中进行比较的原因，从而使得其复杂度为 O(ElogE)。
*   - 改进：
*     如果我们可以增加一个数据结构用来保存与每个顶点相连的最小的横切边，并在切分不断扩大的过程中持续更新，这样一来：
*     1. 每次将新顶点加入切分内侧时，
*     2.
*     所以上两个问题就不存在了
* */

public class PrimMST {
}