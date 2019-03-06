package Graph.GraphReader;

import Graph.Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.util.*;

public class GraphReader {
    List<String> lines;

    public GraphReader() {
        lines = new ArrayList<>();
    }

    public GraphReader read(String filePath) {
        if (filePath == null)
            throw new IllegalArgumentException("filename cannot be empty.");

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNext())
                lines.add(scanner.nextLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return this;
    }

    public Graph build(Class clazz, boolean directed) {
        Constructor constructor = null;
        Graph graph = null;

        try {
            constructor = clazz.getConstructor(int.class, boolean.class);
            graph = (Graph) constructor.newInstance(getVertexCount(), directed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String line : lines) {
            String[] params = line.split(" ");
            graph.addEdge(Integer.parseInt(params[0]), Integer.parseInt(params[1]));
        }

        return graph;
    }

    private int getVertexCount() {
        return (int) lines.stream()
                .flatMap(x -> Arrays.stream(x.split(" ")).limit(2))
                .distinct()
                .count();
    }
}
