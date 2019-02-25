package Graph;

/*
* Sparse graph implemented by adjacency list（使用邻接表实现稀疏图）
*
* - 处理平行边：
*   - 在 hasEdge 中使用了 contains 来寻找元素，而 contains 底层会进行遍历，因此使得 hasEdge 是 O(n) 的复杂度，从而也使得 addEdge
*     也是 O(n) 的复杂度。
*   - 因为处理平行边的效率是邻接表的一个劣势，所以常见的邻接表的实现中会允许存在平行边，而在所有边都添加完之后再统一去除平行边。
* */

import com.sun.tools.javac.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static Utils.Helpers.log;

public class SparseGraph implements Graph {
    private int n, m;  // n 为节点数，m 为边数
    private boolean directed;  // 该图是否为有向图
    private ArrayList<Integer>[] graph;  // 图的结构是 ArrayList 数组，不同于 DenseGraph 中的二维数组，SparseGraph 中的内层需要 ArrayList 的动态扩容功能来提高空间使用效率。

    public SparseGraph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        m = 0;
        graph = new ArrayList[n];
        for (int i = 0; i < n; i++)
            graph[i] = new ArrayList<>();
    }

    /*
     * 增操作
     * */
    public void addEdge(int v, int w) {  // 在顶点 v 和 w 之间建立一条边
        if (v < 0 || v >= n || w < 0 || w >= n)
            throw new IllegalArgumentException("hasEdge failed. Vertex index is out of boundary");

        if (hasEdge(v, w))  // 该实现中不允许平行边
            return;

        graph[v].add(w);
        if (v != w && !directed)  // 该实现中允许自环边，但不能重复添加自环边
            graph[w].add(v);
        m++;
    }

    /*
     * 查操作
     * */
    public boolean hasEdge(int v, int w) {
        if (v < 0 || v >= n || w < 0 || w >= n)
            throw new IllegalArgumentException("hasEdge failed. Vertex index is out of boundary");
        return graph[v].contains(w);  // contains 会遍历列表，因此是 O(n) 复杂度
    }

    // 读取一个顶点的所有邻边
    // ∵ 不能暴露 graph 给外界 ∴ 使用迭代器模式，返回一个访问某一顶点的边的迭代器（或者不使用迭代器，直接返回所有相邻接点也可以）
    // 在 java 中返回迭代器和返回所有相邻接点是一样的（都是返回 graph[v]，只是返回值类型不同）
    public Iterable<Integer> adjIterator(int v) {
        if (v < 0 || v >= n)
            throw new IllegalArgumentException("adjIterator failed. Vertex index is out of boundary");
        return graph[v];  // ArrayList 实现了 List，List 继承了 Collection，Collection 继承了 Iterable
    }

    public int getVertexCount() { return n; }

    public int getEdgeCount() { return m; }

    /*
     * Misc
     * */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < n; i++) {
            s.append(i + " | ");
            s.append(graph[i].stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(" ")));
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        // 测试 addEdge
        SparseGraph g = new SparseGraph(4, false);  // 无向图
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(3, 1);
        log(g);

        // 测试 hasEdge
        log(g.hasEdge(3, 0));
        log(g.hasEdge(3, 1));

        // 测试 adjIterator
        for (int n : g.adjIterator(2))
            log(n);
    }
}
