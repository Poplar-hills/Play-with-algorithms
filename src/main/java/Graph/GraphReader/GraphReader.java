package Graph.GraphReader;

import Graph.Graph;
import Graph.DenseGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static Utils.Helpers.log;

/*
* GraphReader 根据文件中的参数构建传入的 graph 实例
*
* - 文件中的第一行是：顶点数、边数
* - 文件中的顶点数必须等于传入的 graph 实例的顶点数
* */

public class GraphReader {
    private Scanner scanner;

    public GraphReader(Graph graph, String filename) {
        readFile(filename);
        buildGraph(graph);
    }

    private void readFile(String filename) {
        if (filename == null)
            throw new IllegalArgumentException("filename cannot be empty.");

        try {
            File file = new File(filename);
            scanner = new Scanner(file, "UTF-8");
            scanner.useLocale(Locale.ENGLISH);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void buildGraph(Graph graph) {
        try {
            int n = scanner.nextInt();  // 文件中的顶点数
            int m = scanner.nextInt();  // 文件中的边数

            if (n < 0 || m < 0)
                throw new IllegalArgumentException("buildGraph failed. Invalid vertex number or edge number.");
            if (n != graph.getVertexCount())
                throw new IllegalArgumentException("buildGraph failed. The graph has different number of edges than as in the file.");

            for (int i = 0; i < m; i++) {
                int v = scanner.nextInt();
                int w = scanner.nextInt();
                assert v >= 0 && v < n;
                assert w >= 0 && w < n;
                graph.addEdge(v, w);
            }
        } catch (InputMismatchException e) {
            String token = scanner.next();
            throw new InputMismatchException("attempts to read an 'int' value from input stream, but the next token is \"" + token + "\"");
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("attemps to read an 'int' value from input stream, but there are no more tokens available");
        }
    }

    public static void main(String[] args) {
        Graph graph = new DenseGraph(13, false);
        String FILE_PATH = "src/main/java/Graph/GraphReader/testG1.txt";
        GraphReader gr = new GraphReader(graph, FILE_PATH);

        log(graph);
    }
}
