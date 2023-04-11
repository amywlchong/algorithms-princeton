import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

// Brute force solution.
// Write a program BruteCollinearPoints.java that
// examines 4 points at a time and
// checks whether they all lie on the same line segment,
// returning all such line segments.
// To check whether the 4 points p, q, r, and s are collinear,
// check whether the three slopes between p and q,
// between p and r, and between p and s are all equal.

public class BruteCollinearPoints {
    private List<LineSegment> lineSegmentList;
    private int countOfSegments;

    public BruteCollinearPoints(Point[] points) {    // finds all line segments containing 4 points

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

        Point[] pointsCopy = Arrays.copyOf(points, n);
        Arrays.sort(pointsCopy);

        for (int i = 0; i < n - 3; i++) {
            for (int j = i + 1; j < n - 2; j++) {
                for (int k = j + 1; k < n - 1; k++) {
                    for (int m = k + 1; m < n; m++) {
                        double slope1 = pointsCopy[i].slopeTo(pointsCopy[j]);
                        double slope2 = pointsCopy[i].slopeTo(pointsCopy[k]);
                        double slope3 = pointsCopy[i].slopeTo(pointsCopy[m]);
                        if (Double.compare(slope1, slope2) == 0 && Double.compare(slope1, slope3) == 0) {
                            LineSegment lineSegment = new LineSegment(pointsCopy[i], pointsCopy[m]);
                            lineSegmentList.add(lineSegment);
                            countOfSegments++;
                        }
                    }
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
