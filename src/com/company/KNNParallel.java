package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class KNNParallel extends KNN {
    private final int nw;

    public KNNParallel(List<float[]> readPoints, int nw) {
        super(readPoints);
        this.nw = nw;
    }

    @Override
    public void compute(int k) {
        initialize(k);
        var indexes = distributeIndex(nw);
        computeDistancesInParallel(indexes);
    }


    private void processCreation(final List<List<Integer>> indexes, Addition action) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(indexes.size());
        for (var index : indexes) {
            Runnable r = () -> computeDistances(index, action);
            executor.submit(r);
            //System.out.print("☻");
        }
        executor.shutdown();
        System.out.println(executor.awaitTermination(10, TimeUnit.HOURS));
        if (!executor.shutdownNow().isEmpty()) {
            throw new Error("Some threads has not been terminated");
        }
    }


    private void computeDistancesInParallel(final List<List<Integer>> indexes) {
        try {
            var start = System.currentTimeMillis();
            processCreation(indexes, (p, i) -> forward(i, p));
            System.out.println("av-> " + (System.currentTimeMillis() - start));
            start = System.currentTimeMillis();
            indexes.get(0).set(0, readPoints.size() - 1);
            processCreation(indexes, (p, i) -> backward(i, p));
            System.out.println("in-> " + (System.currentTimeMillis() - start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void computeDistances(List<Integer> rowIndexes, Addition action) {
        for (int i : rowIndexes) {
            Point pi = knn.get(i);
            action.execute(pi, i);
        }
    }

    protected void forward(int i, Point pi) {
        for (int j = i + 1; j < knn.size(); ++j) {
            Point pj = knn.get(j);
            float d = eucledeanDistance(pi.getCoordinates(), pj.getCoordinates());
            pi.insertANeighbour(pj, d);
        }
    }

    protected void backward(int i, Point pi) {
        for (int j = i - 1; j >= 0; --j) {
            Point pj = knn.get(j);
            float d = eucledeanDistance(pi.getCoordinates(), pj.getCoordinates());
            pi.insertANeighbour(pj, d);
        }
    }

    private List<List<Integer>> distributeIndex(int nw) {
        List<List<Integer>> indexIntervall = new ArrayList<>(nw);
        int size = readPoints.size();
        for (int i = 0; i < nw; i++) {
            indexIntervall.add(new ArrayList<>(size / nw));
        }
        for (int i = 0; i < size / 2; ++i) {
            indexIntervall.get(i % indexIntervall.size()).add(i);
            int rev = size - 2 - i;
            if (rev != i) {
                indexIntervall.get(i % indexIntervall.size()).add(rev);
            }
        }
        indexIntervall.sort((o1, o2) -> o2.size() - o1.size());
        for (int i = 0; i < indexIntervall.size(); ++i) {
            var q1 = indexIntervall.get(i);
            var q2 = indexIntervall.get(indexIntervall.size() - i - 1);
            if (q1.size() - 1 > q2.size()) {
                var e = q1.remove(q1.size() - 1);
                q2.add(e);
            } else {
                break;
            }
        }
        return indexIntervall;
    }
}
