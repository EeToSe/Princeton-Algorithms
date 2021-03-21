import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {
    private KdNode root; // root of KdTree
    private int size; // size of KdTree

    // Helper data structure to denote KdTree node
    private static class KdNode {
        private final Point2D p; // 2d coordinates
        private RectHV rect; // the axis-aligned rectangle corresponding to this node
        private KdNode lb; // the left/bottom subtree
        private KdNode rf; // the right/top subtree

        public KdNode(Point2D p) {
            this.p = p;
        }
    }

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point and orientation to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            root = new KdNode(p, 0);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        current
    }

    // draw all points to standard draw
    public void draw()

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect)

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p)
}
