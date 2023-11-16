import java.util.ArrayList;
import java.util.List;

class myAVL {
    private Node root;

    public myAVL() {
    }

    private static class Node {
        List<SaleRecord> saleRecords;
        Node left;
        Node right;
        int height;

        public Node(SaleRecord saleRecord) {
            saleRecords = new ArrayList<>();
            saleRecords.add(saleRecord);
            left = null;
            right = null;
            height = 1;
        }
    }

    private int height(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    private int getBalance(Node node) {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    private Node insert(Node node, SaleRecord saleRecord) {
        if (node == null)
            return new Node(saleRecord);

        if (saleRecord.date.compareTo(node.saleRecords.get(0).date) < 0) {
            node.left = insert(node.left, saleRecord);
        }
        else if (saleRecord.date.compareTo(node.saleRecords.get(0).date) > 0) {
            node.right = insert(node.right, saleRecord);
        }
        else {
            node.saleRecords.add(saleRecord);
        }
        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);

        if (balance > 1) {
            if (saleRecord.date.compareTo(node.left.saleRecords.get(0).date) < 0) {
                return rightRotate(node);
            }
            else {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
        }

        if (balance < -1) {
            if (saleRecord.date.compareTo(node.right.saleRecords.get(0).date) > 0) {
                return leftRotate(node);
            }
            else {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }

        return node;
    }

    public void add(SaleRecord saleRecord) {
        root = insert(root, saleRecord);
    }

    public List<SaleRecord> search(String key) {
        return search(root, key);
    }

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
