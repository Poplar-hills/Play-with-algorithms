package Graph;

import Graph.GraphReader.GraphReader;

import static Utils.Helpers.log;

/*
* 连通分量（Connected Component）
*
* - 通过对图进行深度优先遍历（DFS）找到图中的连通分量
*
* - DFS 的复杂度分析：
*   - 如果是邻接表则为 O(V + E)，其中 E, V 分别是顶点数和边数
*   - 如果是邻接矩阵则为 O(V^2)
*   因为 DFS 的实现代码中是要遍历邻接表或邻接矩阵中的每一个节点（顶点 + 边），那么：
*         0 | 1
*         1 | 0  2  3      在邻接表中，节点数 = 顶点数 V + 边数 E，因此复杂度为 O(V + E)；
*         2 | 1  3         而由于 V 一定是 E 的低阶项，所以 O(V + E) 实际上相当于 O(E)。
*         3 | 1  2
*
*         0  1  2  3
*        +-----------
*      0 | 0  1  0  0      在邻接矩阵中，节点数 = 顶点数 V 的平方，因此复杂度为 O(V^2)；
*      1 | 1  0  1  1      而因为邻接矩阵表达的是稠密图（几乎每个顶点都与其它所有节点相连），因此边数
*      2 | 0  1  0  1      是 V^2 量级的，即 E = V^2，所以 O(V^2) 也可以写作 O(E)。
*      3 | 0  1  1  0
*
*   简单的说，在图论算法中，只要图要遍历一遍，复杂度都应该是 O(V + E) 的。这是因为我们不可能只顾边而不顾点得进行遍历。边的
*   遍历一定伴随点的遍历，而遍历边的过程一定是沿着边上的节点进行的。
* */

public class ConnectedComponent {
    private Graph graph;
    private boolean[] visited;   // 记录每个顶点是否被访问过的数组
    private int componentCount;  // 找到的连通分量个数
    private int[] setIds;        // 为每个顶点记录所在连通分量的 id 号，以检查任意两个顶点是否连通（类似并查集）

    public ConnectedComponent(Graph graph) {
        int n = graph.getVertexCount();
        this.graph = graph;
        componentCount = 0;
        visited = new boolean[n];  // 开辟的空间大小跟 graph 的顶点数一致
        setIds = new int[n];

        for (int i = 0; i < visited.length; i++) {  // 初始化 visited、setIds
            visited[i] = false;
            setIds[i] = -1;
        }

        for (int i = 0; i < visited.length; i++) {  // 搜索连通分量
            if (!visited[i]) {  // 深度优先遍历会遍历一个连通分量上的所有顶点，没有被遍历的顶点说明在其他分量上
                depthFirstSearch(i);
                componentCount++;
            }
        }
    }

    private void depthFirstSearch(int v) {  // 对传入的顶点进行深度优先遍历
        if (v < 0 || v >= graph.getVertexCount())
            throw new IllegalArgumentException("depthFirstSearch failed. Vertex out of boundary.");

        visited[v] = true;           // 访问顶点
        setIds[v] = componentCount;  // 为顶点所在连通分量的 id 赋值

        for (int w : graph.getAdjacentEdges(v)) {
            if (!visited[w])
                depthFirstSearch(w);  // 递归遍历
        }
    }

    public int getCount() { return componentCount; }

    public boolean isConnected(int v, int w) {
        int n = graph.getVertexCount();
        if (v < 0 || v >= n || w < 0 || w >= n)
            throw new IllegalArgumentException("isConnected failed. Vertex out of boundary.");
        return setIds[v] == setIds[w];
    }

    public static void main(String[] args) {
        Graph g1 = new GraphReader()
                .read("src/main/java/Graph/GraphReader/testG1.txt")
                .build(SparseGraph.class, false);

        Graph g2 = new GraphReader()
                .read("src/main/java/Graph/GraphReader/testG2.txt")
                .build(SparseGraph.class, false);

        ConnectedComponent component1 = new ConnectedComponent(g1);
        ConnectedComponent component2 = new ConnectedComponent(g2);

        log(String.format("Graph testG1 has %d connected components.", component1.getCount()));
        log(String.format("Graph testG2 has %d connected components.", component2.getCount()));

        log(component1.isConnected(1, 2));
        log(component1.isConnected(2, 10));
    }
}
