package com.company;

import java.util.ArrayList;
import java.util.List;

public class KNN {

    private List<Point> knn;
    private final List<float[]> readPoints;

    public KNN(List<float[]> readPoints) {
        this.readPoints = readPoints;
    }

    private float[][] computeMatrix() {
        float[][] adj = new float[readPoints.size() - 1][];
        for (int i = 0; i < readPoints.size() - 1; ++i) {
            float[] row = new float[readPoints.size() - 1 - i];
            for (int j = i + 1; j < readPoints.size(); ++j) {
                row[j - i - 1] = eucledeanDistance(readPoints.get(i), readPoints.get(j));
            }
            adj[i] = row;
        }
        return adj;
    }

    public void compute(int k) {
        initialize(k);
        float[][] m = computeMatrix();
        for (int i = 0; i < m.length; ++i) {
            Point pi = knn.get(i);
            var start = System.nanoTime();
            for (int j = 0; j < m[i].length; ++j) {
                Point pj = knn.get(j + i + 1);
                pi.insertANeighbour(new Pair<>(pj, m[i][j]), "{a}");
                pj.insertANeighbour(new Pair<>(pi, m[i][j]), "{i}");
            }
            var elapsed = System.nanoTime() - start;
            System.out.printf(" i= %4d, len= %4d, t=%8d \n", i, m[i].length, elapsed);
        }

    }

    private void initialize(int k) {
        knn = new ArrayList<>(readPoints.size());
        for (int i = 0; i < readPoints.size(); ++i) {
            knn.add(new Point(i + 1, readPoints.get(i)[0], readPoints.get(i)[1], k));
        }
    }

    private float eucledeanDistance(float[] p1, float[] p2) {
        float d = (float) (Math.pow(p2[0] - p1[0], 2) + Math.pow(p2[1] - p1[1], 2));
        return (float) Math.sqrt(d);
    }
}
