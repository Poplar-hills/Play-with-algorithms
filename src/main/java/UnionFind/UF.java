package UnionFind;

public interface UF {
    boolean isConnected(int p, int q);
    void union(int p, int q);
    int getSize();
}
