package BST;

/*
* 二分搜索树（Binary Search Tree）
*
* - SEE: DataStructure 中的 BST
* */

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Consumer;

import static Utils.Helpers.log;

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

        @Override
        public String toString() { return key.toString() + ": " + value.toString(); }
    }

    public BST() {
        root = null;
        size = 0;
    }

    /*
    * 增操作
    * */
    public void add(K key, V value) {
        root = add(root, key, value);
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

    /*
    * 删操作
    * */

    public V remove(K key) {
        V retValue = search(key);
        root = remove(root, key);
        return retValue;
    }

    private Node remove(Node node, K key) {
        if (node == null)
            return null;
        if (node.key.compareTo(key) < 0) {
            node.left = remove(node.left, key);
            return node;
        }
        else if (node.key.compareTo(key) > 0) {
            node.right = remove(node.right, key);
            return node;
        }
        else {
            if (node.right == null) {
                Node leftchild = node.left;
                node.left = null;
                size--;
                return leftchild;
            }
            if (node.left == null) {
                Node rightChild = node.right;
                node.right = null;
                size--;
                return rightChild;
            }

            Node predecessor = getMax(node.left);
            predecessor.left = removeMax(node.left);
            predecessor.right = node.right;
            node.left = node.right = null;
            return predecessor;
        }

    }

    private Node removeMax(Node node) {
        
    }

    /*
     * 查操作
     * */
    public V search(K key) {  // 在 BST 中查找 key 所对应的 value
        return search(root, key);
    }

    private V search(Node node, K key) {
        if (node == null)
            return null;
        if (key.compareTo(node.key) < 0)
            return search(node.left, key);
        if (key.compareTo(node.key) > 0)
            return search(node.right, key);
        return node.value;
    }

    public boolean contains(K key) {
        return contains(root, key);
    }

    private boolean contains(Node node, K key) {
        if (node == null)
            return false;
        if (key.compareTo(node.key) < 0)
            return contains(node.left, key);
        if (key.compareTo(node.key) > 0)
            return contains(node.right, key);
        return true;
    }

    public Node getMin() {
        if (size == 0)
            throw new IllegalArgumentException("getMin failed.");
        return getMin(root);
    }

    private Node getMin(Node node) {
         return node.left != null ? getMin(node.left) : node;
    }

    public Node getMax() {
        if (size == 0)
            throw new IllegalArgumentException("getMax failed.");
        return getMax(root);
    }

    private Node getMax(Node node) {
        return node.right != null ? getMax(node.right) : node;
    }

    public int getSize() { return size; }

    public boolean isEmpty() { return size == 0; }

    /*
    * Traverse
    * */
    public void preOrderTraverse(Consumer handler) { preOrderTraverse(root, handler); }

    private void preOrderTraverse(Node node, Consumer handler) {
        if (node == null) return;
        handler.accept(node);
        preOrderTraverse(node.left, handler);
        preOrderTraverse(node.right, handler);
    }

    public void preOrderTraverseNR(Consumer handler) {
        Stack<Node> stack = new Stack<>();
        if (root == null) return;
        stack.push(root);
        while (!stack.isEmpty()) {
            Node node = stack.pop();
            handler.accept(node);
            if (node.right != null)
                stack.push(node.right);
            if (node.left != null)
                stack.push(node.left);
        }
    }

    public void inOrderTraverse(Consumer handler) {
        inOrderTraverse(root, handler);
    }

    private void inOrderTraverse(Node node, Consumer handler) {
        if (node == null) return;
        inOrderTraverse(node.left, handler);
        handler.accept(node);
        inOrderTraverse(node.right, handler);
    }

    public void inOrderTraverseNR(Consumer handler) {

    }

    public void postOrderTraverse(Consumer handler) {
        postOrderTraverse(root, handler);
    }

    private void postOrderTraverse(Node node, Consumer handler) {
        if (node == null) return;
        inOrderTraverse(node.left, handler);
        inOrderTraverse(node.right, handler);
        handler.accept(node);
    }

    public void levelOrderTraverse(Consumer handler) {
        if (root == null) return;
        handler.accept(root);
        levelOrderTraverse(root, handler);
    }

    private void levelOrderTraverse(Node node, Consumer handler) {
        if (node.left != null)
            handler.accept(node.left);
        if (node.right != null)
            handler.accept(node.right);
        if (node.left != null)
            levelOrderTraverse(node.left, handler);
        if (node.right != null)
            levelOrderTraverse(node.right, handler);
    }

    public void levelOrderTraverseNR(Consumer handler) {
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node curr = queue.remove();
            handler.accept(curr);
            if (curr.left != null)
                queue.add(curr.left);
            if (curr.right != null)
                queue.add(curr.right);
        }
    }

    /*
    * Misc
    * */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        toString(root, s, 0);
        return s.toString();
    }

    private void toString(Node node, StringBuilder s, int depth) {
        if (node == null) return;

        s.append("\n");
        for (int i = 0; i < depth; i++)
            s.append("---");
        s.append(node.toString());

        toString(node.left, s, depth + 1);
        toString(node.right, s, depth + 1);
    }

    /*
    * main
    * */
    public static void main(String[] args) {
         Integer[] arr = {5, 2, 7, 3, 1, 6, 7, 8};
         BST<Integer, Integer> bst = new BST<Integer, Integer>();

         for (int e : arr)
            bst.add(e, e * 2);

        log(bst);

        bst.preOrderTraverseNR(node -> System.out.println("-> " + node.toString()));

        log(bst.getMin());
        log(bst.getMax());
    }
}
