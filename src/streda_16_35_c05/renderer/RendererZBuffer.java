package streda_16_35_c05.renderer;

import streda_16_35_c05.model.Element;
import streda_16_35_c05.model.TopologyType;
import streda_16_35_c05.model.Vertex;
import streda_16_35_c05.rasterize.DepthBuffer;
import streda_16_35_c05.rasterize.Raster;
import streda_16_35_c05.shader.Shader;
import transforms.*;

import java.util.List;
import java.util.Optional;

public class RendererZBuffer implements GPURenderer {

    private final Raster<Integer> imageRaster;
    private final Raster<Double> depthBuffer;

    boolean wireframe = false;

    private Mat4 model, view, projection;
    private Shader<Vertex, Col> shader;

    public RendererZBuffer(Raster<Integer> imageRaster) {
        this.imageRaster = imageRaster;
        this.depthBuffer = new DepthBuffer(imageRaster);

        model = new Mat4Identity();
        view = new Mat4Identity();
        projection = new Mat4Identity();
    }

    @Override
    public void draw(List<Element> elements, List<Integer> ib, List<Vertex> vb) {
        for (Element element : elements) {
            final TopologyType topologyType = element.getTopologyType();
            final int start = element.getStart();
            final int count = element.getCount();
            model = element.getModel();

            if (topologyType == TopologyType.TRIANGLE) {
                for (int i = start; i < start + count; i += 3) {
                    final Integer i1 = ib.get(i);
                    final Integer i2 = ib.get(i + 1);
                    final Integer i3 = ib.get(i + 2);
                    final Vertex v1 = vb.get(i1);
                    final Vertex v2 = vb.get(i2);
                    final Vertex v3 = vb.get(i3);
                    prepareTriangle(v1, v2, v3);
                }

            } else if (topologyType == TopologyType.LINE) {
                for (int i = start; i < start + count; i += 2) {
                    final Integer i1 = ib.get(i);
                    final Integer i2 = ib.get(i + 1);
                    final Vertex v1 = vb.get(i1);
                    final Vertex v2 = vb.get(i2);
                    prepareLine(v1, v2);
                }
            }
        }
    }

    private void prepareTriangle(Vertex v1, Vertex v2, Vertex v3) {

        Vertex a = new Vertex(v1.getPoint().mul(model).mul(view).mul(projection), v1.getColor(), v1.getTextCoord());
        Vertex b = new Vertex(v2.getPoint().mul(model).mul(view).mul(projection), v2.getColor(), v2.getTextCoord());
        Vertex c = new Vertex(v3.getPoint().mul(model).mul(view).mul(projection), v3.getColor(), v3.getTextCoord());

        if ((a.getX() > a.getW() && b.getX() > b.getW() && c.getX() > c.getW()) ||
                (a.getX() < -a.getW() && b.getX() < -b.getW() && c.getX() < -c.getW()) ||
                (a.getY() > a.getW() && b.getY() > b.getW() && c.getY() > c.getW()) ||
                (a.getY() < -a.getW() && b.getY() < -b.getW() && c.getY() < -c.getW()) ||
                (a.getZ() > a.getW() && b.getZ() > b.getW() && c.getZ() > c.getW()) ||
                (a.getZ() < 0 && b.getZ() < 0 && c.getZ() < 0)) return;

        if (a.getZ() < b.getZ()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }
        if (b.getZ() < c.getZ()) {
            Vertex temp = b;
            b = c;
            c = temp;
        }
        if (a.getZ() < b.getZ()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }

        if (a.getZ() < 0) {
            return;
        } else if (b.getZ() < 0) {
            double t1 = (0 - a.getZ()) / (b.getZ() - a.getZ());
            Vertex ab = a.mul(1 - t1).add(b.mul(t1));

            double t2 = (0 - a.getZ()) / (c.getZ() - a.getZ());
            Vertex ac = a.mul(1 - t2).add(c.mul(t2));
            drawLineOrTriangle(a, ab, ac);

        } else if (c.getZ() < 0) {
            double t1 = (0 - b.getZ()) / (c.getZ() - b.getZ());
            Vertex bc = b.mul(1 - t1).add(c.mul(t1));
            drawLineOrTriangle(a, b, bc);

            double t2 = (0 - a.getZ()) / (c.getZ() - a.getZ());
            Vertex ac = a.mul(1 - t2).add(c.mul(t2));

            drawLineOrTriangle(a, bc, ac);

        } else {
            drawLineOrTriangle(a, b, c);
        }
    }

