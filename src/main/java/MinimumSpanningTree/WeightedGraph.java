package MinimumSpanningTree;

public interface WeightedGraph<Weight extends Number & Comparable> {
    void addEdge(int v, int w, Weight weight);
    boolean hasEdge(int v, int w);
    Iterable<Edge<Weight>> getAdjacentVertexes(int v);
    int getVertexCount();
    int getEdgeCount();
}