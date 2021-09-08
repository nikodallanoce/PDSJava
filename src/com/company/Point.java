package com.company;

import java.util.*;

public class Point {

    public int getID() {
        return ID;
    }

    private final int ID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return ID == point.ID && Arrays.equals(coordinates, point.coordinates);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(ID);
        result = 31 * result + Arrays.hashCode(coordinates);
        return result;
    }

    public float[] getCoordinates() {
        return coordinates;
    }

    private final float[] coordinates;

    public Queue<Pair<Point, Float>> getNeighbours() {
        return neighbours;
    }

    private Queue<Pair<Point, Float>> neighbours;

    public Point(int ID, float x, float y, int k) {
        this.ID = ID;
        this.coordinates = new float[]{x, y};
        initializeNeighbours(k);
    }

    public Point() {
        this.ID = -1;
        this.coordinates = null;
    }

    private void initializeNeighbours(int k) {
        Comparator<Pair<Point, Float>> cmp = (o1, o2) -> Float.compare(o2.getVal(), o1.getVal());
        neighbours = new PriorityQueue<>(k, cmp);
        for (int i = 0; i < k; i++) {
            neighbours.add(new Pair<>(new Point(), Float.MAX_VALUE));
        }
    }

    public void insertANeighbour(Point p, float d) {
        Pair<Point, Float> front = neighbours.peek();
        assert front != null;
        if (d < front.getVal()) {
            neighbours.poll();
            neighbours.add(new Pair<>(p, d));
        }
    }

    @Override
    public String toString() {
        return "Point{" +
                "ID=" + ID +
                ", x,y= " + Arrays.toString(coordinates) +
                "}";
    }

}
