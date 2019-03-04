package MinimumSpanTree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static Utils.Helpers.log;

public class SparseGraph<Weight extends Number & Comparable> implements WeightedGraph {
    private int n, m;
    private boolean directed;
    private List<Edge<Weight>>[] graph;

    public SparseGraph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        m = 0;
        graph = new ArrayList[n];
        for (int i = 0; i < n; i++)
            graph[i] = new ArrayList<>();
    }

    @Override
    public void addEdge(int v, int w, Number weight) {
        if (v < 0 || v >= n || w < 0 || w >= n)
            throw new IllegalArgumentException("addEdge failed. Vertex index is out of boundary");

        // 注意，由于在邻接表查找是否有平行边的效率较低，因此这里允许平行边的出现

        graph[v].add(new Edge(v, w, weight));
        if (v != w && !directed)
            graph[w].add(new Edge(w, v, weight));

        m++;
    }

    @Override
    public boolean hasEdge(int v, int w) {
        if (v < 0 || v >= n || w < 0 || w >= n)
            throw new IllegalArgumentException("hasEdge failed. Vertex index is out of boundary");
        return graph[v].contains(w);
    }

    @Override
    public Iterable<Edge> getAdjacentVertexes(int v) {
        return null;
    }

    @Override
    public int getVertexCount() {
        return 0;
    }

    @Override
    public int getEdgeCount() {
        return 0;
    }

    /*
     * Misc
     * */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < n; i++) {
            s.append(i + " | ");
            s.append(graph[i].stream()
                    .map(x -> String.format("{to: %d, w: %d}", x.w(), x.weight()))
                    .collect(Collectors.joining(" ")));
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        WeightedGraph graph = new SparseGraph(4, false);
        graph.addEdge(1, 2, 8);
        graph.addEdge(2, 3, 7);
        graph.addEdge(2, 3, 9);  // 稀疏图因为性能原因允许平行边
        log(graph);
    }
}
