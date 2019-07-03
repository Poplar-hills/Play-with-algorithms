package Graph;

import Graph.GraphReader.GraphReader;

import java.util.*;

import static Utils.Helpers.log;

/*
* 寻路（Path）
*
* - 通过深度优先遍历（Depth First Search, DFS）找到两点之间的路径（不一定是最短的）
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
        for (int i = 0; i < n; i++) {  // 初始化 visited、from 数组
            visited[i] = false;
            from[i] = -1;
        }

        dfs(source);  // 对 source 进行寻路，记录在 from 数组中
    }

    private void dfs(int v) {
        if (v < 0 || v >= graph.getVertexCount())
            throw new IllegalArgumentException("dfs failed. Vertex out of boundary.");

        visited[v] = true;
        for (int w : graph.getAdjacentEdges(v)) {
            if (!visited[w]) {
                from[w] = v;          // 记录上一跳顶点
                dfs(w);  // 递归
            }
        }
    }

    public boolean hasPath(int target) {  // 检查从顶点 source 到顶点 target 之间是否有路径（和 isConnected 一个意思）
        if (target < 0 || target >= graph.getVertexCount())
            throw new IllegalArgumentException("hasPath failed. Vertex out of boundary.");
        return visited[target];           // 如果在寻路过程中访问过该顶点，则说明在一个连通分量上，即有路径到达
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

    public List<List<Integer>> allPaths(int target) {   // 找到两顶点之间的所有路径
        if (target < 0 || target >= graph.getVertexCount())
            throw new IllegalArgumentException("path failed. Vertex out of boundary.");

        List<List<Integer>> res = new ArrayList<>();
        Queue<List<Integer>> q = new LinkedList<>();    // 队列存储所有从 source 出发的路径，每个分支都是一条新路径

        List<Integer> initialPath = new ArrayList<>();  // 生成初始路径，放入 source 顶点
        initialPath.add(source);
        q.offer(initialPath);

        while (!q.isEmpty()) {
            List<Integer> path = q.poll();               // 每次拿出一条路径
            int lastVertex = path.get(path.size() - 1);  // 获取路径中的最后一个顶点
            if (lastVertex == target) {                  // 若该顶点就是 target 顶点则说明该是一条有效路径，放入 res 中；而那些走不到
                res.add(path);                           // target 上的路径（比如成环的路径）会被丢弃（poll 出来后不会再 offer 进去）
                continue;
            }
            for (int adj : graph.getAdjacentEdges(lastVertex)) {  // 获取所有相邻顶点
                if (path.contains(adj)) continue;                 // 若顶点已存在于该路径中，则说明已经访问过，不再继续（否则会成环）
                List<Integer> newPath = new ArrayList<>(path);    // 复制该路径并 add 这个 adj 顶点，形成一条新路径，放入 q 中
                newPath.add(adj);
                q.offer(newPath);
            }
        }

        return res;
    }

    public static void main(String[] args) {
        Graph g = new GraphReader()
                .read("src/main/java/Graph/GraphReader/testG2.txt")
                .build(SparseGraph.class, false);
        log(g);

        Path p = new Path(g, 4);
        log("A path from 4 to 5: " + p.path(5));
        log("A path from 4 to 2: " + p.path(2));
        log("A path from 4 to 3: " + p.path(3));
        log("All paths from 4 to 3: " + p.allPaths(3));
    }
}
