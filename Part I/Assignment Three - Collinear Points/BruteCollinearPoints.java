/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> lineSegments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("");
        }


        lineSegments = new ArrayList<>();

        // copy input array

        Point[] ourPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Null Point");
            }
            ourPoints[i] = points[i];
        }

        // Sort out copied array by the compreTo() points method.
        // Will be in sorted order meaning can check for duplicates, also means the lowest and highest index is line segment start and endpoint respectively
        Arrays.sort(ourPoints);

        for (int i = 0; i < ourPoints.length - 1; i++) {
            if (ourPoints[i].compareTo(ourPoints[i + 1]) == 0) {
                throw new IllegalArgumentException("Duplicate Point");
            }
        }


        for (int p = 0; p < ourPoints.length - 3; p++) {
            for (int q = p + 1; q < ourPoints.length - 2; q++) {
                double pq = ourPoints[p].slopeTo(ourPoints[q]);
                // System.out.println(pq);
                for (int r = q + 1; r < ourPoints.length - 1; r++) {
                    double pr = ourPoints[p].slopeTo(ourPoints[r]);
                    // System.out.println(pr);
                    if (Double.compare(pr, pq) == 0.0) {
                        for (int s = r + 1; s < ourPoints.length; s++) {
                            double ps = ourPoints[p].slopeTo(ourPoints[s]);
                            // System.out.println(ps);
                            if (Double.compare(ps, pq) == 0.0) {
                                // System.out.println("Duplicate: " + pq + " " + pr + " " + ps + " ");
                                LineSegment line = new LineSegment(ourPoints[p], ourPoints[s]);
                                lineSegments.add(line);
                            }
                        }
                    }
                }
            }
        }

    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
    }

    public static void main(String[] args) {

    }
}
