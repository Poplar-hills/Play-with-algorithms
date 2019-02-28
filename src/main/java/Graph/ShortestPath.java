package Graph;

/*
* 最短路径（Shortest Path）
*
* - 通过广度优先遍历找到两点之间的最短路径
*
* - 例：对于 testG2.txt 中描述的 graph：
*
*             0 ----- 1 ------- 4       0 | 1  2  3
*           / \     / \        /        1 | 0  2  3  4
*         /    \  /    \     /          2 | 0  1
*       /       2       \  /            3 | 1  4  5
*      5 --------------- 3              4 | 1  3
*                                       5 | 0  3
*
*   如果 source 是4，则经过 new ShortestPath(graph, 4) 之后：
*       vertex:    0  1  2  3  4  5
*       from:    [ 1  4  1  4 -1  3 ]
*       orders:  [ 2  1  2  1  0  2 ]
* */

import Graph.GraphReader.GraphReader;

import java.util.*;

import static Utils.Helpers.log;

public class ShortestPath {
    private Graph graph;
    private boolean[] visited;
    private int[] from;
    private int source;
    private int[] orders;  // 记录从 source 到每一个节点的最短距离

    public ShortestPath(Graph graph, int source) {
        if (source < 0 || source >= graph.getVertexCount())
            throw new IllegalArgumentException("Failed to initiate. The source is out of the boundary of the graph");

        this.graph = graph;
        this.source = source;

        int n = graph.getVertexCount();
        visited = new boolean[n];
        from = new int[n];
        orders = new int[n];
        for (int i = 0; i < n; i++) {
            visited[i] = false;
            from[i] = -1;
            orders[i] = -1;
        }

        breadthFirstSearch();  // 对 source 进行寻路，记录在 from 和 orders 数组中
    }

    private void breadthFirstSearch() {
        Queue<Integer> queue = new LinkedList<>();  // 使用队列作为辅助数据结构（类似 BST 的 levelOrderTraverse）
        queue.add(source);
        visited[source] = true;
        orders[source] = 0;

        while (!queue.isEmpty()) {
            int v = queue.remove();
            for (int w : graph.adjIterator(v)) {
                if (!visited[w]) {
                    queue.add(w);
                    visited[w] = true;
                    from[w] = v;
                    orders[w] = orders[v] + 1;
                }
            }
        }
    }

    public int length(int target) {  // 借助 orders 数组查询从 source 到 target 的最短距离
        if (target < 0 || target >= graph.getVertexCount())
            throw new IllegalArgumentException("length failed. Vertex out of boundary.");
        return orders[target];
    }

    public boolean hasPath(int target) {  // 与 Path 中的相同
        if (target < 0 || target >= graph.getVertexCount())
            throw new IllegalArgumentException("hasPath failed. Vertex out of boundary.");
        return visited[target];
    }

    public List<Integer> path(int target) {  // 与 Path 中的相同
        if (target < 0 || target >= graph.getVertexCount())
            throw new IllegalArgumentException("path failed. Vertex out of boundary.");

        Stack<Integer> stack = new Stack<>();
        int v = target;
        while (from[v] != -1) {
            stack.add(v);
            v = from[v];
        }

        List<Integer> list = new ArrayList<>();
        while (!stack.isEmpty())
            list.add(stack.pop());

        return list;
    }

    public static void main(String[] args) {
        Graph g = new SparseGraph(6, false);
        new GraphReader(g, "src/main/java/Graph/GraphReader/testG2.txt");  // 将文件中的图信息读入 g 中

        ShortestPath p = new ShortestPath(g, 4);

        log(p.hasPath(5));
        log(p.path(5));
        log(p.length(5));

        log(p.hasPath(0));
        log(p.path(0));
        log(p.length(0));
    }
}