    private void prepareLine(Vertex v1, Vertex v2) {

        Vertex a = new Vertex(v1.getPoint().mul(model).mul(view).mul(projection), v1.getColor(), v1.getTextCoord());
        Vertex b = new Vertex(v2.getPoint().mul(model).mul(view).mul(projection), v2.getColor(), v2.getTextCoord());

        if ((a.getX() > a.getW() && b.getX() > b.getW()) ||
                (a.getX() < -a.getW() && b.getX() < -b.getW()) ||
                (a.getY() > a.getW() && b.getY() > b.getW()) ||
                (a.getY() < -a.getW() && b.getY() < -b.getW()) ||
                (a.getZ() > a.getW() && b.getZ() > b.getW()) ||
                (a.getZ() < 0 && b.getZ() < 0)) return;

        if (a.getZ() < b.getZ()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }

        if (a.getZ() < 0) {
            return;
        } else if (b.getZ() < 0) {
            double t1 = (0 - a.getZ()) / (b.getZ() - a.getZ());
            Vertex ab = a.mul(1 - t1).add(b.mul(t1));
            drawLine(a, ab);
        } else {
            drawLine(a, b);
        }
    }

    private void drawLineOrTriangle(Vertex a, Vertex b, Vertex c) {
        if (wireframe) {
            drawLine(a, b);
            drawLine(b, c);
            drawLine(a, c);
        } else {
            drawTriangle(a, b, c);
        }
    }

    private void drawTriangle(Vertex a, Vertex b, Vertex c) {
        Optional<Vertex> oA = a.dehomog();
        Optional<Vertex> oB = b.dehomog();
        Optional<Vertex> oC = c.dehomog();

        if (oA.isEmpty() || oB.isEmpty() || oC.isEmpty()) return;

        a = oA.get();
        b = oB.get();
        c = oC.get();

        a = transformToWindow(a);
        b = transformToWindow(b);
        c = transformToWindow(c);

        if (a.getY() > b.getY()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }

        if (b.getY() > c.getY()) {
            Vertex temp = b;
            b = c;
            c = temp;
        }

        if (a.getY() > b.getY()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }

        long start = (long) Math.max(Math.ceil(a.getY()), 0);
        double end = Math.min(b.getY(), imageRaster.getHeight() - 1);

        for (long y = start; y <= end; y++) {
            double t1 = (y - a.getY()) / (b.getY() - a.getY());
            double t2 = (y - a.getY()) / (c.getY() - a.getY());

            Vertex ab = a.mul(1 - t1).add(b.mul(t1));
            Vertex ac = a.mul(1 - t2).add(c.mul(t2));

            fillLine(y, ab, ac);
        }

        start = (long) Math.max(Math.ceil(b.getY()), 0);
        end = Math.min(c.getY(), imageRaster.getWidth() - 1);

        for (long y = start; y <= end; y++) {
            double t1 = (y - b.getY()) / (c.getY() - b.getY());
            double t2 = (y - a.getY()) / (c.getY() - a.getY());

            Vertex bc = b.mul(1 - t1).add(c.mul(t1));
            Vertex ac = a.mul(1 - t2).add(c.mul(t2));

            fillLine(y, bc, ac);
        }

    }

