package MinimumSpanningTree;

import MinimumSpanningTree.WeightedGraphReader.WeightedGraphReader;

import java.util.ArrayList;
import java.util.List;

import static Utils.Helpers.log;

/*
* Lazy Prim 算法
*
* - 用途：寻找最小生成树
*
* - 过程：
*   动画演示 SEE: https://coding.imooc.com/lesson/71.html#mid=1489（0'33''）
*   1. 从一个顶点开始切分（切分内侧只有一个顶点）
*   2. 找到该顶点的所有横切边，并插入最小堆中进行比较（注意：切分内侧顶点之间的边不是横切边）
*   3. 取堆中权值最小的横切边加入最小生成树中（注意：此时堆中权值最小的边不一定是横切边，需要判断）
*   4. 将该最小边上的另一个顶点加入切分内侧
*   5. 回到步骤2进行循环
*
* - 时间复杂度：
*   lazyPrim 方法中的 while 循环的终止条件是堆为空，而算法过程中又会将所有的边都插入堆中，因此 while 循环的
*   执行次数就是图中边数 E。而循环内部：
*     1. heap.extractMin()：因为 heap 中有 E 条边，因此一次 extractMin 是 O(logE) 级别，E 次就是 O(ElogE)。
*     2. visit()：一次 visit 会遍历一个顶点的所有邻边，E 次 visit 虽然会重复遍历一些边，但也是 E 级别的（若是邻
*        接矩阵则是 V^2）。每次 visit 内部要调一次 insert，与 extractMin 同理，每次 insert 也是 O(logE)，所以
*        总体也是 O(ElogE)。
*   因此 lazyPrim 总体就是 O(ElogE + ElogE) = O(ElogE)。
* */

public class LazyPrimMST<Weight extends Number & Comparable> {
    private WeightedGraph graph;
    private boolean[] visited;           // 用于标记节点 i 是否被访问过
    private List<Edge<Weight>> mst;      // 最小生成树所包含的所有边
    private MinHeap<Edge<Weight>> heap;  // 用于比较边的最小堆（辅助数据结构）
    private Number minWeight;            // 最小生成树的权值之和（注意：是 Number 类型，Number 是一个抽象类）

    public LazyPrimMST(WeightedGraph graph) {
        this.graph = graph;
        int n = graph.getVertexCount();
        visited = new boolean[n];         // boolean 数组初值都为 false
        mst = new ArrayList<>();
        heap = new MinHeap<>();

        lazyPrim();  // 开始计算最小生成树
    }

    private void lazyPrim() {
        visit(0);  // 步骤1：从一个顶点开始切分，将其加入切分内侧进行访问

        while (!heap.isEmpty()) {
            // 步骤3：取堆中权值最小的横切边加入最小生成树中
            Edge<Weight> e = heap.extractMin();
            if (visited[e.v()] && visited[e.w()])  // 如果一条横切边的两个端点都被 visit 过了，说明它不再是横切边了
                continue;
            mst.add(e);
            // 步骤4：将最小边上的另一个顶点加入切分内侧进行访问
            visit(visited[e.v()] ? e.w() : e.v());
        }

        // 计算最小生成树的权值
        minWeight = mst.get(0).weight().doubleValue();  // 初值
        for (int i = 1; i < mst.size(); i++)
            minWeight = minWeight.doubleValue() + mst.get(i).weight().doubleValue();
    }

    private void visit(int v) {
        if (visited[v])
            throw new IllegalArgumentException("visit failed. Vertex has already been visited.");

        visited[v] = true;

        // 步骤2：找到该顶点的所有横切边，并插入最小堆中进行比较（注意切分内侧顶点之间的边不是横切边）
        Iterable<Edge<Weight>> it = graph.getAdjacentVertexes(v);
        for (Edge<Weight> e : it)
            if (!visited[e.theOther(v)])  // 若另一个顶点没有被 visit 则说明该边是横切边，应加入堆中比较
                heap.insert(e);
    }

    public List<Edge<Weight>> mstEdges() { return mst; }

    public Number weight() { return minWeight; }

    public static void main(String[] args) {
        // 下面用到的 testG1.txt 中的图就是 https://coding.imooc.com/lesson/71.html#mid=1489（1'24''）中的图
        WeightedGraph<Double> g = new WeightedGraphReader()
                .read("src/main/java/MinimumSpanningTree/WeightedGraphReader/testG1.txt")
                .build(WeightedSparseGraph.class, false);
        log(g);

        LazyPrimMST<Double> mst = new LazyPrimMST<>(g);
        log(mst.mstEdges());  // 结果应该与 https://coding.imooc.com/lesson/71.html#mid=1489（8'41''）中的红色边一致
        log(mst.weight());
    }
}
