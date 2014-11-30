package com.agh;

import java.awt.geom.Line2D;

public class Segment {
    private Point leftPoint;
    private Point rightPoint;
    private Polygon polygon;

    public Segment(Point leftPoint, Point rightPoint) {
        this.leftPoint = leftPoint;
        this.rightPoint = rightPoint;
    }

    public Point getLeftPoint() {
        return leftPoint;
    }

    public void setLeftPoint(Point leftPoint) {
        this.leftPoint = leftPoint;
    }

    public Point getRightPoint() {
        return rightPoint;
    }

    public void setRightPoint(Point rightPoint) {
        this.rightPoint = rightPoint;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public static boolean intersects(Segment first, Segment second) {
        return Line2D.linesIntersect(first.getLeftPoint().getX(), first.getLeftPoint().getY(),
                first.getRightPoint().getX(), first.getRightPoint().getY(),
                second.getLeftPoint().getX(), second.getLeftPoint().getY(),
                second.getRightPoint().getX(), second.getRightPoint().getY());
    }

    @Override
    public String toString() {
        return "Segment{" +
                "leftPoint=" + leftPoint +
                ", rightPoint=" + rightPoint +
                '}';
    }

    public static boolean areCurrentlyConnected(Segment first, Segment second) {
        if (first.getLeftPoint().equals(second.getLeftPoint()) || first.getRightPoint().equals(second.getRightPoint())) {
            return true;
        }
        if (first.getLeftPoint().equals(second.getRightPoint()) || first.getRightPoint().equals(second.getLeftPoint())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Segment segment = (Segment) o;

        if (!leftPoint.equals(segment.leftPoint)) return false;
        if (!rightPoint.equals(segment.rightPoint)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = leftPoint.hashCode();
        result = 31 * result + rightPoint.hashCode();
        return result;
    }
}
