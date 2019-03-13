package MinimumSpanningTree.PerformanceTest;

import MinimumSpanningTree.*;
import MinimumSpanningTree.WeightedGraphReader.WeightedGraphReader;

/*
* 复杂度：
*   - Lazy Prim 算法为 O(ElogE)
*   - Prim 算法为 O(ElogV)
*   - Kruskal 算法为 O(ElogE)
*
* 测试结果：对于10000个顶点、60000条边的图，Prim 算法实际效率要比 Lazy Prim 和 Kruskal 快两倍左右。
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
