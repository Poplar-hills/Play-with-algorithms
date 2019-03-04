package MinimumSpanningTree;

public class Edge<Weight extends Number & Comparable> implements Comparable<Edge> {
    private int a, b;  // 边上的两个顶点（对于有向图来说，该边是从 a 指向 b，对于无向图来说都一样）
    private Weight weight;  // 边的权值

    public Edge(int a, int b, Weight weight) {
        this.a = a;
        this.b = b;
        this.weight = weight;
    }

    public Edge() { }

    public int v() { return a; }

    public int w() { return b; }

    public Weight weight() { return weight; }

    public int theOther(int x) {  // 给定边上的一个顶点，返回另一个顶点
        if (x != a && x != b)
            throw new IllegalArgumentException("theOther failed. x is not a vertex on the edge");
        return x == a ? b : a;
    }

    @Override
    public int compareTo(Edge other) {
        int comp = weight.compareTo(other.weight());
        if (comp < 0) return -1;
        else if (comp > 0) return 1;
        else return 0;
    }
}
