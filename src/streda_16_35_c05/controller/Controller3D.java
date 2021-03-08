package streda_16_35_c05.controller;

import streda_16_35_c05.model.Element;
import streda_16_35_c05.model.TopologyType;
import streda_16_35_c05.model.Vertex;
import streda_16_35_c05.movement.Movement;
import streda_16_35_c05.rasterize.Raster;
import streda_16_35_c05.renderer.GPURenderer;
import streda_16_35_c05.renderer.RendererZBuffer;
import streda_16_35_c05.view.Panel;
import transforms.*;

import java.util.ArrayList;
import java.util.List;

public class Controller3D {

    private final Panel panel;
    private final Raster<Integer> imageRaster;
    private final GPURenderer renderer;

    private final List<Element> elementBuffer;
    private final List<Integer> indexBuffer;
    private final List<Vertex> vertexBuffer;

    private Movement movement;

    private Mat4 model, projection;
    private Camera camera;

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.imageRaster = panel.getRaster();
        this.renderer = new RendererZBuffer(imageRaster);

        elementBuffer = new ArrayList<>();
        indexBuffer = new ArrayList<>();
        vertexBuffer = new ArrayList<>();

        initMatrices();

        movement = new Movement(camera, this, panel);

        movement.initListener();

        createScene();

        display(camera);
    }

    private void createScene() {
        vertexBuffer.add(new Vertex(new Point3D(.5, .0, .9), new Col(255, 0, 0)));
        vertexBuffer.add(new Vertex(new Point3D(.7, .7, .9), new Col(255, 120, 0)));
        vertexBuffer.add(new Vertex(new Point3D(.0, .5, .3), new Col(255, 255, 0)));

        vertexBuffer.add(new Vertex(new Point3D(.3, .8, .5), new Col(0, 255, 0)));
        vertexBuffer.add(new Vertex(new Point3D(.1, .2, 1), new Col(0, 255, 120)));
        vertexBuffer.add(new Vertex(new Point3D(.7, .3, .2), new Col(0, 255, 255)));


        vertexBuffer.add(new Vertex(new Point3D(0, 0, 0), new Col(255, 255, 255)));
        vertexBuffer.add(new Vertex(new Point3D(5, 0, 0), new Col(255, 0, 0)));
        vertexBuffer.add(new Vertex(new Point3D(0, 5, 0), new Col(0, 255, 0)));
        vertexBuffer.add(new Vertex(new Point3D(0, 0, 5), new Col(0, 0, 255)));

        indexBuffer.add(0);
        indexBuffer.add(2);
        indexBuffer.add(1);

        indexBuffer.add(5);
        indexBuffer.add(4);
        indexBuffer.add(3);


        indexBuffer.add(6);
        indexBuffer.add(7);

        indexBuffer.add(6);
        indexBuffer.add(8);

        indexBuffer.add(6);
        indexBuffer.add(9);

        elementBuffer.add(new Element(TopologyType.TRIANGLE, 0, 6));
        elementBuffer.add(new Element(TopologyType.LINE, 6, 6));

        renderer.draw(elementBuffer, indexBuffer, vertexBuffer);
    }

    private void initMatrices() {
        model = new Mat4Identity();

        Vec3D e = new Vec3D(0, -5, 2);
        camera = new Camera()
                .withPosition(e)
                .withAzimuth(Math.toRadians(90))
                .withZenith(Math.toRadians(-20));

        projection = new Mat4PerspRH(
                Math.PI / 3,
                imageRaster.getHeight() / (float) imageRaster.getWidth(),
                0.5,
                50
        );
    }

    public void display(Camera camera) {
        renderer.clear();
        imageRaster.clear();

        renderer.setWireframe(movement.isWireframe());

        renderer.setModel(model);
        renderer.setView(camera.getViewMatrix());
        renderer.setProjection(projection);

        renderer.draw(elementBuffer, indexBuffer, vertexBuffer);

        panel.repaint();
    }

}
