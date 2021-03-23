import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {
    private KdNode root; // root of KdTree
    private int size; // size of KdTree
    private Point2D winnerPoint; // nearest point found
    private double champion;
    private int num;

    // Helper data structure to denote KdTree node
    private static class KdNode {
        private final Point2D p; // 2d coordinates
        private final int level; // the height of node
        private RectHV rect; // the axis-aligned rectangle corresponding to this node
        private KdNode lb; // the left/bottom subtree
        private KdNode rt; // the right/top subtree

        public KdNode(Point2D p, int level) {
            this.p = p;
            this.level = level;
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
            throw new IllegalArgumentException("calls insert with a null point");
        }

        // init the root node with rect(0,0,1,1)
        if (root == null) {
            size++;
            root = new KdNode(p, 0);
            root.rect = new RectHV(0, 0, 1, 1);
        }
        // insert and increase size by 1 only when p is not included
        if (!contains(p)) {
            insert(root, p);
            size++;
        }
    }

    private void insert(KdNode x, Point2D p) {
        int cmp = compare(x, p);
        if (cmp < 0) {
            // insert point p into x.lb
            if (x.lb == null) {
                x.lb = new KdNode(p, x.level + 1);
                if (x.level % 2 == 0)
                    x.lb.rect = new RectHV(x.rect.xmin(), x.rect.ymin(), x.p.x(), x.rect.ymax());
                else x.lb.rect = new RectHV(x.rect.xmin(), x.rect.ymin(), x.rect.xmax(), x.p.y());
            }
            else {
                insert(x.lb, p);
            }
        }

        else {
            if (x.rt == null) {
                x.rt = new KdNode(p, x.level + 1);
                if (x.level % 2 == 0)
                    x.rt.rect = new RectHV(x.p.x(), x.rect.ymin(), x.rect.xmax(), x.rect.ymax());
                else x.rt.rect = new RectHV(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.rect.ymax());
            }
            else {
                insert(x.rt, p);
            }
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("calls contains with a null point");
        }
        // use private helper recursion methods
        return contains(p, root);
    }

    private boolean contains(Point2D p, KdNode node) {
        if (node == null) {
            return false;
        }
        else if (p.equals(node.p)) {
            return true;
        }
        else {
            if (compare(node, p) < 0) {
                return contains(p, node.lb);
            }
            else {
                return contains(p, node.rt);
            }
        }
    }

    // draw all points to standard draw
    public void draw() {
        // 清空画布
        StdDraw.clear();
        // 递归调用
        draw(root);
    }

    private void draw(KdNode node) {
        if (node != null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            node.p.draw();

            // set red or blue based on the level of node
            if (node.level % 2 == 0) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
            }

            draw(node.lb);
            draw(node.rt);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }
        // recursively implement range
        return range(rect, root);
    }

    private ArrayList<Point2D> range(RectHV rect, KdNode node) {
        ArrayList<Point2D> points = new ArrayList<Point2D>();

        if (node != null && rect.intersects(node.rect)) {
            // 偷懒做法 appends all of the elements returned to the end
            points.addAll(range(rect, node.lb));
            points.addAll(range(rect, node.rt));
            if (rect.contains(node.p)) {
                points.add(node.p);
            }
        }
        return points;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }
        num = 0;
        // search root node, init champion and winnerPoint
        champion = Double.POSITIVE_INFINITY;
        nearest(p, root);
        return winnerPoint;
    }

    private void nearest(Point2D p, KdNode node) {
        if (node == null) {
            return;
        }

        if (node.rect.distanceSquaredTo(p) < champion) {
            double dist = p.distanceSquaredTo(node.p);

            if (dist < champion) {
                champion = dist;
                winnerPoint = node.p;
            }

            if (node.lb != null && node.lb.rect.contains(p)) {
                nearest(p, node.lb);
                if (node.rt.rect.distanceSquaredTo(p) < champion) {
                    nearest(p, node.rt);
                }
            }

            else if (node.rt != null && node.rt.rect.contains(p)) {
                nearest(p, node.rt);
                if (node.lb.rect.distanceSquaredTo(p) < champion) {
                    nearest(p, node.lb);
                }
            }

            else {
                double distlb = node.lb != null ? node.lb.rect.distanceSquaredTo(p) :
                                Double.POSITIVE_INFINITY;
                double distrt = node.rt != null ? node.rt.rect.distanceSquaredTo(p) :
                                Double.POSITIVE_INFINITY;
                if (distlb <= distrt) {
                    nearest(p, node.lb);
                    if (node.rt != null && node.rt.rect.distanceSquaredTo(p) < champion) {
                        nearest(p, node.rt);
                    }
                }
                else {
                    nearest(p, node.rt);
                    if (node.lb != null && node.lb.rect.distanceSquaredTo(p) < champion) {
                        nearest(p, node.lb);
                    }
                }

            }
            // if (node.rect.distanceSquaredTo(p) < champion) {
            //     double dist = p.distanceSquaredTo(node.p);
            //
            //     if (dist < champion) {
            //         champion = dist;
            //         winnerPoint = node.p;
            //     }
            //
            //     // pruning progress: decide which
            //     if (node.lb != null && node.lb.rect.contains(p)) {
            //         nearest(p, node.lb);
            //         nearest(p, node.rt);
            //     }
            //     else if (node.rt != null && node.rt.rect.contains(p)) {
            //         nearest(p, node.rt);
            //         nearest(p, node.lb);
            //     }
            //     else {
            //         double distlb = node.lb != null ? node.lb.rect.distanceSquaredTo(p) :
            //                         Double.POSITIVE_INFINITY;
            //         double distrt = node.rt != null ? node.rt.rect.distanceSquaredTo(p) :
            //                         Double.POSITIVE_INFINITY;
            //         if (distlb <= distrt) {
            //             nearest(p, node.lb);
            //             nearest(p, node.rt);
            //         }
            //         else {
            //             winnerPoint = nearest(p, node.rt);
            //             winnerPoint = nearest(p, node.lb);
            //         }
            //     }
            // }
            // return winnerPoint;
        }
    }


    // compare point p with parent, calculate the discriminator based on height of parent node
    // @return value: 1 - go left/bottom; -1 - go right/top; 0 - same point
    private int compare(KdNode parent, Point2D p) {
        int discriminator = parent.level % 2;
        if (discriminator == 0) {
            // even case, compare by x-coordinate
            if (Double.compare(p.x(), parent.p.x()) == 0) {
                return Double.compare(p.y(), parent.p.y());
            }
            return Double.compare(p.x(), parent.p.x());
        }
        else {
            // odd case, compare by y-coordinate
            return Double.compare(p.y(), parent.p.y());
        }
    }

    public static void main(String[] args) {
        // // initialize the data structures from file
        // String filename = args[0];
        // In in = new In(filename);
        // KdTree kdtree = new KdTree();
        // while (!in.isEmpty()) {
        //     double x = in.readDouble();
        //     double y = in.readDouble();
        //     Point2D p = new Point2D(x, y);
        //     kdtree.insert(p);
        // }
        // RectHV searchRect = new RectHV(0, 0.5, 1, 1);
        // ArrayList list = (ArrayList) kdtree.range(searchRect);
        KdTree kt = new KdTree();
        Point2D query = new Point2D(0.91, 0.6);
        Point2D pA = new Point2D(0.7, 0.2);
        Point2D pB = new Point2D(0.5, 0.4);
        Point2D pC = new Point2D(0.2, 0.3);
        Point2D pD = new Point2D(0.4, 0.7);
        Point2D pE = new Point2D(0.9, 0.6);
        kt.insert(pA);
        kt.insert(pB);
        kt.insert(pC);
        kt.insert(pD);
        kt.insert(pE);

        Point2D p = kt.nearest(query);
        System.out.println(p);
        //System.out.println(kdtree.num);
        //kdtree.draw();
    }
}
