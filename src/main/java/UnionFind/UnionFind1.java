package UnionFind;

/*
* 并查集（Union Find）第一版
*
* SEE: play-with-data-structure/UnionFind
* */

public class UnionFind1 implements UF {
    private int[] setIds;

    public UnionFind1(int size) {
        setIds = new int[size];
        for (int i = 0; i < size; i++)
            setIds[i] = i;
    }

    private int find(int p) {
        if (p < 0 || p >= setIds.length)
            throw new IllegalArgumentException("find failed.");
        return setIds[p];
    }

    @Override
    public boolean isConnected(int p, int q) {
        return find(p) == find(q);
    }

    @Override
    public void union(int p, int q) {
        int pId = find(p);
        int qId = find(q);

        if (pId == qId)
            return;

        for (int i = 0; i < setIds.length; i++)
            if (setIds[i] == pId)
                setIds[i] = qId;
    }

    @Override
    public int getSize() {
        return setIds.length;
    }
}
