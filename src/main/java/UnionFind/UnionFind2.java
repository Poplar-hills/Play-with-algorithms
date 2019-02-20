package UnionFind;

/*
 * 并查集（Union Find）第二版
 *
 * SEE: play-with-data-structure/UnionFind
 * */

public class UnionFind2 implements UF {
    private int[] setIds;

    public UnionFind2(int size) {
        setIds = new int[size];
        for (int i = 0; i < size; i++)
            setIds[i] = i;
    }

    private int find(int p) {
        if (p < 0 || p >= setIds.length)
            throw new IllegalArgumentException("find failed.");
        return setIds[p] == p ? p : find(setIds[p]);
    }

    @Override
    public boolean isConnected(int p, int q) {
        return find(p) == find(q);
    }

    @Override
    public void union(int p, int q) {
        int pRoot = find(p);
        int qRoot = find(q);

        if (pRoot == qRoot)
            return;

        setIds[pRoot] = qRoot;
    }

    @Override
    public int getSize() {
        return setIds.length;
    }
}
