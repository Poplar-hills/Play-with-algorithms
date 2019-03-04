package MinimumSpanTree;

/*
* Minimum Spanning Tree（最小生成树）
*
* - 背景：在 ShortestPath 中，我们说过"很多图的问题实际上是树的问题，或者说可以转化为树的问题来解决"。最小生成树的问题就是这样的。
*
* - 定义：
*   - 生成树：一副无向图的生成树是具有该图的全部顶点，同时边数最少的连通子图。
*   - 最小生成树：在一副带权图的生成树中，总权重最小的生成树称为最小生成树。更通俗的解释：如果一幅图有 n 个顶点，则至少需要 n-1
*     条边才能把所有节点都连接起来，形成一棵生成树。而在一幅图中，这样的生成树可能有多个，但是他们的权重之和不同，其中权重之和最
*     小的即是最小生成树。
*
* - 条件：
*   一幅图必须要满足 1.带权 2.无向 3.连通 这三个条件才能找到其最小生成树。
*     - 如果图不带权，则有生成树，但无最小生成树。
*     - 如果图有向，则没有最小生成树（最小生成树是针对无向图而言的，有向图上的这类问题叫做最小树形图）。
*     - 如果图不连通（即存在多个连通分量），则可以对每个分量求出最小生成树，最后得到的是该图的最小生成森林。
*
* - 应用：
*   最小生成树中的"权之和"可以看做是对成本之和的抽象。例如：
*     - 电缆布线（cabling）：如何用最小成本将发电站的电传输至电网中的每一个节点
*     - 网络设计
*     - 电路设计
* */

public class MinSpanTree {
}
