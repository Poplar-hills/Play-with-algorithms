package ShortestPath;

import MinimumSpanningTree.Edge;
import MinimumSpanningTree.WeightedGraph;
import MinimumSpanningTree.WeightedGraphReader.WeightedGraphReader;
import MinimumSpanningTree.WeightedSparseGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static Utils.Helpers.log;

/*
* Bellman-ford 单源最短路径算法（Bellman-ford Single Source Shortest Path）
*
* - 概要
*   - 可以为含有负权边（negative weight edge）的图计算最短路径（Dijkstra 算法则不行）
*   - 相应的代价是复杂度是 O(EV)，大大高于 Dijkstra 算法的 O(ElogV)。
*
* - 前提：
*   - 计算含有负权边的图的最短路径仍然依赖于松弛操作（relaxation）。
*   - 负权环：
*     - 如果一幅图中包含负权环（negative weight cycle），则该图中不存在最短路径，因为在环中每转一圈得到的总距离就越小。
*     - 例：3个顶点形成的负权环：0->1: 5, 1->2: -4, 2->0: -3；2个顶点形成的负权环：1->2: -4, 2->1: 1
*     - Bellman-ford 算法允许图中含有负权边，但不允许有负权环。
*
* - 算法原理 & 复杂度：
*   - 在 Dijkstra 算法中，因为前提是图中没有负权边，因此当找到某个顶点的所有邻边中最短的那条时就可以认定该边一定在最短路径树上。
*     而当图中存在负权边时，该假设就不再成立，因为经过更多节点的路径可能总距离反而更短，因此需要检查是否存在这种路径。
*   - 检查的办法就是反复对图中的每条边进行松弛操作，使得起始顶点到每个顶点的距离逐步逼近其最短距离。
*               --- g ----
*             / d ---- e  \
*            f  |   c  |  h    图中从 a 到 b 有4条不同的路径，反复对边 a->b 进行松弛以检查哪条路径的距离最短。
*             \ | /   \| /
*               a ---- b
*   - 在一幅图中，从起始顶点到某一顶点最多会经过 V 个顶点（即经过所有顶点）、最多有 V-1 条边。对所有边进行一次松弛操作能够确定
*     最小路径树上的一段路径，因为最小路径树最多有 V-1 段，因此需要对所有边进行 V-1 轮松弛操作即可找到完整的最小路径树。
*   - 对所有边条边进行一次松弛操作的复杂度是 O(E)，要进行 V-1 轮这样的操作，因此整体复杂度是 O(VE)。
*   - 算法过程演示 SEE: https://www.youtube.com/watch?v=obWXjtg0L64&vl=en（0'35''）
*
* - 实现：
*   - 因为 Bellman-ford 算法是通过遍历来逐步趋近每个顶点的最短距离，不再需要对边比较，因此也就不需要使用索引堆作为辅助数据结构了。
* */

public class BellmanFord<Weight extends Number & Comparable<Weight>> {
    private WeightedGraph graph;
    private int source;
    private Weight[] distances;
    private Edge<Weight>[] spt;

    public BellmanFord(WeightedGraph graph, int source) {
        this.graph = graph;
        this.source = source;
        int n = graph.getVertexCount();
        distances = (Weight[]) new Number[n];
        spt = new Edge[n];  // 这里可以直接实例化 Edge 数组（？？？？）

        for (int i = 0; i < distances.length; i++)
            distances[i] = null;  // 初始化时所有的顶点都不可达

        bellmanFord();
    }

    private void bellmanFord() {
        distances[source] = (Weight)(Number) 0;

        for (int i = 0; i < graph.getVertexCount() - 1; i++) {  // 一共迭代 V-1 次
            for (int v = 0; v < graph.getVertexCount(); v++) {  // 每次迭代遍历所有顶点
                Iterable<Edge<Weight>> it = graph.getAdjacentEdges(v);
                for (Edge<Weight> e : it)  // 对每个顶点的每条边进行松弛
                    relax(v, e);
            }
        }
    }

    private void relax(int v, Edge<Weight> e) {
        int w = e.theOther(v);
        if (distances[v] != null) {  // 如果该边的源节点还没被访问过则直接跳过（SEE：上面过程演示链接中的说明）
            Number relaxedDistance = distances[v].doubleValue() + e.weight().doubleValue();
            if (distances[w] == null || distances[w].compareTo((Weight) relaxedDistance) > 0) {
                distances[w] = (Weight) relaxedDistance;  // 更新顶点的最短距离
                spt[w] = e;  // 更新最短路径树中的对应路径
            }
        }
    }

    public Weight[] distances() { return distances; }

    public Weight distanceTo(int w) { return distances[w]; }

    public List<Edge<Weight>> shortestPathTree() {
        List<Edge<Weight>> r = new ArrayList<>();
        for (int i = 0; i < spt.length; i++)
            if (spt[i] != null)  // 因为 spt 中起始顶点位置上的值是 null，因此要去掉
                r.add(spt[i]);
        return r;
    }

    public List<Edge<Weight>> shortestPathTo(int w) {  // 与 Dijkstra 的同名方法略有不同，因为这里的 spt 是数组而非列表
        Stack<Edge<Weight>> stack = new Stack<>();
        List<Edge<Weight>> path = new ArrayList<>();

        // 读懂这段逻辑这个方法就理解了
        Edge<Weight> e = spt[w];
        while (e.v() != source) {
            stack.push(e);
            e = spt[e.v()];
        }
        stack.push(e);  // 若 source=0、w=1，则不加这句的话最后返回的就是空列表了

        while (!stack.empty())
            path.add(stack.pop());

        return path;
    }

    public static void main(String[] args) {
        WeightedGraph<Double> g = new WeightedGraphReader()
                .read("src/main/java/ShortestPath/TestData/testG2.txt")  // 可以尝试使用 testG3.txt 测试
                .build(WeightedSparseGraph.class, true);

        BellmanFord<Double> b = new BellmanFord<>(g, 0);  // 如果 source 改成2的话则只存在到达部分顶点的路径（2到不了0），因此没有最短路径树

        log(b.distances());
        log(b.shortestPathTree());
        log(b.shortestPathTo(1));
        log(b.shortestPathTo(2));
        log(b.shortestPathTo(3));
        log(b.shortestPathTo(4));
    }
}
