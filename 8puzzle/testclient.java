import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
public class testclient {
  public static void main(String[] args) {

      // create initial board from file
      In in = new In(args[0]);
      int n = in.readInt();
      int[][] tiles = new int[n][n];
      for (int i = 0; i < n; i++)
          for (int j = 0; j < n; j++)
              tiles[i][j] = in.readInt();
      Board initial = new Board(tiles);

      // solve the puzzle
      Solver solver = new Solver(initial);

      // print solution to standard output
      if (!solver.isSolvable())
          StdOut.println("No solution possible");
      else {
          StdOut.println("Minimum number of moves = " + solver.moves());
          for (Board board : solver.solution())
              StdOut.println(board);
      }
      // test if solver is immutable
      StdOut.println("moves changed? " + solver.moves());
      StdOut.println("isSolvable? " + solver.isSolvable());
      for (Board board : solver.solution())
          StdOut.println(board);
  }
}
