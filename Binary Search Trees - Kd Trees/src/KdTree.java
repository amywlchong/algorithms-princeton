import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.List;
import java.util.ArrayList;

// 2d-tree implementation.
// Write a mutable data type KdTree.java that
// uses a 2d-tree to implement the same API
// (but replace PointSET with KdTree).
// A 2d-tree is a generalization of a BST to two-dimensional keys.
// The idea is to build a BST with points in the nodes,
// using the x- and y-coordinates of the points as keys
// in strictly alternating sequence.

public class KdTree {

    private Node root;
    private int size;

    private static class Node {
        private final Point2D point;
        private final RectHV rect;
        private final boolean isOddLevel;     // To alternate between comparing x and y coordinates in the tree
        private Node left, right;

        public Node(Point2D point, double xmin, double ymin, double xmax, double ymax, boolean isOddLevel) {
            this.point = point;
            this.rect = new RectHV(xmin, ymin, xmax, ymax);
            this.isOddLevel = isOddLevel;
        }

        private double compareToPoint(Point2D p) {
            if (!isOddLevel) {
                return p.x() - point.x();
            } else {
                return p.y() - point.y();
            }
        }

    }

    public KdTree() {                               // construct an empty tree of points
        this.root = null;
        this.size = 0;
    }

    public boolean isEmpty() {                      // is the tree empty?
        return root == null;
    }

    public int size() {                         // number of points in the tree
        return size;
    }

    // Insert and search.
    // The algorithms for insert and search are similar to those for BSTs,
    // but at the root we use the x-coordinate
    // (if the point to be inserted has a smaller x-coordinate
    // than the point at the root, go left; otherwise go right);
    // then at the next level, we use the y-coordinate
    // (if the point to be inserted has a smaller y-coordinate
    // than the point in the node, go left; otherwise go right);
    // then at the next level the x-coordinate, and so forth.

    public void insert(Point2D p) {             // add the point to the tree (if it is not already in the tree)
        checkNotNull(p);

        if (this.contains(p)) {
            return;
        }

        root = insertRecursive(root, p, 0.0, 0.0, 1.0, 1.0, false);
        size++;
    }

    // Recursive function to insert a new point into the KdTree
    private Node insertRecursive(Node node, Point2D newPoint, double xmin, double ymin, double xmax, double ymax, boolean isOddLevel) {

        if (node == null) {
            return new Node(newPoint, xmin, ymin, xmax, ymax, isOddLevel);
        }

        double cmp = node.compareToPoint(newPoint);

        if (cmp < 0) {
            if (!node.isOddLevel) {

                node.left = insertRecursive(node.left, newPoint,
                        node.rect.xmin(), node.rect.ymin(), node.point.x(), node.rect.ymax(), true);
            } else {

                node.left = insertRecursive(node.left, newPoint,
                        node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.point.y(), false);
            }
        }

        else {
            if (!node.isOddLevel) {

                node.right = insertRecursive(node.right, newPoint,
                        node.point.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax(), true);
            } else {

                node.right = insertRecursive(node.right, newPoint,
                        node.rect.xmin(), node.point.y(), node.rect.xmax(), node.rect.ymax(), false);
            }
        }

        return node;
    }

    public boolean contains(Point2D comparedPoint) {            // does the tree contain point p?
        checkNotNull(comparedPoint);

        Node x = root;
        double cmp;
        while (x != null) {
            if (x.point.equals(comparedPoint)) {
                return true;
            }
            cmp = x.compareToPoint(comparedPoint);
            if (cmp < 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        return false;
    }

    // Draw.
    // The draw() method should draw all of the points to standard draw in black
    // and the subdivisions in red (for vertical splits) and blue (for horizontal splits).
    // This method need not be efficient—it is primarily for debugging.
    public void draw() {                         // draw all points to standard draw
        for (Node node : this.getNodesInOrder()) {
            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(StdDraw.BLACK);
            node.point.draw();

            StdDraw.setPenRadius(0.002);
            if (!node.isOddLevel) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());
            }
        }
    }

    // Return a queue of the nodes in the k-d tree.
    private Queue<Node> getNodesInOrder() {
        Queue<Node> q = new Queue<>();
        inorder(root, q);
        return q;
    }

    // Perform an in-order traversal of the tree.
    private void inorder(Node x, Queue<Node> q) {

        if (x == null) return;

        // An in-order traversal visits nodes in the following order: left child, current node, right child.

        inorder(x.left, q);

        q.enqueue(x);

        inorder(x.right, q);
    }

    // Range search.
    public Iterable<Point2D> range(RectHV rect) {             // to find all points that are inside the rectangle (or on the boundary)
        checkNotNull(rect);

        List<Point2D> pointsInRect = new ArrayList<>();
        addPointsInRect(root, rect, pointsInRect);
        return pointsInRect;
    }

    // Recursive function to add to the ArrayList all points that fall within the rectangle
    private void addPointsInRect(Node x, RectHV rect, List<Point2D> list) {

        if (x == null) return;

        if (rect.contains(x.point) && !list.contains(x.point)) {
            list.add(x.point);
        }

        if (x.left != null && rect.intersects(x.left.rect)) {
            addPointsInRect(x.left, rect, list);
        }

        if (x.right != null && rect.intersects(x.right.rect)) {
            addPointsInRect(x.right, rect, list);
        }
    }

