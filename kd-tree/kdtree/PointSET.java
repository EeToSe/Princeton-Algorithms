import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> PointTS;

    // construct an empty set of points
    public PointSET() {
        this.PointTS = new TreeSet<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return PointTS.isEmpty();
    }

    // number of points in the set
    public int size() {
        return PointTS.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        // check if point p in the tree
        if (p == null) {
            throw new IllegalArgumentException();
        }
        PointTS.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return PointTS.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point : PointTS) {
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        for (Point2D point : PointTS) {
            if (rect.contains(point)) {
                points.add(point);
            }
        }
        return points;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (PointTS.size() == 0) {
            return null;
        }
        Point2D ans = PointTS.first();
        Double champion = p.distanceSquaredTo(ans);
        for (Point2D point : PointTS) {
            Double distance = p.distanceSquaredTo(ans);
            if (distance < champion) {
                champion = distance;
                ans = point;
            }
        }
        return ans;
    }
}
