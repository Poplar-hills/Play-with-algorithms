package MinimumSpanningTree;

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
*
* - 如何找到一幅图的最小生成树：
*   - 两种算法：Prim 算法、Krusk 算法
*   - 这两种算法都应用了 Cut Property（切分定理）
*
* - 切分定理（Cut Property）：
*   - 切分（Cut）：把图中的顶点分为两部分的过程就是一个切分。
*   - 横切边（Crossing Edge）：一个边上的两个顶点分别属于一个切分的两部分中，则该边叫做横切边。
*     图形化解释 SEE: https://coding.imooc.com/lesson/71.html#mid=1488（4'00''）。
*   - 切分定理（Cut Property）：给定一幅图的任意切分，其横切边中权值最小的边必然在该图的最小生成树中。
*     证明：将一副有 n 个顶点的图切成分别具有1个顶点和 n-1 个顶点的两部分。此时图中的横切边即是第一部分中那一个顶点的
*          所有邻边，而其中权值最小的边一定在最小生成树上（比如上面👆链接中的图中的5号顶点）。
*         
* */

import java.util.ArrayList;
import java.util.List;

public class LazyPrimMST<Weight extends Number & Comparable> {
    private WeightedGraph graph;
    private boolean[] marked;            // 用于标记节点 i 是否被访问过
    private List<Edge<Weight>> mst;      // 最小生成树所包含的所有边
    private MinHeap<Edge<Weight>> heap;  // 用于比较边的最小堆（辅助数据结构）
    private Weight minWeight;            // 最小生成树的权值之和

    public LazyPrimMST(WeightedGraph graph) {
        this.graph = graph;
        int n = graph.getVertexCount();
        marked = new boolean[n];                   // boolean 数组初值都为 false
        mst = new ArrayList<>();

        lazyPrim();
    }

    private void lazyPrim() {
        visit(0);
        while (!heap.isEmpty()) {
            Edge<Weight> e = heap.extractMin();
            if (marked[e.v()] && marked[e.w()])  // 如果一条横切边的两个端点都被 mark 过了，说明它不再是横切边了
                continue;
            mst.add(e);  // 将该边（目前权重最小的横切边）添加到最小生成树中
        }
    }

    private void visit(int v) {
        if (marked[v])
            throw new IllegalArgumentException("visit failed. Vertex has already been marked.");

        marked[v] = true;
        for (Edge<Weight> e : graph.getAdjacentVertexes(v))
            if (!marked[e.theOther(v)])  // 若另一个顶点没有被 mark 则说明该边是横切边（crossing edge），应加入堆中比较
                heap.insert(e);
    }

    public Edge<Weight>[] mstEdges() { return mst; }

    public Weight weight() { return minWeight; }

}
