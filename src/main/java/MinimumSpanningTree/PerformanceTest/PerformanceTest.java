package MinimumSpanningTree.PerformanceTest;

import MinimumSpanningTree.*;
import MinimumSpanningTree.WeightedGraphReader.WeightedGraphReader;

/*
* 分析：对于10000个顶点、60000条边的图：
*   - Lazy Prim 算法的复杂度为 O(ElogE)
*   - Prim 算法的复杂度为 O(ElogV)，实际效率是 Lazy Prim 的两倍
*   - Kruskal 算法的复杂度为 ？？？？？？？？？
* */

public class PerformanceTest {
    private static void timeIt(Runnable fn) {
        double startTime = System.nanoTime();
        fn.run();
        double endTime = System.nanoTime();
        System.out.println((endTime - startTime) / 1000000000.0);
    }

    public static void main(String[] args) {
        WeightedGraph<Double> g = new WeightedGraphReader()
                .read("src/main/java/MinimumSpanningTree/PerformanceTest/testG1.txt")
                .build(WeightedSparseGraph.class, false);

        System.out.println(String.format("The graph contains %d vertexes and %d edges", g.getVertexCount(), g.getEdgeCount()));
        timeIt(() -> { LazyPrimMST<Double> mst = new LazyPrimMST<>(g); });
        timeIt(() -> { PrimMST<Double> mst = new PrimMST<>(g); });
        timeIt(() -> { KruskalMST<Double> mst = new KruskalMST<>(g); });
    }
}
