package ShortestPath;

import MinimumSpanningTree.AuxiliaryDataStructure.IndexMinHeap;
import MinimumSpanningTree.Edge;
import MinimumSpanningTree.WeightedGraph;
import MinimumSpanningTree.WeightedGraphReader.WeightedGraphReader;
import MinimumSpanningTree.WeightedSparseGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static Utils.Helpers.log;

/*
* Dijkstra 单源最短路径算法（Dijkstra Single Source Shortest Path）
*
* - 概要
*   - Dijkstra 算法是应用最广、名气最大的针对有权图的最短路径算法。
*   - 用途：用于计算一幅带权图的最短路径树（即从起始顶点到图中所有其他顶点的最短路径）。
*   - 局限性：图中不能有负权边（有负权边的应用场景本来也很少）
*   - 时间复杂度：O(ElogV)
*
* - 运算过程动画演示 SEE：https://coding.imooc.com/lesson/71.html#mid=1495（3'30''）
*   看完演示中的例子后再来思考，若将 2->3 和 3->4 的权值分别改为 1.01 和 0.09，则结果是怎样的？（用手画来模拟运算过程最有效）
*
* - 运算过程：
*   1. 由起始顶点开始访问（顶点0）
*   2. 找到该顶点的所有邻边
*   3. 更新起始顶点到每条邻边上另一顶点的距离：
*        0  1  2  3  4           0  1  2  3  4
*        -------------    --->   -------------
*        0  -  -  -  -           0  5  2  6  -
*   4. 在未被访问的顶点（1、2、3、4）中，找到从起始顶点开始能以最短距离到达的那个顶点（顶点2），则到达该顶点的路径即在最短路
*      径树上（因为在不存在负权边的前提下，从起始顶点经过其他顶点再回到该顶点的距离一定更长）。
*   5. 对起始顶点的所有邻边进行 relax 操作，即检查经过顶点2到这些邻边上另一顶点的路径是否比之前记录的距离更短，是则更新：
*        0  1  2  3  4           0  1  2  3  4
*        -------------    --->   -------------
*        0  5  2  6  -           0  3  2  5  7
*   6. 至此完成一轮 Dijkstra 算法循环，跳到第4步继续循环（此时未被访问的顶点是1、3、4，距离最短的是路径是2->1，因此访问顶点1，
*      且路径2->1在最短路径树上）。
*
* - 松弛操作的理解：
*           1
*         /   \    该图中：0 可以之接达到 1，或者 0 绕经 2 之后也可以达到 1
*       0 ---- 2
*   对边 0->1 进行松弛操作就是检查从 0 绕经 2 到 1 的路径的距离是否比边 0->1 的距离更短。
*
* - 算法所需数据结构：
*   从运算过程中可知，该算法主要做两件事：
*     1. 从未被访问的顶点中找出到达距离最短的那个节点，即从一个数组中找最小值。
*     2. 在发现有更短距离的路径后需要更新对应顶点距离，即更新数组中的元素。
*   - 最小索引堆能同时满足这两个需求（类似 Prim 算法里的需求）。
*
* - 复杂度分析：
*   正是因为要使用最小索引堆：
*     - 因此只需要开辟顶点个数大小的空间；
*     - 每次插入、更新操作都是 O(logV) 的复杂度；
*     - 另外在算法过程中要对所有边进行遍历，因此使得算法的整体复杂度为 O(ElogV) 级别。
* */

public class Dijkstra<Weight extends Number & Comparable<Weight>> {
    private WeightedGraph graph;
    private int source;              // 起始顶点，即单源最短路径中的"源"
    private Weight[] distances;      // 记录起始顶点到其他所有顶点的最短距离
    private boolean[] visited;       // 记录每个顶点是否被访问过
    private List<Edge<Weight>> spt;  // 最短路径树
    private IndexMinHeap<Edge<Weight>> heap;  // 辅助数据结构

    public Dijkstra(WeightedGraph graph, int source) {
        this.graph = graph;
        this.source = source;
        int n = graph.getVertexCount();
        distances = (Weight[]) new Number[n];
        visited = new boolean[n];  // 默认值都是 false
        spt = new ArrayList<>();
        heap = new IndexMinHeap(graph.getVertexCount());

        for (int i = 0; i < distances.length; i++)
            distances[i] = null;  // 初始化时所有的顶点都不可达

        dijkstra();
    }

    private void dijkstra() {
        visit(source);
        distances[source] = (Weight)(Number) 0;

        while (!heap.isEmpty()) {
            Edge<Weight> minE = heap.extractMin();
            int minV = visited[minE.v()] ? minE.w() : minE.v();

            if (visited[minV])
                continue;

            spt.add(minE);  // 添加到最短路径树中

            Iterable<Edge<Weight>> it = graph.getAdjacentEdges(minV);
            for (Edge<Weight> e : it)  // 对顶点 minV 的每条邻边进行松弛操作
                relax(minV, e);

            visit(minV);
        }
    }

    private void relax(int v, Edge<Weight> e) {
        int w = e.theOther(v);
        Number relaxedDistance = distances[v].doubleValue() + e.weight().doubleValue();  // 计算松弛距离
        if (distances[w] == null || distances[w].compareTo((Weight) relaxedDistance) > 0)
            distances[w] = (Weight) relaxedDistance;  // 更新顶点的最短距离
    }

    private void visit(int v) {
        visited[v] = true;  // 访问顶点

        Iterable<Edge<Weight>> it = graph.getAdjacentEdges(v);
        for (Edge<Weight> e : it) {  // 遍历该顶点的邻边
            int w = e.theOther(v);

            if (distances[w] == null)
                distances[w] = e.weight();  // 更新起始顶点到邻边上另一顶点的距离（前提是之前没有值）

            if (heap.contains(w))  // 将邻边放入堆中进行比较
                heap.change(w, e);
            else
                heap.insert(w, e);
        }
    }

    public Weight[] distances() { return distances; }

    public Weight distanceTo(int w) { return distances[w]; }  // 寻找从 source 到 w 的最短路径

    public List<Edge<Weight>> shortestPathTree() { return spt; }

    public List<Edge<Weight>> shortestPathTo(int w) {  // 寻找从 source 到 w 的最短路径
        Stack<Edge<Weight>> stack = new Stack<>();     // 辅助数据结构，用于 reverse 输出列表中的元素
        List<Edge<Weight>> path = new ArrayList<>();

        int targetVertex = w;
        for (int i = spt.size() - 1; i >= 0; i--) {
            Edge<Weight> e = spt.get(i);
            if (e.w() == targetVertex) {
                targetVertex = e.v();
                stack.add(e);
            }
        }

        while (!stack.empty())
            path.add(stack.pop());

        return path;
    }

    public static void main(String[] args) {
        WeightedGraph<Double> g = new WeightedGraphReader()
            .read("src/main/java/ShortestPath/TestData/testG1.txt")
            .build(WeightedSparseGraph.class, true);  // Dijkstra 算法对于有向图或无向图同样有效

        Dijkstra<Double> d = new Dijkstra<>(g, 0);

        log(d.shortestPathTree());
        log(d.distances);
        log(d.shortestPathTo(1));
        log(d.shortestPathTo(2));
        log(d.shortestPathTo(3));
        log(d.shortestPathTo(4));
    }
}
