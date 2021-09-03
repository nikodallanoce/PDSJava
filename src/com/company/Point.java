package com.company;

import java.util.Comparator;
import java.util.Optional;
import java.util.PriorityQueue;

public class Point {

    private final int ID;
    private final float x;
    private final float y;
    private PriorityQueue<Pair<Point, Float>> neighbours;

    public Point(int ID, float x, float y, int k) {
        this.ID = ID;
        this.x = x;
        this.y = y;
        initializeNeighbours(k);
    }

    public Point() {
        this(-1, -1, -1, 0);
    }

    private void initializeNeighbours(int k) {
        if (k > 0) {
            Comparator<Pair<Point, Float>> cmp = (o1, o2) -> Float.compare(o2.getVal(), o1.getVal());
            neighbours = new PriorityQueue<>(k, cmp);
            for (int i = 0; i < k; i++) {
                neighbours.add(new Pair<>(new Point(), Float.MAX_VALUE));
            }
        }
    }

    public void insertANeighbour(Pair<Point, Float> neigh, String print) {
        Pair<Point, Float> front = neighbours.peek();
        System.out.print(print);
        assert front != null;
        if (neigh.getVal() < front.getVal()) {
            neighbours.poll();
            neighbours.add(neigh);
            System.out.print("#");
        }
    }

    @Override
    public String toString() {
        return "Point{" +
                "ID=" + ID +
                ", x,y= [" + x +
                "," + y +
                "]}";
    }
}
