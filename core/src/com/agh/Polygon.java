package com.agh;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private List<Point> points = new ArrayList<Point>();
    private List<Segment> segments = new ArrayList<Segment>();
    private int pointCount;

    public Polygon(List<Point> points) {
        this.points = points;
        this.pointCount = points.size();
        for (int i = 0; i < pointCount - 1; i++) {
            Segment segment = new Segment(points.get(i), points.get(i + 1));
            segment.setPolygon(this);
            segments.add(segment);
        }
        Segment segment = new Segment(points.get(pointCount - 1), points.get(0));
        segment.setPolygon(this);
        segments.add(segment);
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public int getPointCount() {
        return pointCount;
    }

    public void setPointCount(int pointCount) {
        this.pointCount = pointCount;
    }

    @Override
    public String toString() {
        /*return "Polygon{" +
                "points=" + points +
                '}';*/
        StringBuilder sb = new StringBuilder();
        for (Point vertex : points) {
            sb.append(vertex.getX()).append(" ").append(vertex.getY()).append('\n');
        }
        return sb.toString();
    }
}
