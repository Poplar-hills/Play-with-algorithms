package Graph;

import Graph.GraphReader.GraphReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import static Utils.Helpers.log;

/*
* 寻路（Path）
*
* */

public class Path {
    private Graph graph;
    private boolean[] visited;
    private int[] from;  // 每个顶点在路径中的上一跳顶点
    private int source;  // 源顶点

    public Path(Graph graph, int source) {
        if (source >= graph.getVertexCount())
            throw new IllegalArgumentException("Failed to initiate Path. The source is out of the boundary of the graph");

        this.graph = graph;
        this.source = source;

        int n = graph.getVertexCount();
        visited = new boolean[n];
        from = new int[n];
        for (int i = 0; i < n; i++) {
            visited[i] = false;
            from[i] = -1;
        }

        // 寻路
        depthFirstSearch(source);
    }

    private void depthFirstSearch(int v) {
        if (v < 0 || v >= graph.getVertexCount())
            throw new IllegalArgumentException("depthFirstSearch failed. Vertex out of boundary.");

        visited[v] = true;
        Iterator<Integer> it = graph.adjIterator(v).iterator();

        while (it.hasNext()) {
            int nextV = it.next();
            if (!visited[nextV]) {
                from[nextV] = v;
                depthFirstSearch(nextV);
            }
        }
    }

    public boolean hasPath(int target) {  // 检查从顶点 source 到顶点 target 之间是否有路径（和 isConnected 一个意思）
        if (target < 0 || target >= graph.getVertexCount())
            throw new IllegalArgumentException("hasPath failed. Vertex out of boundary.");
        return visited[target];  // 如果在寻路过程中访问过该顶点，则说明在一个连通分量上，即有路径到达
    }

    public List<Integer> path(int target) {
        if (target < 0 || target >= graph.getVertexCount())
            throw new IllegalArgumentException("path failed. Vertex out of boundary.");

        Stack<Integer> stack = new Stack<>();  // 使用栈记录从 target -> source 中的每一个节点
        int v = target;
        while (v != -1) {  // source 的 from 是 -1，到此结束循环
            stack.add(v);
            v = from[v];
        }

        List<Integer> list = new ArrayList<>();  // 再将栈中的元素出栈给列表，获得从 source -> target 的路径（相当于借用 stack 进行了一次 reverse 操作）
        while (!stack.isEmpty())
            list.add(stack.pop());
        return list;
    }

    public static void main(String[] args) {
        Graph g = new SparseGraph(6, false);
        new GraphReader(g, "src/main/java/Graph/GraphReader/testG2.txt");
        Path p = new Path(g, 4);

        log(g);
        log(p.path(5));  // 输出从 4 到 5 的 path
        log(p.path(2));  // 输出从 4 到 2 的 path（不是最短路径）
        log(p.path(3));  // 输出从 4 到 3 的 path（不是最短路径）
    }
}
