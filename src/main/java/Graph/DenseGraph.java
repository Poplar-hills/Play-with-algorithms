package Graph;

import static Utils.Helpers.*;

/*
* Dense graph implemented by adjacency matrix（使用邻接矩阵实现稠密图）
* */

public class DenseGraph {
    private int n, m;  // n 为节点数，m 为边数
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
    public void addEdge(int i, int j) {  // 在顶点 i 和 j 之间建立一条边
        if (i < 0 || i >= n || j < 0 || j >= m)
            throw new IllegalArgumentException("addEdge failed. Vertex index is out of boundary");

        if (hasEdge(i, j))
            return;

        graph[i][j] = true;
        if (!directed)
            graph[j][i] = true;
        m++;
    }

    /*
     * 查操作
     * */
    public boolean hasEdge(int i, int j) {
        if (i < 0 || i >= n || j < 0 || j >= m)
            throw new IllegalArgumentException("hasEdge failed. Vertex index is out of boundary");
        return graph[i][j];
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
            for (int j = 0; j < n; j++)
                s.append(graph[i][j]);
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        DenseGraph g = new DenseGraph(4, false);
        log(g);
    }
}
