package com.company;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class KNN {

    protected List<Point> knn;
    protected final List<float[]> readPoints;

    public KNN(List<float[]> readPoints) {
        this.readPoints = readPoints;
    }

    public void compute(int k) {
        initialize(k);
        forward();
        backward();
        /*for (int i = 0; i < readPoints.size() - 1; ++i) {
            Point pi = knn.get(i);
            for (int j = i + 1; j < readPoints.size(); ++j) {
                Point pj = knn.get(j);
                float d = eucledeanDistance(readPoints.get(i), readPoints.get(j));
                pi.insertANeighbour(pj, d);
                pj.insertANeighbour(pi, d);
            }
        }*/
    }

    private void forward() {
        for (int i = 0; i < knn.size() - 1; ++i) {
            Point pi = knn.get(i);
            for (int j = i + 1; j < knn.size(); ++j) {
                Point pj = knn.get(j);
                float d = eucledeanDistance(pi.getCoordinates(), pj.getCoordinates());
                pi.insertANeighbour(pj, d);
            }
        }
    }

    private void backward() {
        for (int i = readPoints.size() - 1; i >= 0; --i) {
            Point pi = knn.get(i);
            for (int j = i - 1; j >= 0; --j) {
                Point pj = knn.get(j);
                float d = eucledeanDistance(pi.getCoordinates(), pj.getCoordinates());
                pi.insertANeighbour(pj, d);
            }
        }
    }

    protected void initialize(int k) {
        knn = new ArrayList<>(readPoints.size());
        for (int i = 0; i < readPoints.size(); ++i) {
            knn.add(new Point(i + 1, readPoints.get(i)[0], readPoints.get(i)[1], k));
        }
    }

    protected float eucledeanDistance(float[] p1, float[] p2) {
        float d = (float) (Math.pow(p2[0] - p1[0], 2) + Math.pow(p2[1] - p1[1], 2));
        return (float) Math.sqrt(d);
    }

    public void writeKNeighbours() {
        try {
            FileWriter myWriter = new FileWriter(getClass().getSimpleName() + "Results.txt");
            var knnLen=knn.size();
            knn.forEach(point -> {
                try {
                    myWriter.write("%5d".formatted(point.getID()) + "->" + queueToString(point.getNeighbours()) + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private String queueToString(Queue<Pair<Point, Float>> neigh) {
        StringBuilder sb = new StringBuilder();
        neigh.forEach(n -> {
            if (Objects.isNull(n)) {
                System.out.println(neigh);
                throw new Error();
            }
            sb.append("{").append("%5d".formatted(n.getKey().getID())).append(" ").append("%.2f".formatted(n.getVal())).append("}");
        });
        return sb.toString();
    }

    public void hasSameNeighbours(List<Point> otherKNN) {
        for (int i = 0; i < knn.size(); i++) {
            var s1 = new HashSet<>(knn.get(i).getNeighbours());
            var s2 = new HashSet<>(otherKNN.get(i).getNeighbours());
            boolean same = s1.equals(s2);
            if (!same) {
                throw new Error("Points at " + i + " have not the same neighbours");
            }
        }
    }
}
