package Graph;

import Graph.GraphReader.GraphReader;

import java.util.Iterator;

import static Utils.Helpers.log;

/*
* 通过对图进行深度遍历找到图中的连通分量（Connected Component）
*
* */

public class ConnectedComponent {
    private Graph graph;
    private boolean[] visited;   // 记录每个顶点是否被访问过的数组
    private int componentCount;  // 找到的连通分量个数
    private int[] setIds;         // 为每个顶点记录所在连通分量的 id 号，以检查任意两个顶点是否连通（类似并查集）

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

        Iterator<Integer> it = graph.adjIterator(v).iterator();

        while (it.hasNext()) {
            int nextV = it.next();
            if (!visited[nextV])
                depthFirstSearch(nextV);  // 递归遍历
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
        Graph g1 = new SparseGraph(13, false);
        new GraphReader(g1, "src/main/java/Graph/GraphReader/testG1.txt");
        ConnectedComponent component1 = new ConnectedComponent(g1);
        log(String.format("Graph testG1 has %d connected components.", component1.getCount()));

        Graph g2 = new SparseGraph(6, false);
        new GraphReader(g2, "src/main/java/Graph/GraphReader/testG2.txt");
        ConnectedComponent component2 = new ConnectedComponent(g2);
        log(String.format("Graph testG2 has %d connected components.", component2.getCount()));

        log(component1.isConnected(1, 2));
        log(component1.isConnected(2, 10));
    }
}
