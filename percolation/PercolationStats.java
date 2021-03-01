import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

// this version for index start from 0
public class PercolationStats {
    private double[] est; // array of esimates
    private Percolation pl; // init the percolation system
    private final int trailNum; // number of trials

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("invaild n or trails");
        }
        this.est = new double[trials];
        this.trailNum = trials;
        for (int trail = 0; trail < trials; trail++) {
            this.pl = new Percolation(n);
            int openedSites = 0;
            while (!pl.percolates()) {
                // choose a site uniformly at random among all blocked sites
                int row = StdRandom.uniform(0, n);
                int col = StdRandom.uniform(0, n);
                if (!pl.isOpen(row, col)) {
                    pl.open(row, col);
                    openedSites++;
                }
            }
            double fraction = (double) (openedSites) / (n * n);
            est[trail] = fraction;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(est);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(est);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean() - 1.96 * this.stddev() / Math.sqrt(trailNum);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean() + 1.96 * this.stddev() / Math.sqrt(trailNum);
    }

    // test client
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);
        StdOut.println("mean                 = " + ps.mean());
        StdOut.println("stddev               =" + ps.mean());
        String interval = "[" + ps.confidenceLo() + "," + ps.confidenceHi() + "]";
        StdOut.println("95% confidence interval =" + interval);
    }
}
