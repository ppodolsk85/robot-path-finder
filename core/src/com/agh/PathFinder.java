package com.agh;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.ConvexHull;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.FloatArray;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PathFinder extends ApplicationAdapter {
    private Stage stage;
    private BitmapFont font;
    private Skin skin;
    private TextureAtlas buttonAtlas;
    private GuiManager guiManager;
    private Point start;
    private Point end;
    private List<Polygon> obstacles = new ArrayList<Polygon>();
    private List<Polygon> obstaclesAfterMinkowskiSum = new ArrayList<Polygon>();
    private List<Segment> allSegments = new ArrayList<Segment>();
    private List<Segment> segmentsAfterMinkowskiSum = new ArrayList<Segment>();
    private List<Point> obstaclesVertices = new ArrayList<Point>();
    private List<Point> obstaclesAfterMinkowskiSumVertices = new ArrayList<Point>();
    private VisibilityGraph visibilityGraph;
    private int radius = 2;
    private Polygon robot;
    private ConvexHull convexHull = new ConvexHull();

    @Override
    public void create() {
        stage = new Stage();
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        guiManager = new GuiManager();

        font = new BitmapFont();
        skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("data/uiskin.atlas"));
        skin.addRegions(buttonAtlas);

        createButtons();
        createSliders();

        createTestData();
        createVisibilityGraph();
        System.out.println(visibilityGraph.toString());

        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }

    private void createVisibilityGraph() {
        visibilityGraph = new VisibilityGraph(start);
        SimpleWeightedGraph<Point, Segment> g = visibilityGraph.getGraph();
        g.addVertex(start);
        g.addVertex(end);
        addFirstOrLastSegments(start);
        addFirstOrLastSegments(end);
        addConnectionsBetweenObstacles();
        List shortest_path = DijkstraShortestPath.findPathBetween(g, start, end);
        System.out.println(shortest_path);
    }

    private void addConnectionsBetweenObstacles() {
//        for (Point p1 : obstaclesVertices) {
        for (Point p1 : obstaclesAfterMinkowskiSumVertices) {
            for (Polygon obstacle : obstaclesAfterMinkowskiSum) {
                for (Point p2 : obstacle.getPoints()) {
                    if (p1.equals(p2)) {
                        continue;
                    }
                    Segment s = new Segment(p1, p2);
                    boolean intersects = false;
//                    for (Segment seg : allSegments) {
                    for (Segment seg : segmentsAfterMinkowskiSum) {
                        if (Segment.areCurrentlyConnected(s, seg)) {
                            continue;
                        }
                        if (Segment.intersects(s, seg)) {
                            intersects = true;
                        }
                    }
                    if (!intersects) {
                        visibilityGraph.addIfNotExists(s);
                    }
                }
            }
        }
    }

    private void addFirstOrLastSegments(Point point) {
//        for (Polygon obstacle : obstacles) {
        for (Polygon obstacle : obstaclesAfterMinkowskiSum) {
            for (Point p : obstacle.getPoints()) {
                Segment s = new Segment(p, point);
                boolean intersects = false;
//                for (Segment seg : allSegments) {
                for (Segment seg : segmentsAfterMinkowskiSum) {
                    if (Segment.areCurrentlyConnected(s, seg)) {
                        continue;
                    }
                    if (Segment.intersects(s, seg)) {
                        intersects = true;
                    }
                }
                if (!intersects) {
                    visibilityGraph.addIfNotExists(s);
                }
            }
        }
    }

    private void createTestData() {
        start = new Point(-80.0d, 20.0d);
        end = new Point(150.0d, -50.0d);
        System.out.println(start);
        System.out.println(end);
        List<Point> firstPolygonPoints = Arrays.asList(new Point(-50.0d, -30.0d),
                new Point(60.0d, -10.0d),
                new Point(10.0d, 40.0d));
        List<Point> secondPolygonPoints = Arrays.asList(new Point(70.0d, 20.0d),
                new Point(80.0d, -70.0d),
                new Point(110.0d, 50.0d));
        Polygon firstPolygon = new Polygon(firstPolygonPoints);
        Polygon secondPolygon = new Polygon(secondPolygonPoints);
        obstacles.addAll(Arrays.asList(firstPolygon, secondPolygon));
        for (Polygon p : obstacles) {
            System.out.println("Polygon:");
            for (Segment s : p.getSegments()) {
                System.out.println(s);
            }
            obstaclesVertices.addAll(p.getPoints());
            allSegments.addAll(p.getSegments());
        }
        List<Point> robotPoints = new ArrayList<Point>();
        for (int i = 0; i < 8; i++) {
            double phi = i * Math.PI / 4;
            robotPoints.add(new Point(radius * Math.cos(phi), radius * Math.sin(phi)));
        }
        robot = new Polygon(robotPoints);
        performMinkowskiSum();
    }

    private void performMinkowskiSum() {
        for (Polygon obstacle : obstacles) {
            System.out.println(obstacle.getPoints());
            FloatArray minkowskiSumPoints = new FloatArray();
            List<Point> newPoints = new ArrayList<Point>();
            for (Point obstaclePoint : obstacle.getPoints()) {
                for (Point robotPoint : robot.getPoints()) {
                    minkowskiSumPoints.add((float) (obstaclePoint.getX() + robotPoint.getX()));
                    minkowskiSumPoints.add((float) (obstaclePoint.getY() + robotPoint.getY()));
                }
            }
            FloatArray hull = convexHull.computePolygon(minkowskiSumPoints, false);
            for (int i = 0; i < hull.size; i+= 2) {
                newPoints.add(new Point((double) hull.get(i), (double) hull.get(i + 1)));
            }
            Polygon newObstacle = new Polygon(newPoints);
            obstaclesAfterMinkowskiSum.add(newObstacle);
            obstaclesAfterMinkowskiSumVertices.addAll(newObstacle.getPoints());
            segmentsAfterMinkowskiSum.addAll(newObstacle.getSegments());
        }
    }

    private void createButtons() {
        createButton(0, 0, 100, 50, "Set robot", new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                guiManager.setAddStartState();
            }
        });

        createButton(0, 50, 100, 50, "Set finish", new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                guiManager.setAddEndState();
            }
        });

        createButton(0, 100, 100, 50, "Add polygon", new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                guiManager.setAddPolygonState();
            }
        });

        createButton(0, 400, 100, 50, "Start animation", new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //TODO: compute and show animation
            }
        });
    }

    private void createButton(int x, int y, int width, int height, String text, EventListener listener) {
        TextButton.TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("default-rect");
        textButtonStyle.down = skin.getDrawable("default-rect-down");
        textButtonStyle.checked = skin.getDrawable("default-pane");
        TextButton button = new TextButton(text, textButtonStyle);
        button.setPosition(x, y);
        button.setWidth(width);
        button.setHeight(height);
        button.addListener(listener);
        stage.addActor(button);
    }

    private void createSliders() {
        createSlider(0, 100, 1, false, 0, 175, 100, "Robot size", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("GuiManager", Float.toString(((Slider) actor).getValue()));
            }
        });
    }

    private void createSlider(float min, float max, float stepSize, boolean vertical, int x, int y, int width, String description, EventListener listener) {
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = skin.getDrawable("default-slider");
        sliderStyle.knob = skin.getDrawable("default-slider-knob");

        Slider slider = new Slider(min, max, stepSize, vertical, sliderStyle);
        slider.setPosition(x, y);
        slider.setWidth(width);
        slider.addListener(listener);
        stage.addActor(slider);
    }


    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }
}
