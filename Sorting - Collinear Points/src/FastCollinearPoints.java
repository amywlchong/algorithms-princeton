import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

// A faster, sorting-based solution.
// Remarkably, it is possible to solve the problem much faster
// than the brute-force solution.
// Given a point p, the following method determines
// whether p participates in a set of 4 or more collinear points.
//        Think of p as the origin.
//        For each other point q, determine the slope it makes with p.
//        Sort the points according to the slopes they makes with p.
//        Check if any 3 (or more) adjacent points in the sorted order
//        have equal slopes with respect to p.
//        If so, these points, together with p, are collinear.
// Applying this method for each of the n points
// in turn yields an efficient algorithm to the problem.

public class FastCollinearPoints {
    private List<LineSegment> lineSegmentList;
    private int countOfSegments;

    public FastCollinearPoints(Point[] points) {    // finds all line segments containing 4 or more points

        if (points == null) {
            throw new IllegalArgumentException("Input points array cannot be null");
        }
        if (Arrays.asList(points).contains(null)) {
            throw new IllegalArgumentException("Input points array cannot contain null elements");
        }

        int n = points.length;

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (points[i].compareTo(points[j]) == 0 || points[i].slopeTo(points[j]) == Double.NEGATIVE_INFINITY) {
                    throw new IllegalArgumentException("Points cannot be the same");
                }
            }
        }

        this.lineSegmentList = new ArrayList<>();
        this.countOfSegments = 0;

        // Create a copy of the points array so original is not modified
        Point[] pointsCopy = Arrays.copyOf(points, n);

        // For every point, check if there are 3 or more other points that have the same slope with the current point
        for (int p = 0; p < n - 1; p++) {

            Arrays.sort(pointsCopy);
            Point point = pointsCopy[p];

            Arrays.sort(pointsCopy, point.slopeOrder());

            int start = 1;
            int end = 1;

            for (int j = 2; j < n; j++) {
                double prevSlope = point.slopeTo(pointsCopy[start]);
                double currentSlope = point.slopeTo(pointsCopy[j]);

                // If slopes are equal, extend the potential line segment to include current point
                if (Double.compare(currentSlope, prevSlope) == 0) {
                    end = j;
                }

                // When we reach the end of the array or the slopes are different,
                // it means the potential line segment has ended.
                // We then check if it is a segment containing 4 or more collinear points that hasn't been added to the list.
                if ((j == n - 1 || Double.compare(currentSlope, prevSlope) != 0) && (end - start) >= 2) {
                    if (point.compareTo(pointsCopy[start]) < 0) {
                        lineSegmentList.add(new LineSegment(point, pointsCopy[end]));
                        countOfSegments++;
                    }
                }

                // If the slopes are different, it means we've moved to a new potential line segment.
                // Thus, we reset 'start' and 'end' to the current point.
                if (Double.compare(currentSlope, prevSlope) != 0) {
                    start = j;
                    end = j;
                }
            }
        }
    }

    public int numberOfSegments() {                 // the number of line segments
        return countOfSegments;
    }

    public LineSegment[] segments() {                // array of line segments
        LineSegment[] finalLineSegments = new LineSegment[countOfSegments];
        return lineSegmentList.toArray(finalLineSegments);
    }

}
