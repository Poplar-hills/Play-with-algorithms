package MinimumSpanningTree;

/*
* Prim 算法：
*
* - 用途：寻找最小生成树，但能提供比 Lazy Prim 更优的性能：
*   - Lazy Prim 是 O(ElogE)
*   - Prim 是 O(ElogV)
*
* - 背景：
*   在 Lazy Prim 算法过程中，每次有新顶点加入切分内侧时，都会将与该顶点相连的所有横切边加入堆中进行比较（最后会使得所有边都被
*   加入堆中，即堆中有 E 条边，因此该堆的每次操作的复杂度都是 logE）。但从结果上看，每个顶点的邻边中最终只会有一条进入最小生成
*   树，因此如果算法以 V 个顶点为出发点，在每次切分过程中为每个顶点找到其当前最小的横切边，就只需要 logV 的复杂度。
*
* - 改进方案：使用 Prim 算法：
*   - 增加一个数据结构用来为每个顶点保存当前时刻与其相连的最小的横切边，并在切分不断扩大的过程中持续更新，使他们一直保持最小；
*   - 那么下一个问题是，什么样的数据结构能满足这2个需求：1.要能找一组元素中的最小值 2.能随时更新其中的任意元素；
*   - 答案就是最小索引堆（Minimum Index Heap）。
*
* - 👉 看动画演示后再理解一遍以上文字：https://coding.imooc.com/lesson/71.html#mid=1490（3'34''）
* */

import MinimumSpanningTree.WeightedGraphReader.WeightedGraphReader;

import java.util.ArrayList;
import java.util.List;

import static Utils.Helpers.log;

public class PrimMST<Weight extends Number & Comparable> {
    private WeightedGraph graph;
    private boolean[] visited;
    private List<Edge<Weight>> mst;
    private IndexMinHeap<Edge<Weight>> indexHeap;
    private Number minWeight;

    public PrimMST(WeightedGraph graph) {
        this.graph = graph;
        visited = new boolean[graph.getVertexCount()];
        mst = new ArrayList<>();
        indexHeap = new IndexMinHeap<>(graph.getVertexCount());
        minWeight = 0;

        prim();  // 开始计算最小生成树
    }

    private void prim() {
        visit(0);
        while (!indexHeap.isEmpty()) {
            Edge<Weight> e = indexHeap.extractMin();  // 得到最小横切边
            mst.add(e);
            visit(visited[e.v()] ? e.w() : e.v());
        }

        for (Edge<Weight> e : mst)
            minWeight = minWeight.doubleValue() + e.weight().doubleValue();
    }

    private void visit(int v) {
        if (visited[v])
            throw new IllegalArgumentException("visit failed. Vertex has already been visited.");

        visited[v] = true;

        Iterable<Edge<Weight>> it = graph.getAdjacentVertexes(v);
        for (Edge<Weight> e : it) {
            int w = e.theOther(v);
            if (!visited[w]) {
                if (indexHeap.contains(w)) {
                    if (e.weight().compareTo(indexHeap.getItem(w).weight()) < 0)
                        indexHeap.change(w, e);
                } else
                    indexHeap.insert(w, e);
            }
        }
    }

    public List<Edge<Weight>> mstEdges() { return mst; }

    public Number weight() { return minWeight; }

    public static void main(String[] args) {
        WeightedGraph<Double> g = new WeightedGraphReader()
                .read("src/main/java/MinimumSpanningTree/WeightedGraphReader/testG1.txt")
                .build(WeightedSparseGraph.class, false);
        log(g);

        PrimMST<Double> mst = new PrimMST<>(g);
        log(mst.mstEdges());  // 结果应该与 https://coding.imooc.com/lesson/71.html#mid=1489（8'41''）中的红色边一致
        log(mst.weight());
    }
}
