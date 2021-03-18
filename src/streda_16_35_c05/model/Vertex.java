package streda_16_35_c05.model;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;

import java.util.Optional;

public class Vertex {

    private final Point3D point;
    private final Col color;
    private final Vec2D textCoord;
    private final double one;

    public Vertex(Point3D point, Col color) {
        this.point = point;
        this.color = color;
        this.textCoord = new Vec2D(0,0);
        this.one = 1.0;
    }

    public Vertex(Point3D point, Col color, Vec2D textCoord) {
        this.point = point;
        this.color = color;
        this.textCoord = textCoord;
        this.one = 1.0;
    }

    public Vertex(Point3D point, Col color, Vec2D textCoord, double one) {
        this.point = point;
        this.color = color;
        this.textCoord = textCoord;
        this.one = one;
    }

    public Point3D getPoint() {
        return point;
    }

    public Col getColor() {
        return color;
    }

    public double getX() {
        return point.getX();
    }

    public double getY() {
        return point.getY();
    }

    public double getZ() {
        return point.getZ();
    }

    public double getW() {
        return point.getW();
    }

    public double getOne() {
        return one;
    }

    public Vec2D getTextCoord() {
        return textCoord;
    }

    public Vertex mul(double t) {
        return new Vertex(point.mul(t), color.mul(t), textCoord.mul(t), one * t);
    }

    public Vertex add(Vertex v) {
        return new Vertex(point.add(v.getPoint()), color.add(v.getColor()), textCoord.add(v.getTextCoord()), one + v.getOne());
    }

    public Optional<Vertex> dehomog() {
        return point.dehomog().map(vec3D -> new Vertex(new Point3D(vec3D), color.mul(1 / point.getW()), textCoord.mul(1 / point.getW()), one / point.getW()));
    }
}
