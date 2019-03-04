package MinimumSpanningTree;

import java.util.ArrayList;
import java.util.List;

import static Utils.Helpers.log;

public class DenseGraph<Weight extends Number & Comparable> implements WeightedGraph {  // 注意这里的接口
    private int n, m;
    private boolean directed;
    private Edge<Weight>[][] graph;

    public DenseGraph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        m = 0;
        graph = new Edge[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                graph[i][j] = null;  // 每个边初始化为 null
        }
    }

    /*
     * 增操作
     * */
    @Override
    public void addEdge(int v, int w, Number weight) {
        if (v < 0 || v >= n || w < 0 || w >= n)
            throw new IllegalArgumentException("addEdge failed. Vertex index is out of boundary");

        graph[v][w] = new Edge(v, w, weight);  // 如果本没边，则新建边；如果已有边，则替换（在其他实现中可以采用别的策略进行替换，例如找到两个边中更大/小的那个进行替换）

        if (v != w && !directed)
            graph[w][v] = new Edge(w, v, weight);

        m++;
    }

    /*
     * 查操作
     * */
    @Override
    public boolean hasEdge(int v, int w) {
        if (v < 0 || v >= n || w < 0 || w >= n)
            throw new IllegalArgumentException("hasEdge failed. Vertex index is out of boundary");
        return graph[v][w] != null;
    }

    @Override
    public Iterable<Edge<Weight>> getAdjacentVertexes(int v) {
        if (v < 0 || v >= n)
            throw new IllegalArgumentException("getAdjacentVertexes failed. Vertex index is out of boundary");

        List<Edge<Weight>> edges = new ArrayList<>();
        for (int i = 0; i < n; i++)
            edges.add(graph[v][i]);
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
            for (int j = 0; j < n; j++) {
                Edge edge = graph[i][j];
                s.append((edge != null ? edge.weight() : "N") + "  ");
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        WeightedGraph graph = new DenseGraph(4, false);
        graph.addEdge(1, 2, 8);
        graph.addEdge(2, 3, 7);
        graph.addEdge(2, 3, 9);  // 替换已存在的边
        log(graph);
    }
}
