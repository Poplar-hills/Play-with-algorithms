package Graph;

public interface Graph {
    void addEdge(int v, int w);
    boolean hasEdge(int v, int w);
    Iterable<Integer> getAdjacentEdges(int v);
    int getVertexCount();
    int getEdgeCount();
}