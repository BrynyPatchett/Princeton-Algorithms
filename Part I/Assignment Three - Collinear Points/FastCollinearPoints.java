/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> lineSegments;


    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("");
        }
        lineSegments = new ArrayList<>();
        Point[] ourPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Null Point");
            }
            ourPoints[i] = points[i];
        }

        // sort array to check for duplicates
        Arrays.sort(ourPoints);

        for (int i = 0; i < ourPoints.length - 1; i++) {
            if (ourPoints[i].compareTo(ourPoints[i + 1]) == 0) {
                throw new IllegalArgumentException("");
            }
        }


        // Think of p as the origin.
        for (int p = 0; p < ourPoints.length; p++) {
            // reset the array to be the points we originally had (undoing the sort relitive to slope of point [p])
            Arrays.sort(ourPoints);

            // sort by the slope order
            Arrays.sort(ourPoints, ourPoints[p].slopeOrder());

            // First index *Should?* be the starting of our line.
            Point startOfSegement = ourPoints[0];
            int firstOfSegemnt = 1;
            int endOfSegment = 2;
            int segmentCount = 1;

            while (endOfSegment < ourPoints.length) {
                double slopeFirst = ourPoints[0].slopeTo(ourPoints[firstOfSegemnt]);
                double slopeLast = ourPoints[0].slopeTo(ourPoints[endOfSegment]);

                while (endOfSegment < ourPoints.length &&
                        Double.compare(ourPoints[0].slopeTo(ourPoints[firstOfSegemnt]),
                                       ourPoints[0].slopeTo(ourPoints[endOfSegment])) == 0.0) {
                    endOfSegment++;
                    segmentCount++;
                }
                if (endOfSegment - firstOfSegemnt >= 3
                        && ourPoints[0].compareTo(ourPoints[firstOfSegemnt]) < 0) {
                    LineSegment line = new LineSegment(startOfSegement,
                                                       ourPoints[endOfSegment - 1]);
                    lineSegments.add(line);
                }
                segmentCount = 1;
                firstOfSegemnt = endOfSegment;
                endOfSegment++;
            }


        }

    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
    }
}
