package MinimumSpanningTree.PerformanceTest;

import MinimumSpanningTree.LazyPrimMST;
import MinimumSpanningTree.PrimMST;
import MinimumSpanningTree.WeightedGraph;
import MinimumSpanningTree.WeightedGraphReader.WeightedGraphReader;
import MinimumSpanningTree.WeightedSparseGraph;

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
    }
}
