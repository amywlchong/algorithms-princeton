import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;

// Brute-force implementation.
// Write a mutable data type PointSET.java that
// represents a set of points in the unit square.

public class PointSET {
    private TreeSet<Point2D> pointSet;

    public PointSET() {                               // construct an empty set of points
        this.pointSet = new TreeSet<>();
    }

    public boolean isEmpty() {                      // is the set empty?
        return this.pointSet.isEmpty();
    }

    public int size() {                         // number of points in the set
        return this.pointSet.size();
    }

    public void insert(Point2D p) {              // add the point to the set (if it is not already in the set)
        checkNotNull(p);
        this.pointSet.add(p);
    }

    public boolean contains(Point2D p) {            // does the set contain point p?
        checkNotNull(p);
        return this.pointSet.contains(p);
    }

    public void draw() {                         // draw all points to standard draw
        StdDraw.setPenRadius(0.01);
        for (Point2D point : pointSet) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {             // all points that are inside the rectangle (or on the boundary)
        checkNotNull(rect);
        TreeSet<Point2D> pointsInRect = new TreeSet<>();
        for (Point2D point : pointSet) {
            if (rect.contains(point)) {
                pointsInRect.add(point);
            }
        }
        return pointsInRect;
    }

    public Point2D nearest(Point2D p) {             // a nearest neighbor in the set to point p; null if the set is empty
        checkNotNull(p);
        if (this.isEmpty()) {
            return null;
        }
        Point2D nearestNeighbor = pointSet.first();
        for (Point2D point : pointSet) {
            if (point.distanceSquaredTo(p) < nearestNeighbor.distanceSquaredTo(p)) {
                nearestNeighbor = point;
            }
        }
        return nearestNeighbor;
    }

    private void checkNotNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
    }

    // unit testing
    public static void main(String[] args) {
        // Test 1: Construct PointSET
        // System.out.println("Test 1: Construct PointSET");
        // PointSET pointSet1 = new PointSET();
        // System.out.println("PointSET constructed successfully");

        // Test 2: Check if the set is initially empty
        System.out.println("\nTest 2: Check if the set is initially empty");
        PointSET pointSet2 = new PointSET();
        System.out.println("Is empty? " + pointSet2.isEmpty()); // Expected: true

        // Test 3: Check size of the empty set
        System.out.println("\nTest 3: Check size of the empty set");
        PointSET pointSet3 = new PointSET();
        System.out.println("Size: " + pointSet3.size()); // Expected: 0

        // Test 4: Insert a point and check the size
        System.out.println("\nTest 4: Insert a point and check the size and if set is empty");
        PointSET pointSet4 = new PointSET();
        Point2D point1 = new Point2D(0.1, 0.1);
        pointSet4.insert(point1);
        System.out.println("Size after inserting a point: " + pointSet4.size()); // Expected: 1
        System.out.println("Is empty after inserting a point? " + pointSet4.isEmpty()); // Expected: false

        // Test 5: Check if the set contains the point that was just added
        System.out.println("\nTest 5: Check if the set contains the point that was just added");
        PointSET pointSet5 = new PointSET();
        pointSet5.insert(point1);
        System.out.println("Contains inserted point (0.1, 0.1)? " + pointSet5.contains(point1)); // Expected: true
        Point2D pointNotInserted = new Point2D(0.5, 0.5);
        System.out.println("Contains not-inserted point (0.5, 0.5)? " + pointSet5.contains(pointNotInserted)); // Expected: false

        // Test 6: Check the nearest point
        System.out.println("\nTest 6: Check the nearest point");
        PointSET pointSet6 = new PointSET();
        Point2D point2 = new Point2D(0.2, 0.2);
        Point2D point3 = new Point2D(0.4, 0.4);
        Point2D queryPoint = new Point2D(0.25, 0.25);
        pointSet6.insert(point1);
        pointSet6.insert(point2);
        pointSet6.insert(point3);
        System.out.println("Nearest point to (0.25, 0.25): " + pointSet6.nearest(queryPoint)); // Expected: (0.2, 0.2)

        // Test 7: Check all points inside the rectangle
        System.out.println("\nTest 7: Check all points inside the rectangle");
        PointSET pointSet7 = new PointSET();
        RectHV rect = new RectHV(0, 0, 0.5, 0.5);
        Point2D pointOutsideRect = new Point2D(0.9, 0.9);
        pointSet7.insert(point1);
        pointSet7.insert(point2);
        pointSet7.insert(pointOutsideRect);
        System.out.println("Points inside the rectangle: ");
        for (Point2D point : pointSet7.range(rect)) {
            System.out.println(point);  // Expected: (0.1, 0.1), (0.2, 0.2)
        }

        // Test 8: Draw the points
        System.out.println("\nTest 8: Draw the points");
        PointSET pointSet8 = new PointSET();
        pointSet8.insert(point1);
        pointSet8.insert(point2);
        pointSet8.insert(point3);
        pointSet8.draw();

        System.out.println("\nAll tests completed!");
    }

}
