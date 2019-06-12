package MinimumSpanningTree;

/*
* Kruskal 算法：
*
* - 在 LazyPrim、Prim 算法中，我们每次都是先找到一个切分的横切边，再找这些横切边中权值最小的那个，则该边即是最小生成树
*   中的一段。如果从另一个方向思考 —— 如果我们每次先去找所有边中的最小边，只要该边不和之前找到的最小边之间构成环（因为树
*   中是不能有环的），则这些边就是最小生成树中的边，这就是 Kruskal 算法的思想。
*   - 动画演示 SEE: https://coding.imooc.com/lesson/71.html#mid=1492（1'48''）
*
* - 证明：对于每次找到的最小边，我们都能找到一个切分，使得该最小边是该切分的横切边，因此符合切分定理（Cut Property）。
*
* - 算法过程：
*   1. 首先需要根据权值对所有边进行排序。
*   2. 对排序后的边从小到大进行遍历，检测每一条边是否会让最小生成树形成环，若不会则将该边加入最小生成树。
*   3. 直到所有顶点都被访问过后，得到了最小生成树。
*
* - 判断图中是否有环：
*   - 使用并查集（这是并查集的经典应用场景）
*   - 具体来说，在将一条边加入最小生成树之前要对该边的2个顶点进行 union 操作，看他们的根是否相同，若相同则说明会成环。
*
* - 实现过程 & 复杂度分析：
*   1. 对边排序可以选择任意一种排序算法。这里使用 O(nlogn) 的堆排序，因为有 E 条边，所以是 O(ElogE)；
*   2. 对边进行遍历的循环最多会执行 E 次（即堆被取空才将所有顶点都访问一遍）：
*     a. 每次从堆中取出最小边，这是 O(logE) 的复杂度；
*     b. 用并查集判断取出的边是否会让最小生成树成环，若使用优化过的并查集则是接近 O(1) 的复杂度；
*     c. 若没有成环则放入最小生成树，并将边上的节点 union 在一起，也是 O(1) 的复杂度;
*   - 因此，Kruskal 算法总体的复杂度是 O(ElogE + E * logE + E + E) = O(ElogE)。
*   - Kruskal 算法确实不如 Prim 算法快，但是因为它思路简单易实现，所以对于规模不大的图比较友好。
* 
* - 算法本质：在最小生成树算法中，Kruskal 算法是典型的贪心算法（排序后每次取最小的不能形成环的边）；
*   哈夫曼树的构建也是典型的贪心算法（每次选择当前频次最低的两棵子树合并构成新的树）。
*
* - 注：动态规划 vs. 贪心算法：两者的区别还是很明显的。贪心是使用一个单一的方法前进，而动态规划则是尝试所有的可能，只不过中
*   间使用“表格”进行记忆（所以要满足重叠子问题和最优子结构的性质）。背包问题是最好的例子。每次都放剩下的物品中单位价值最高的
*   物品，这种策略是贪心（单一的策略前进）；而动态规划其实每次尝试了拿去所有的物品。
* */

import MinimumSpanningTree.AuxiliaryDataStructure.MinHeap;
import MinimumSpanningTree.AuxiliaryDataStructure.UnionFind;
import MinimumSpanningTree.WeightedGraphReader.WeightedGraphReader;

import java.util.ArrayList;
import java.util.List;

import static Utils.Helpers.log;

public class KruskalMST<Weight extends Number & Comparable> {
    private WeightedGraph graph;
    private List<Edge<Weight>> mst;
    private Number minWeight;

    public KruskalMST(WeightedGraph graph) {
        this.graph = graph;
        mst = new ArrayList<>();
        minWeight = 0;

        kruskal();
    }

    private void kruskal() {
        MinHeap<Edge<Weight>> heap = new MinHeap<>(graph.getEdgeCount());

        // 将所有边加入堆中排序
        for (int v = 0; v < graph.getVertexCount(); v++) {
            Iterable<Edge<Weight>> it = graph.getAdjacentEdges(v);  // 拿到每个顶点的所有邻边
            for (Edge<Weight> e : it) {
                boolean edgeNotInHeap = e.theOther(v) > e.v();
                if (edgeNotInHeap)  // 因为顶点是从小到大进行遍历的，因此如果该边上的另一个节点 > 当前节点，说明另一个节点还没有被遍历到，即该边还没有被插入堆中
                    heap.insert(e);
            }
        }

        // 从小到大检测每条边是否会让最小生成树形成环，是则丢弃，否则放入 mst
        UnionFind uf = new UnionFind(heap.getSize());
        while (!heap.isEmpty() && mst.size() < graph.getVertexCount() - 1) {  // 一棵 mst 中最多有顶点数-1条边
            Edge<Weight> edge = heap.extractMin();
            if (!uf.isConnencted(edge.v(), edge.w())) {  // 判断是否成环
                mst.add(edge);
                uf.union(edge.v(), edge.w());
            }
        }

        // 计算 mst 的权值之和
        for (int i = 0; i < mst.size(); i++)
            minWeight = minWeight.doubleValue() + mst.get(i).weight().doubleValue();
    }

    public List<Edge<Weight>> mstEdges() { return mst; }

    public Number weight() { return minWeight; }

    public static void main(String[] args) {
        WeightedGraph<Double> g = new WeightedGraphReader()
                .read("src/main/java/MinimumSpanningTree/WeightedGraphReader/testG1.txt")
                .build(WeightedSparseGraph.class, false);
        log(g);

        KruskalMST<Double> mst = new KruskalMST<>(g);
        log(mst.mstEdges());  // 结果应该与 https://coding.imooc.com/lesson/71.html#mid=1489（8'41''）中的红色边一致
        log(mst.weight());
    }
}
