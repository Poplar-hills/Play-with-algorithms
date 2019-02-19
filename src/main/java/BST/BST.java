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
        if (key.compareTo(node.key) < 0) {
            node.left = remove(node.left, key);
            return node;
        }
        else if (key.compareTo(node.key) > 0) {
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

            Node successor = getMin(node.right);
            successor.right = removeMin(node.right);
            successor.left = node.left;
            node.left = node.right = null;
            return successor;
        }

    }

    private Node removeMax(Node node) {
        if (node.right == null) {
            Node leftChild = node.left;
            node.left = null;
            size--;
            return leftChild;
        }
        node.right = removeMax(node.right);
        return node;
    }

    private Node removeMin(Node node) {
        if (node.left == null) {
            Node rightChild = node.right;
            node.right = null;
            size--;
            return rightChild;
        }
        node.left = removeMin(node.left);
        return node;
    }

    public V removeMax() {
        if (root == null)
            throw new IllegalArgumentException("removeMax failed");
        Node maxNode = getMax(root);
        root = removeMax(root);
        return maxNode.value;
    }

    public V removeMin() {
        if (root == null)
            throw new IllegalArgumentException("removeMin failed");
        Node minNode = getMin(root);
        root = removeMin(root);
        return minNode.value;
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

    public K floor(K key) {
        if (size == 0 || key.compareTo(getMin(root).key) < 0)  // 如果不存在 key 的 floor 值（树为空或 key 比树中的最小值还小）
            return null;
        return floor(root, key).key;
    }

    private Node floor(Node node, K key) {
        if (node == null)
            return null;
        if (key.compareTo(node.key) == 0)  // 如果 key == node.key，则该 node 就是 key 的 floor 节点
            return node;
        if (key.compareTo(node.key) < 0)  // 如果 key < node.key，则 key 的 floor 节点一定在 node 的左子树中（因为 floor 一定小于 key）
            return floor(node.left, key);
        // 如果 key > node.key，则 node 可能是 key 的 floor 节点，也可能不是，需要尝试在 node 的右子树中寻找。
        // 因为右子树中的节点一定都 > node，因此如果其中有 < key 的节点就一定是 floor。
        Node potentialFloor = floor(node.right, key);
        return potentialFloor != null
                ? potentialFloor
                : node;
    }

    public K ceil(K key) {
        if (size == 0 || key.compareTo(getMax(root).key) > 0)  // 如果不存在 key 的 ceil 值（树为空或 key 比树中的最大值还大）
            return null;
        return ceil(root, key).key;
    }

    private Node ceil(Node node, K key) {  // 与 floor 非常类似
        if (node == null)
            return null;
        if (key.compareTo(node.key) == 0)
            return node;
        if (key.compareTo(node.key) > 0)
            return ceil(node.right, key);

        Node potentialCeil = ceil(node.left, key);
        return potentialCeil != null
                ? potentialCeil
                : node;
    }

    public int getSize() { return size; }

    public boolean isEmpty() { return size == 0; }

    /*
    * Traverse
    * */
    public void preorderTraverse(Consumer handler) { preorderTraverse(root, handler); }

    private void preorderTraverse(Node node, Consumer handler) {
        if (node == null) return;
        handler.accept(node);
        preorderTraverse(node.left, handler);
        preorderTraverse(node.right, handler);
    }

    public void preorderTraverseNR(Consumer handler) { // 使用 stack 实现（对比 levelOrderTraverse）
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

    public void inorderTraverse(Consumer handler) {
        inorderTraverse(root, handler);
    }

    private void inorderTraverse(Node node, Consumer handler) {
        if (node == null) return;
        inorderTraverse(node.left, handler);
        handler.accept(node);
        inorderTraverse(node.right, handler);
    }

    public void inorderTraverseNR(Consumer handler) {
        if (root == null)
            throw new IllegalArgumentException("inorderTraverse failed.");

        Stack<Node> stack = new Stack();
        Node curr = root;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();
            handler.accept(curr);
            curr = curr.right;
        }
    }

    public void postOrderTraverse(Consumer handler) {
        postOrderTraverse(root, handler);
    }

    private void postOrderTraverse(Node node, Consumer handler) {
        if (node == null) return;
        inorderTraverse(node.left, handler);
        inorderTraverse(node.right, handler);
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
        s.append(String.format("\nSize: %d, Min: %d, Max: %d", getSize(), getMin().key, getMax().key));
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
        Integer[] arr = {7, 2, 0, 8, 9, 4, 4, 5, 3};
        BST<Integer, Integer> bst = new BST<Integer, Integer>();

        for (int e : arr)
            bst.add(e, e * 2);
        log(bst);

        bst.inorderTraverseNR(System.out::println);

        bst.remove(2);
        bst.remove(7);
        bst.remove(0);
        log(bst);
    }
}
