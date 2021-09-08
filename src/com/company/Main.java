package com.company;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) {
        List<float[]> randCoord = generatePoints(20_000);
        writePoints(randCoord);
        List<float[]> points = readPoints("points.txt");
        KNN seqKNN = new KNN(points);
        KNN parKNN = new KNNParallel(points, 8);
        int k = 50;

        var start = System.currentTimeMillis();
        parKNN.compute(k);
        //parKNN.writeKNeighbours();
        var tpar = System.currentTimeMillis() - start;
        System.out.println("par: " + tpar);

        start = System.currentTimeMillis();
        seqKNN.compute(k);
        //seqKNN.writeKNeighbours();
        var tseq = System.currentTimeMillis() - start;
        System.out.println("seq: " + tseq);
        System.out.println("spdup: " + (double) tseq / tpar);

        seqKNN.hasSameNeighbours(parKNN.knn);
    }

    public static List<float[]> readPoints(String fileName) {
        List<float[]> points = new LinkedList<>();
        Path path = Paths.get(fileName);
        try (Scanner scanner = new Scanner(path)) {
            while (scanner.hasNextLine()) {
                var line = scanner.nextLine();
                var coord = line.split(",");
                points.add(new float[]{Float.parseFloat(coord[0]), Float.parseFloat(coord[1])});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(points);
    }

    private static List<float[]> generatePoints(int n) {
        List<float[]> points = new ArrayList<>(n);
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            points.add(new float[]{randomFloat(r, 1f, 10f), randomFloat(r, 1f, 10f)});
        }
        return points;
    }

    private static float randomFloat(Random r, float min, float max) {
        return min + r.nextFloat() * (max - min);
    }

    private static void writePoints(List<float[]> points) {
        try {
            FileWriter myWriter = new FileWriter("points.txt");
            points.stream().parallel().forEach(coord -> {
                try {
                    myWriter.write(coord[0] + "," + coord[1] + "\n");
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
}
