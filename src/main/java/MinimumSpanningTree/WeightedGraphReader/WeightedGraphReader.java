package MinimumSpanningTree.WeightedGraphReader;

import MinimumSpanningTree.WeightedGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WeightedGraphReader {
    List<String> lines;

    public WeightedGraphReader() {
        lines = new ArrayList<>();
    }

    public WeightedGraphReader read(String filePath) {
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

    public WeightedGraph build(Class clazz, boolean directed) {
        Constructor constructor = null;
        WeightedGraph<Double> graph = null;

        try {
            constructor = clazz.getConstructor(int.class, boolean.class);
            graph = (WeightedGraph<Double>) constructor.newInstance(8, directed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String s : lines) {
            String[] params = s.split(" ");
            graph.addEdge(Integer.parseInt(params[0]), Integer.parseInt(params[1]), Double.parseDouble(params[2]));
        }

        return graph;
    }
}
