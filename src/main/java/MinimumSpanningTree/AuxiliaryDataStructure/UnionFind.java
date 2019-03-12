package MinimumSpanningTree.AuxiliaryDataStructure;

import java.util.Arrays;

import static Utils.Helpers.log;

/*
* 并查集（作为 KruskalMST 的辅助数据结构）
*
* - 采用了 UnionFind6 中的实现，即 Quick Union + rank 优化 + path compression 优化。
* */

public class UnionFind {
    private int[] setIds;
    private int[] sizes;

    public UnionFind(int size) {
        setIds = new int[size];
        sizes = new int[size];

        for (int i = 0; i < setIds.length; i++) {
            setIds[i] = i;
        }
    }

    private int find(int p) {
        if (p < 0 || p >= setIds.length)
            throw new IllegalArgumentException("find failed. p is out of bound.");
        return (setIds[p] == p) ? p : find(setIds[p]);
    }

    public boolean isConnencted(int p, int q) {
        return find(p) == find(q);
    }

    public void union(int p, int q) {
        int pRoot = find(p);
        int qRoot = find(q);

        if (pRoot == qRoot)
            return;

        if (sizes[pRoot] < sizes[qRoot]) {
            setIds[pRoot] = qRoot;
            sizes[qRoot] += sizes[pRoot];
        } else {
            setIds[qRoot] = pRoot;
            sizes[pRoot] += sizes[qRoot];
        }
    }

    public int getSize() { return setIds.length; }

    @Override
    public String toString() {
        return Arrays.toString(setIds);
    }

    public static void main(String[] args) {
        UnionFind uf = new UnionFind(5);

        uf.union(2, 4);
        log(uf);
        uf.union(4, 0);
        log(uf);

        log(uf.isConnencted(2, 0));
    }
}
