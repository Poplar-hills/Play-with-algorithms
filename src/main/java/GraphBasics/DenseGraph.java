package GraphBasics;

import java.util.ArrayList;

import static Utils.Helpers.*;

/*
* 使用邻接矩阵实现稠密图（Dense graph implemented by adjacency matrix）
* */

public class DenseGraph implements Graph {
    private int n, m;  // n 为顶点数，m 为边数
    private boolean directed;  // 该图是否为有向图
    private boolean[][] graph;  // 图的结构是二维布尔数组，graph[i][j] 表示顶点 i 与顶点 j 是否相连

    public DenseGraph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        m = 0;                      // 边数初始化为0
        graph = new boolean[n][n];  // 初始化 n * n 的矩阵，
        for (int i = 0; i < n; i++)
            graph[i] = new boolean[n];  // 初始矩阵内没有相连接的顶点（默认都为 false）
    }

    /*
     * 增操作
     * */
    @Override
    public void addEdge(int v, int w) {  // 在顶点 v 和 w 之间建立一条边
        if (v < 0 || v >= n || w < 0 || w >= n)
            throw new IllegalArgumentException("addEdge failed. Vertex index is out of boundary");

        if (hasEdge(v, w))  // 两点之间是否已存在边（该实现中不允许平行边）
            return;

        graph[v][w] = true;
        if (!directed)
            graph[w][v] = true;
        m++;
    }

    /*
     * 查操作
     * */
    @Override
    public boolean hasEdge(int v, int w) {
        if (v < 0 || v >= n || w < 0 || w >= n)
            throw new IllegalArgumentException("hasEdge failed. Vertex index is out of boundary");
        return graph[v][w];
    }

    @Override
    public Iterable<Integer> getAdjacentEdges(int v) {  // 读取一个顶点的所有邻边（∵ 不能暴露 graph 给外界 ∴ 使用迭代器模式，返回一个访问某一顶点的边的迭代器）
        if (v < 0 || v >= n)
            throw new IllegalArgumentException("getAdjacentEdges failed. Vertex index is out of boundary");

        ArrayList<Integer> edges = new ArrayList<>();
        for (int i = 0; i < n; i++)
            if (graph[v][i])  // 如果为 true 则添加索引 i 进 edges
                edges.add(i);
        return edges;
    }

    @Override
    public int getVertexCount() { return n; }

    @Override
    public int getEdgeCount() { return m; }

    /*
     * Misc
     * */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("    ");
        for (int i = 0; i < n; i++)
            s.append(i + "  ");
        s.append("\n  +");
        for (int i = 0; i < n; i++)
            s.append("---");
        s.append("\n");
        for (int i = 0; i < n; i++) {
            s.append(i + " | ");
            for (int j = 0; j < n; j++)
                s.append((graph[i][j] ? 1 : 0) + "  ");
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        // 测试 addEdge
        DenseGraph g = new DenseGraph(4, false);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(3, 1);
        log(g);

        // 测试 hasEdge
        log(g.hasEdge(3, 0));
        log(g.hasEdge(3, 1));

        // 测试 getAdjacentEdges
        for (int n : g.getAdjacentEdges(2))
            log(n);
    }
}
