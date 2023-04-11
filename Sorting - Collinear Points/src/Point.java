import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

public final class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        if (this.x == that.x && this.y == that.y) {
            return Double.NEGATIVE_INFINITY;
        }
        if (this.x == that.x) {
            return Double.POSITIVE_INFINITY;
        }
        if (this.y == that.y) {
            return +0.0;
        }
        return 1.0 * (that.y - this.y) / (that.x - this.x) + 0.0;
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     * point (x0 = x1 and y0 = y1);
     * a negative integer if this point is less than the argument
     * point; and a positive integer if this point is greater than the
     * argument point
     */
    public int compareTo(Point that) {
        if (this.y == that.y) {
            return this.x - that.x;
        }
        return this.y - that.y;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     * The slopeOrder() method should return a comparator that compares
     * its two argument points by the slopes they make with the
     * invoking point (x0, y0). Formally, the point (x1, y1) is less than
     * the point (x2, y2) if and only if the slope (y1 − y0) / (x1 − x0)
     * is less than the slope (y2 − y0) / (x2 − x0).
     *
     * @return the Comparator that defines this ordering on points
     */

    private class BySlope implements Comparator<Point> {

        @Override
        public int compare(Point a, Point b) {
            return Double.compare(Point.this.slopeTo(a), Point.this.slopeTo(b));
        }
    }

    public Comparator<Point> slopeOrder() {
        return new BySlope();
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {

        // Test 1: Construct Point
        System.out.println("Test 1: Construct Point");
        Point point1 = new Point(2, 3);
        System.out.println("Point: " + point1.toString()); // Expected: (2, 3)

        // Test 2: Compare two points
        System.out.println("\nTest 2: Compare two points");
        Point point2 = new Point(4, 5);
        Point point3 = new Point(2, 3);
        int comparisonResult = point2.compareTo(point3);
        System.out.println("Comparison result: " + comparisonResult); // Expected: positive integer (as point2 is greater than point3)
        int comparisonResult2 = point3.compareTo(point2);
        System.out.println("Comparison result 2: " + comparisonResult2); // Expected: negative integer (as point3 is less than point2)

        // Test 3: Check slope between two points and slope order comparator
        Point point6 = new Point(2, 3);
        Point point7 = new Point(4, 5);
        Point point8 = new Point(2, 5);

        // Test 3.1: Check slopeTo with point7
        System.out.println("\nTest 3.1: Calculate slope between two points");
        double slopeTo7 = point6.slopeTo(point7);
        System.out.println("Slope to point7: " + slopeTo7); // Expected: 1.0 (as points point6 and point7 are on a 45 degree line)

        // Test 3.2: Check slopeTo with point8
        System.out.println("\nTest 3.2: Calculate slope between two points");
        double slopeTo8 = point6.slopeTo(point8);
        System.out.println("Slope to point8: " + slopeTo8); // Expected: +Infinity (as points point6 and point8 are vertical)

        // Test 3.3: Check slope order comparator
        System.out.println("\nTest 3.3: Check slope order comparator");
        Comparator<Point> comparator = point6.slopeOrder();
        int comparatorResult = comparator.compare(point7, point8);
        System.out.println("Comparator result: " + comparatorResult); // Expected: -1 (as point7 is less than point8 in terms of slope with point6)
        int comparatorResult2 = comparator.compare(point8, point7);
        System.out.println("Comparator result 2: " + comparatorResult2); // Expected: +1 (as point8 is greater than point7 in terms of slope with point6)

        System.out.println("\nAll tests completed!");
    }

}
