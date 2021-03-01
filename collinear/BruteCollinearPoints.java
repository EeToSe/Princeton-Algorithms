import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
   private final int pointsNum; // number of total points
   private final Point[] points;
   private LineSegment[] collinearPoints; // array to store LineSegment

   // finds all line segments containing 4 points
   public BruteCollinearPoints(Point[] points) {

      // corner case
      if (points == null) {
          throw new IllegalArgumentException("array pointer is null");
      }

      pointsNum = points.length;
      this.points = Arrays.copyOf(points, pointsNum); // defensive copy of Points array
      for (int i = 0; i < pointsNum; i++) {
        if (this.points[i] == null) {
            throw new IllegalArgumentException("array elements contain null");
        }
      }

      Arrays.sort(this.points); // necessary for checking duplicates

      for (int i = 0; i < pointsNum - 1; i++) {
        for (int j = i+1; j < pointsNum; j++) {
          if (points[i].slopeTo(points[j]) == Double.NEGATIVE_INFINITY) {
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
     // the line segments
     ArrayList<LineSegment> segmentsList = new ArrayList<LineSegment>(); // temporary segment list to append
     collinearPoints = new LineSegment[pointsNum];
     // Brute force every 4 points
     for (int i = 0; i < pointsNum - 3; i++) {
       for (int j = i + 1; j < pointsNum - 2; j++) {
         for (int k = j + 1; k < pointsNum -1; k++) {
           for (int m = k + 1; m < pointsNum; m++) {
             if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[k]) &&
                 points[i].slopeTo(points[j]) == points[i].slopeTo(points[m])) {
                   LineSegment newElement = new LineSegment(points[i], points[m]);
                   segmentsList.add(newElement);
             }
           }
         }
       }
     }
     collinearPoints = segmentsList.toArray(new LineSegment[segmentsList.size()]);
     return Arrays.copyOf(collinearPoints, collinearPoints.length);
     // LineSegment[] tmp = Arrays.copyOf(collinearPoints, collinearPoints.length);
     // return tmp;
   }
}
