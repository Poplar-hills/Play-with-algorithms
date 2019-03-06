package Graph;

import Graph.GraphReader.GraphReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static Utils.Helpers.log;

/*
* 寻路（Path）
*
* - 通过深度优先遍历（Depth First Search, DFS）找到两点之间的路径
*
* - 例：对于 testG2.txt 中描述的 graph：
*
*             0 ----- 1 ------- 4       0 | 1  2  5
*           / \     / \        /        1 | 0  2  3  4
*         /    \  /    \     /          2 | 0  1
*       /       2       \  /            3 | 1  4  5
*      5 --------------- 3              4 | 1  3
*                                       5 | 0  3
*
*   如果 source 是4，则经过 new Path(graph, 4) 之后：
*       vertex:    0  1  2  3  4  5
*       from:    [ 1  4  0  5 -1  0 ]
* */

public class Path {
    private Graph graph;
    private boolean[] visited;
    private int[] from;  // 记录每个顶点在路径中的上一跳顶点
    private int source;  // 源顶点

    public Path(Graph graph, int source) {
        if (source < 0 || source >= graph.getVertexCount())
            throw new IllegalArgumentException("Failed to initiate. The source is out of the boundary of the graph");

        this.graph = graph;
        this.source = source;

        int n = graph.getVertexCount();
        visited = new boolean[n];
        from = new int[n];
        for (int i = 0; i < n; i++) {
            visited[i] = false;
            from[i] = -1;
        }

        depthFirstSearch(source);  // 对 source 进行寻路，记录在 from 数组中
    }

    private void depthFirstSearch(int v) {  // DFS
        if (v < 0 || v >= graph.getVertexCount())
            throw new IllegalArgumentException("depthFirstSearch failed. Vertex out of boundary.");

        visited[v] = true;
        for (int w : graph.getAdjacentVertexes(v)) {
            if (!visited[w]) {
                from[w] = v;  // 记录上一跳顶点
                depthFirstSearch(w);  // 递归
            }
        }
    }

    public boolean hasPath(int target) {  // 检查从顶点 source 到顶点 target 之间是否有路径（和 isConnected 一个意思）
        if (target < 0 || target >= graph.getVertexCount())
            throw new IllegalArgumentException("hasPath failed. Vertex out of boundary.");
        return visited[target];  // 如果在寻路过程中访问过该顶点，则说明在一个连通分量上，即有路径到达
    }

    public List<Integer> path(int target) {  // 借助 from 数组查询 source 到 target 的路径（不一定是最短的）
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
        Graph g = new GraphReader()
                .read("src/main/java/Graph/GraphReader/testG2.txt")
                .build(SparseGraph.class, false);
        log(g);

        Path p = new Path(g, 4);
        log(p.path(5));  // 输出从 4 到 5 的 path
        log(p.path(2));  // 输出从 4 到 2 的 path（不是最短路径）
        log(p.path(3));  // 输出从 4 到 3 的 path（不是最短路径）
    }
}
