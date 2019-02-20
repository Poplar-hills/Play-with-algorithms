package UnionFind;

/*
 * 并查集（Union Find）第四版
 *
 * SEE: play-with-data-structure/UnionFind
 * */

public class UnionFind4 implements UF {
    private int[] setIds;
    private int[] ranks;

    public UnionFind4(int size) {
        setIds = new int[size];
        ranks = new int[size];
        for (int i = 0; i < size; i++) {
            setIds[i] = i;
            ranks[i] = 1;
        }
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

        if (ranks[pRoot] < ranks[qRoot])
            setIds[pRoot] = setIds[qRoot];
        else if (ranks[pRoot] > ranks[qRoot])
            setIds[qRoot] = setIds[pRoot];
        else {
            setIds[qRoot] = setIds[pRoot];
            ranks[pRoot]++;
        }
    }

    @Override
    public int getSize() {
        return setIds.length;
    }
}
