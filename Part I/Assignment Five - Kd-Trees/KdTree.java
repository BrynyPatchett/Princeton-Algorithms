/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {

    private Node root;
    private int size;

    private class Node {
        double[] cords;
        RectHV rect;
        private Node left;
        private Node right;

        private Node(double[] pAxes) {
            cords = new double[2];
            cords[0] = pAxes[0];
            cords[1] = pAxes[1];

        }

        private Node(double[] pAxes, double[] rectMin, double[] rectMax) {
            cords = new double[2];
            cords[0] = pAxes[0];
            cords[1] = pAxes[1];
            rect = new RectHV(rectMin[0], rectMin[1], rectMax[0], rectMax[1]);

        }


    }


    public KdTree() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        if (size > 0) return false;
        return true;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        // if the point we provided is null throw an error
        if (p == null) {
            throw new IllegalArgumentException("");
        }
        double[] rectMin = { 0.0, 0.0, };
        double[] rectMax = { 1.0, 1.0 };
        double[] axes = new double[2];
        axes[0] = p.x();
        axes[1] = p.y();
        root = insert(root, 0, axes, rectMin, rectMax);
    }


    private Node insert(Node node, int depth, double[] axes, double[] rectMin, double[] rectMax) {
        if (node == null) {
            size++;
            return new Node(axes, rectMin, rectMax);
        }
        int axis = depth % node.cords.length;
        int compareResult = Double.compare(node.cords[axis], axes[axis]);
        if (compareResult == 0) {
            compareResult = Double.compare(node.cords[(depth + 1) % node.cords.length],
                                           axes[(depth + 1) % node.cords.length]);
        }

        if (compareResult > 0) {
            rectMax[axis] = node.cords[axis];
            node.left = insert(node.left, depth + 1, axes, rectMin, rectMax);
        }

        else if (compareResult < 0) {
            rectMin[axis] = node.cords[axis];
            node.right = insert(node.right, depth + 1, axes, rectMin, rectMax);
        }
        return node;
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("");
        }
        double[] axes = new double[2];
        axes[0] = p.x();
        axes[1] = p.y();
        return contains(root, 0, axes);
    }


    private boolean contains(Node node, int depth, double[] axes) {
        if (node == null) {
            return false;
        }
        int axis = depth % node.cords.length;
        double compareResult = Double.compare(node.cords[axis], axes[axis]);

        if (compareResult == 0) {
            compareResult = Double.compare(node.cords[(depth + 1) % node.cords.length],
                                           axes[(depth + 1) % node.cords.length]);
            if (compareResult == 0) {
                return true;
            }
        }

        if (compareResult > 0) {
            return contains(node.left, depth + 1, axes);
        }

        else {
            return contains(node.right, depth + 1, axes);
        }
    }


    private Iterable<Node> iterator() {
        Queue<Node> queue = new Queue<Node>();
        queue = breadthFirst(queue, root);
        return queue;
    }

    private Queue<Node> breadthFirst(Queue<Node> queue, Node node) {
        if (node == null) {
            return queue;
        }
        queue.enqueue(node);
        breadthFirst(queue, node.left);
        breadthFirst(queue, node.right);
        return queue;

    }


    public void draw() {
        Queue<Node> q = new Queue<Node>();
        q = breadthFirst(q, root);

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);

        int depth = 0;
        for (Node p : q) {
            Point2D point = new Point2D(p.cords[0], p.cords[1]);
            point.draw();
            RectHV rect = p.rect;
            rect.draw();
        }
    }


    public Iterable<Point2D> range(RectHV rect) {

        if (rect == null) {
            throw new IllegalArgumentException("");

        }

        Queue<Node> queue = new Queue<Node>();
        searchRect(rect, root, queue);

        ArrayList<Point2D> points = new ArrayList<Point2D>();


        for (Node p : queue) {
            Point2D point = new Point2D(p.cords[0], p.cords[1]);
            points.add(point);
        }

        return points;


    }

    private Queue<Node> searchRect(RectHV rect, Node node, Queue<Node> q) {
        if (node == null) {
            return q;
        }

        if (node.rect.intersects(rect)) {
            if (rect.contains(new Point2D(node.cords[0], node.cords[1]))) {
                q.enqueue(node);
            }
            searchRect(rect, node.left, q);
            searchRect(rect, node.right, q);
        }

        return q;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("");
        }
        if (size == 0) {
            return null;
        }
        double[] axis = { p.x(), p.y() };
        Point2D nearest = new Point2D(root.cords[0], root.cords[1]);
        return nearest(p, root, nearest, 0, axis);
    }

    private Point2D nearest(Point2D p, Node current, Point2D nearest, int depth, double[] axes) {
        if (current != null) {

            Point2D currentPoint = new Point2D(current.cords[0], current.cords[1]);
            double nearestdist = nearest.distanceSquaredTo(p);
            // if the current nearst to the point is a bigger distance than the current rectangle to the point
            if (nearestdist >= current.rect.distanceSquaredTo(p)) {
                // if the current point is closer set it to the current
                if (Double.compare(currentPoint.distanceSquaredTo(p), nearestdist)
                        < 0) {
                    nearest = currentPoint;

                }

                int axis = depth % current.cords.length;
                int compareResult = Double.compare(current.cords[axis], axes[axis]);
                if (compareResult > 0) {
                    nearest = nearest(p, current.left, nearest, depth + 1, axes);
                    nearest = nearest(p, current.right, nearest, depth + 1, axes);
                }
                else {
                    nearest = nearest(p, current.right, nearest, depth + 1, axes);
                    nearest = nearest(p, current.left, nearest, depth + 1, axes);
                }
            }
        }
        return nearest;
    }

    public static void main(String[] args) {

        KdTree kd = new KdTree();

        System.out.println(kd.range(new RectHV(0, 0, 1, 1)));


    }
}
