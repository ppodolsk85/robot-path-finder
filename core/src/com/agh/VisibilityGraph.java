package com.agh;

import java.util.ArrayList;
import java.util.List;

public class VisibilityGraph {
    List<Point> vertices = new ArrayList<Point>();
    List<Segment> segments = new ArrayList<Segment>();

    public VisibilityGraph(Point start) {
        vertices.add(start);
    }

    public void addIfNotExists(Segment s) {
        Point first = s.getLeftPoint();
        Point second = s.getRightPoint();
        if (!vertices.contains(first)) {
            vertices.add(first);
        }
        if (!vertices.contains(second)) {
            vertices.add(second);
        }
        if (!segments.contains(s) && !segments.contains(new Segment(s.getRightPoint(), s.getLeftPoint()))) {
            segments.add(s);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("VisibilityGraph{vertices={");
        for (Point vertex: vertices) {
            sb.append(vertex.toString());
        }
        sb.append("}, segments={");
        for (Segment segment: segments) {
            sb.append(segment.toString());
        }
        sb.append("}\n");
        sb.append(String.format("Vertices count: %d Segment count: %d\n", vertices.size(), segments.size()));
        return sb.toString();
    }

}
