package MinimumSpanningTree.AuxiliaryDataStructure;

import java.util.Arrays;

import static Utils.Helpers.log;

/*
* 并查集（作为 KruskalMST 的辅助数据结构）
*
* - 采用了 UnionFind5 中的实现，即 Quick Union + rank 优化 + path compression 优化。
* */

public class UnionFind {
    private int[] setIds;
    private int[] ranks;

    public UnionFind(int size) {
        setIds = new int[size];
        ranks = new int[size];

        for (int i = 0; i < size; i++) {
            setIds[i] = i;
            ranks[i] = 1;
        }
    }

    private int find(int p) {
        if (p < 0 || p >= setIds.length)
            throw new IllegalArgumentException("find failed. p is out of bound.");

        while (setIds[p] != p) {
            setIds[p] = setIds[setIds[p]];
            p = setIds[p];
        }
        return p;
    }

    public boolean isConnencted(int p, int q) {
        return find(p) == find(q);
    }

    public void union(int p, int q) {
        int pRoot = find(p);
        int qRoot = find(q);

        if (pRoot == qRoot)
            return;

        if (ranks[pRoot] < ranks[qRoot]) {
            setIds[pRoot] = qRoot;
        } else if (ranks[pRoot] > ranks[qRoot]) {
            setIds[qRoot] = pRoot;
        } else {
            setIds[pRoot] = qRoot;
            ranks[qRoot]++;
        }
    }

    public int getSize() { return setIds.length; }

    @Override
    public String toString() {
        return Arrays.toString(setIds);
    }

    public static void main(String[] args) {
        UnionFind uf = new UnionFind(6);

        uf.union(3, 2);
        log(uf);
        uf.union(5, 3);
        log(uf);
        uf.union(4, 0);
        log(uf);
        uf.union(4, 5);
        log(uf);

        log(uf.isConnencted(4, 0));
        log(uf.isConnencted(4, 1));
        log(uf.isConnencted(4, 2));
        log(uf.isConnencted(4, 3));
        log(uf.isConnencted(4, 5));
    }
}
