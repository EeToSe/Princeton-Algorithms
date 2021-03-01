import java.util.Arrays;
import java.util.ArrayList;

public class FastCollinearPoints {
   private final int pointsNum; // number of total points
   private final Point[] points; // copy of input points
   private LineSegment[] collinearPoints; // array of line segments

   public FastCollinearPoints(Point[] points) {

      // Corner case check
      if (points == null) {
          throw new IllegalArgumentException("array pointer is null");
      }

      pointsNum = points.length;
      this.points = Arrays.copyOf(points, pointsNum);

      for (int i = 0; i < pointsNum; i++) {
        if (this.points[i] == null) {
            throw new IllegalArgumentException("array elements contain null");
        }
      }

      Arrays.sort(this.points); // necessary to check duplicates
      for (int i = 0; i < pointsNum - 1; i++) {
        for (int j = i + 1; j < pointsNum; j++) {
          if (this.points[i].slopeTo(this.points[j]) == Double.NEGATIVE_INFINITY) {
            throw new IllegalArgumentException("duplicates");
          }
        }
      }
   }

   public           int numberOfSegments() {
      // the number of line segments
      return collinearPoints.length;
   }

   public LineSegment[] segments() {
      ArrayList<LineSegment> segmentsList = new ArrayList<LineSegment>();
      for (int i = 0; i < pointsNum; i++) {
          if (pointsNum < 4) {
            // total points < 4, no need to find collinearPoints
            break;
          }
          Point p = points[i]; // think of p as the origin;
          Point[] pointsOrder = Arrays.copyOf(points, pointsNum); // defensive copy
          Arrays.sort(pointsOrder, p.slopeOrder());  // sort by Comparator
          int begin = 1;
          int end = 1;
          double prev = p.slopeTo(pointsOrder[end]); // first slope
          // start from index 1, exclude the origin point
          for (int j = 2; j < pointsNum; j++) {
            double currentSlope = p.slopeTo(pointsOrder[j]);
            if (currentSlope != prev) {
              if (end - begin >= 2) {
                    // filter the subsegments
                    if (p.compareTo(pointsOrder[begin]) < 0) {
                        segmentsList.add(new LineSegment(p, pointsOrder[end]));
                    }
              }
              prev = currentSlope;
              begin = j;
            }
            end = j;
          }
          if (end - begin >= 2) {
            if (p.compareTo(pointsOrder[begin]) < 0) {
              segmentsList.add(new LineSegment(p, pointsOrder[end]));
            }
          }
      }
      collinearPoints = segmentsList.toArray(new LineSegment[segmentsList.size()]);
      return Arrays.copyOf(collinearPoints, collinearPoints.length);
      // return collinearPoints;
   }
}