    private void drawLine(Vertex a, Vertex b) {
        Optional<Vertex> oA = a.dehomog();
        Optional<Vertex> oB = b.dehomog();

        if (oA.isEmpty() || oB.isEmpty()) return;

        a = oA.get();
        b = oB.get();

        a = transformToWindow(a);
        b = transformToWindow(b);

        int x1 = (int) a.getX();
        int x2 = (int) b.getX();
        int y1 = (int) a.getY();
        int y2 = (int) b.getY();

        if ((x1 == x2) && (y1 == y2)) {
            drawPixel(x1, y1, a.getZ(), a.getColor());

        } else {
            int dx = Math.abs(x2 - x1);
            int dy = Math.abs(y2 - y1);
            int difference = dx - dy;

            int shift_x, shift_y;

            if (x1 < x2) shift_x = 1; else shift_x = -1;
            if (y1 < y2) shift_y = 1; else shift_y = -1;

            while ((x1 != x2) || (y1 != y2)) {

                int p = 2 * difference;

                if (p > -dy) {
                    difference = difference - dy;
                    x1 = x1 + shift_x;
                }
                if (p < dx) {
                    difference = difference + dx;
                    y1 = y1 + shift_y;
                }

                final Col finalColor = shader.shade(a);
                drawPixel(x1, y1, a.getZ(), finalColor);
            }
        }
    }

    public Vertex transformToWindow(Vertex vertex) {
        Vec3D vec3D = new Vec3D(vertex.getPoint())
                .mul(new Vec3D(1, -1, 1)) // Y jde nahoru a my chceme, aby šlo dolů
                .add(new Vec3D(1, 1, 0)) // (0,0) je uprostřed a my chceme, aby bylo vlevo nahoře
                // máme <0;2> -> vynásobíme polovinou velikosti plátna
                .mul(new Vec3D(imageRaster.getWidth() / 2f, imageRaster.getHeight() / 2f, 1));

        return new Vertex(new Point3D(vec3D), vertex.getColor(), vertex.getTextCoord());
    }

    private void fillLine(long y, Vertex a, Vertex b) {
        if (a.getX() > b.getX()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }

        long start = (long) Math.max(Math.ceil(a.getX()), 0);
        double end = Math.min(b.getX(), imageRaster.getWidth() - 1);

        for (long x = start; x <= end; x++) {
            double t = (x - a.getX()) / (b.getX() - a.getX());
            Vertex finalVertex = a.mul(1 - t).add(b.mul(t));

            final Col finalColor = shader.shade(finalVertex);
            drawPixel((int) x, (int) y, finalVertex.getZ(), finalColor);
        }
    }

    private void drawPixel(int x, int y, double z, Col color) {
        Optional<Double> zOptinal = depthBuffer.getElement(x, y);
        if (zOptinal.isPresent() && z < zOptinal.get()) {
            depthBuffer.setElement(x, y, z);
            imageRaster.setElement(x, y, color.getRGB());
        }
    }

    @Override
    public void clear() {
        depthBuffer.clear();
        imageRaster.clear();
    }

    @Override
    public void setModel(Mat4 model) {
        this.model = model;
    }

    @Override
    public void setView(Mat4 view) {
        this.view = view;
    }

    @Override
    public void setProjection(Mat4 projection) {
        this.projection = projection;
    }

    @Override
    public boolean isWireframe() {
        return wireframe;
    }

    @Override
    public void setWireframe(boolean wireframe) {
        this.wireframe = wireframe;
    }

    @Override
    public void setShader(Shader<Vertex, Col> shader) {
        this.shader = shader;
    }

    @Override
    public Vertex findPoint(Vertex vertex) {
        Vertex a = new Vertex(vertex.getPoint().mul(model).mul(view).mul(projection), vertex.getColor(), vertex.getTextCoord());
        Optional<Vertex> oA = a.dehomog();
        a = oA.get();
        a = transformToWindow(a);

        return a;
    }
}