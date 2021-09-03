package com.company;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        List<float[]> points = readPoints("points_test.txt");
        //List<float[]> points = generatePoints(6);
        KNN knn = new KNN(points);
        var start = System.currentTimeMillis();
        knn.compute(2);
        System.out.println(System.currentTimeMillis() - start);
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
}
