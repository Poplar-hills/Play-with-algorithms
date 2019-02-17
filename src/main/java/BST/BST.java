package BST;

/*
* 二分搜索树（Binary Search Tree）
*
* - SEE: DataStructure 中的 BST
* */

public class BST<K extends Comparable<K>, V> {
    private Node root;
    private int size;

    private class Node {
        private K key;
        private V value;
        private Node left, right;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            left = right = null;
        }
    }

    public BST() {
        root = null;
        size = 0;
    }

    public int getSize() { return size; }

    public boolean isEmpty() { return size == 0; }

    public void add(K key, V value) {
        add(root, key, value);
    }

    private Node add(Node node, K key, V value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        if (key.compareTo(node.key) < 0)
            node.left = add(node.left, key, value);
        else if (key.compareTo(node.key) > 0)
            node.right = add(node.right, key, value);
        else
            node.value = value;
        return node;
    }

    @Override
    public String toString() {
        return "";
    }

    public static void main(String[] args) {
         Integer[] arr = {5, 2, 7, 3, 1, 6, 7, 8};
         BST<Integer> bst = new BST();

         for (int n : arr)
            bst.add(n);
    }
}
