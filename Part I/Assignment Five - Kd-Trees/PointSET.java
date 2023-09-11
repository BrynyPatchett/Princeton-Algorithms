/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

public class PointSET {

    private SET<Point2D> set;

    public PointSET() {
        set = new SET<Point2D>();
    }

    public int size() {
        return set.size();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("");
        }
        set.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("");
        }
        return set.contains(p);
    }

    public void draw() {
        for (Point2D point : set) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("");
        }
        ArrayList<Point2D> pointsInRect = new ArrayList<Point2D>();
        for (Point2D point : set) {
            if (rect.contains(point)) {
                pointsInRect.add(point);
            }
        }
        return pointsInRect;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("");
        }
        if (set.isEmpty()) {
            return null;
        }

        Point2D nearest = null;
        for (Point2D point : set) {
            if (nearest == null || point.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
                nearest = point;
            }
        }
        return nearest;
    }


    public static void main(String[] args) {


    }
}
