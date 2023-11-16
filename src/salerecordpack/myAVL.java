package salerecordpack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jere Perisic
 * @version November 15, 2023
 */
class myAVL {
    private Node root;

    public myAVL() {
    }
    /**
     * node in an AVL tree, height, left, right child.
     */
    private static class Node {
        List<SaleRecord> saleRecords;
        Node left;
        Node right;
        int height;
        /**
         * constructs a new Node with SaleRecord
         *
         * @param saleRecord SaleRecord stored in the node.
         */
        public Node(SaleRecord saleRecord) {
            saleRecords = new ArrayList<>();
            saleRecords.add(saleRecord);
            left = null;
            right = null;
            height = 1;
        }
    }
    /**
     * returns 0 for null node or height of the node
     *
     * @param node node to get height
     * @return 0 for null node or node height
     */
    private int height(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }
    /**
     * Right rotation
     *
     * @param y node for rotation
     * @return new root of rotated subtree
     */
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T = x.right;

        x.right = y;
        y.left = T;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }
    /**
     * left rotation
     *
     * @param x node for rotation
     * @return new root of rotated subtree
     */
    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T = y.left;

        y.left = x;
        x.right = T;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }
    /**
     * balance factor which is difference between heights of subtrees
     *
     * @param node node to get balance factor
     * @return 0 if node null or balance factor
     */
    private int getBalance(Node node) {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }
    /**
     * add saleRecord to AVL
     *
     * @param saleRecord saleRecord to add to AVL
     */
    public void add(SaleRecord saleRecord) {
        root = insert(root, saleRecord);
    }
    /**
     * insert saleRecord to AVL at the specified node
     *
     * @param node        node where saleRecord needs to be insereted
     * @param saleRecord  saleRecord to be inserted.
     * @return root of new subtree
     */
    private Node insert(Node node, SaleRecord saleRecord) {
        if (node == null) {
            return new Node(saleRecord);
        }

        if (saleRecord.date.compareTo(node.saleRecords.get(0).date) < 0) {
            node.left = insert(node.left, saleRecord);
        } else if (saleRecord.date.compareTo(node.saleRecords.get(0).date) > 0) {
            node.right = insert(node.right, saleRecord);
        } else {
            node.saleRecords.add(saleRecord);
        }

        updateNodeHeightAndBalance(node);

        return balanceNode(node, saleRecord);
    }

    private void updateNodeHeightAndBalance(Node node) {
        node.height = Math.max(height(node.left), height(node.right))+ 1;
    }
    /**
     * balance tree after new insertion
     *
     * @param node       node to be balanced
     * @param saleRecord saleRecord to be inserted.
     * @return root of balance subtree
     */
    private Node balanceNode(Node node, SaleRecord saleRecord) {
        int balance = getBalance(node);
        if (balance > 1) {
            if (saleRecord.date.compareTo(node.left.saleRecords.get(0).date) < 0) {
                return rightRotate(node);
            } else {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
        }
        if (balance < -1) {
            if (saleRecord.date.compareTo(node.right.saleRecords.get(0).date) > 0) {
                return leftRotate(node);
            } else {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }
        return node;
    }


    /**
     * searches AVL for specific dates
     *
     * @param key date to be searched
     * @return list of saleRecords on that date
     */
    public List<SaleRecord> search(String key) {
        return search(root, key);
    }
    /**
     * searches AVL for specific dates starting at the given root
     *
     * @param node root of subtree
     * @param key  date to search in AVL
     * @return list of SaleRecords with specified date.
     */
    private List<SaleRecord> search(Node node, String key) {
        if (node == null) {
            return new ArrayList<>();
        }
        List<SaleRecord> matchingSaleRecords = new ArrayList<>();
        for (SaleRecord saleRecord : node.saleRecords) {
            if (saleRecord.date.compareTo(key) == 0) {
                matchingSaleRecords.add(saleRecord);
            }
        }
        List<SaleRecord> leftSubtreeResults = search(node.left, key);
        List<SaleRecord> rightSubtreeResults = search(node.right, key);
        matchingSaleRecords.addAll(leftSubtreeResults);
        matchingSaleRecords.addAll(rightSubtreeResults);

        return matchingSaleRecords;
    }
}
