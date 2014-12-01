package com.agh;

import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.List;

public class VisibilityGraph {
    private List<Point> vertices = new ArrayList<Point>();
    private List<Segment> segments = new ArrayList<Segment>();
    private SimpleWeightedGraph<Point, Segment> graph = new SimpleWeightedGraph<Point, Segment>(Segment.class);

    public VisibilityGraph(Point start) {
        vertices.add(start);
    }

    public void addIfNotExists(Segment s) {
        Point first = s.getLeftPoint();
        Point second = s.getRightPoint();
        if (!vertices.contains(first)) {
            graph.addVertex(first);
            vertices.add(first);
        }
        if (!vertices.contains(second)) {
            graph.addVertex(second);
            vertices.add(second);
        }
        if (!segments.contains(s) && !segments.contains(new Segment(s.getRightPoint(), s.getLeftPoint()))) {
            Segment segment = graph.addEdge(first, second);
            segment.setLeftPoint(first);
            segment.setRightPoint(second);
            graph.setEdgeWeight(segment, Math.sqrt((first.getX() - second.getX()) * (first.getX() - second
                    .getX()) + (first
                    .getY() - second.getY()) * (first.getY() - second.getY())));
            segments.add(s);
        }
    }

    @Override
    public String toString() {
        /*StringBuilder sb = new StringBuilder("VisibilityGraph{vertices={");
        for (Point vertex : vertices) {
            sb.append(vertex.toString());
        }
        sb.append("}, segments={");
        for (Segment segment : segments) {
            sb.append(segment.toString());
        }
        sb.append("}\n");
        sb.append(String.format("Vertices count: %d Segment count: %d\n", vertices.size(), segments.size()));*/
        StringBuilder sb = new StringBuilder();
        for (Point vertex : vertices) {
            sb.append(vertex.getX()).append(" ").append(vertex.getY()).append('\n');
        }
        return sb.toString();
    }

    public SimpleWeightedGraph<Point, Segment> getGraph() {
        return graph;
    }
}