    // Nearest-neighbor search.
    public Point2D nearest(Point2D p) {             // to find a nearest neighbor in the tree to point p; null if the tree is empty
        checkNotNull(p);
        if (root == null) {
            return null;
        }
        return findNearestPoint(root, p, root.point);
    }

    // The findNearestPoint method starts at the given node 'x' and explores both subtrees
    // to find the point in the tree that is closest to a given query point 'p'.
    private Point2D findNearestPoint(Node x, Point2D p, Point2D nearestPointSoFar) {

        if (x.point.distanceSquaredTo(p) < nearestPointSoFar.distanceSquaredTo(p)) {
            nearestPointSoFar = x.point;
        }

        if (p.compareTo(x.point) == 0) {
            return x.point;
        }

        double cmp = x.compareToPoint(p);

        if (cmp < 0) {
            nearestPointSoFar = searchAndUpdateNearestPoint(x.left, p, nearestPointSoFar);
            nearestPointSoFar = searchAndUpdateNearestPoint(x.right, p, nearestPointSoFar);
        }

        else {
            nearestPointSoFar = searchAndUpdateNearestPoint(x.right, p, nearestPointSoFar);
            nearestPointSoFar = searchAndUpdateNearestPoint(x.left, p, nearestPointSoFar);
        }

        return nearestPointSoFar;
    }

    // Searches for the point closest to 'p'.
    private Point2D searchAndUpdateNearestPoint(Node node, Point2D p, Point2D nearestPointSoFar) {
        if (node != null) {
            if (node.rect.distanceSquaredTo(p) < nearestPointSoFar.distanceSquaredTo(p)) {
                nearestPointSoFar = findNearestPoint(node, p, nearestPointSoFar);
            }
        }
        return nearestPointSoFar;
    }

    private void checkNotNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
    }

    // unit testing
    public static void main(String[] args) {
        // Test 1: Construct KdTree
        // System.out.println("Test 1: Construct KdTree");
        // KdTree kdTree1 = new KdTree();
        // System.out.println("KdTree constructed successfully");

        // Test 2: Check if the tree is initially empty
        System.out.println("\nTest 2: Check if the tree is initially empty");
        KdTree kdTree2 = new KdTree();
        System.out.println("Is empty? " + kdTree2.isEmpty()); // Expected: true

        // Test 3: Check size of the empty tree
        System.out.println("\nTest 3: Check size of the empty tree");
        KdTree kdTree3 = new KdTree();
        System.out.println("Size: " + kdTree3.size()); // Expected: 0

        // Test 4: Insert a point and check the size
        System.out.println("\nTest 4: Insert a point and check the size and if tree is empty");
        KdTree kdTree4 = new KdTree();
        Point2D point1 = new Point2D(0.1, 0.1);
        kdTree4.insert(point1);
        System.out.println("Size after inserting a point: " + kdTree4.size()); // Expected: 1
        System.out.println("Is empty after inserting a point? " + kdTree4.isEmpty()); // Expected: false

        // Test 5: Check if the tree contains the point that was just added
        System.out.println("\nTest 5: Check if the tree contains the point that was just added");
        KdTree kdTree5 = new KdTree();
        kdTree5.insert(point1);
        System.out.println("Contains inserted point (0.1, 0.1)? " + kdTree5.contains(point1)); // Expected: true
        Point2D pointNotInserted = new Point2D(0.5, 0.5);
        System.out.println("Contains not-inserted point (0.5, 0.5)? " + kdTree5.contains(pointNotInserted)); // Expected: false

        // Test 6: Check the nearest point
        System.out.println("\nTest 6: Check the nearest point");
        KdTree kdTree6 = new KdTree();
        Point2D point2 = new Point2D(0.2, 0.2);
        Point2D point3 = new Point2D(0.4, 0.4);
        Point2D queryPoint = new Point2D(0.25, 0.25);
        kdTree6.insert(point1);
        kdTree6.insert(point2);
        kdTree6.insert(point3);
        System.out.println("Nearest point to (0.25, 0.25): " + kdTree6.nearest(queryPoint)); // Expected: (0.2, 0.2)

        // Test 7: Check all points inside the rectangle
        System.out.println("\nTest 7: Check all points inside the rectangle");
        KdTree kdTree7 = new KdTree();
        RectHV rect = new RectHV(0, 0, 0.5, 0.5);
        Point2D pointOutsideRect = new Point2D(0.9, 0.9);
        kdTree7.insert(point1);
        kdTree7.insert(point2);
        kdTree7.insert(pointOutsideRect);
        System.out.println("Points inside the rectangle: ");
        for (Point2D point : kdTree7.range(rect)) {
            System.out.println(point);  // Expected: (0.1, 0.1), (0.2, 0.2)
        }

        // Test 8: Draw the points and the subdivisions
        System.out.println("\nTest 8: Draw the points and the subdivisions");
        KdTree kdTree8 = new KdTree();
        kdTree8.insert(point1);
        kdTree8.insert(point2);
        kdTree8.insert(point3);
        kdTree8.draw();

        System.out.println("\nAll tests completed!");
    }

}
