package MinimumSpanningTree.WeightedGraphReader;

import MinimumSpanningTree.WeightedGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.util.*;

public class WeightedGraphReader {
    List<String> lines;

    public WeightedGraphReader() {
        lines = new ArrayList<>();
    }

    public WeightedGraphReader read(String filePath) {
        if (filePath == null)
            throw new IllegalArgumentException("filename cannot be empty.");

        try (Scanner scanner = new Scanner(new File(filePath))) {  // 一次性读取文件中的所有行
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
            graph = (WeightedGraph<Double>) constructor.newInstance(getVertexCount(), directed);  // 利用反射创建 graph
        } catch (Exception e) {  // 简化了错误处理，真实场景中不能这么写
            e.printStackTrace();
        }

        for (String line : lines) {
            String[] params = line.split(" ");
            graph.addEdge(Integer.parseInt(params[0]), Integer.parseInt(params[1]), Double.parseDouble(params[2]));
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
