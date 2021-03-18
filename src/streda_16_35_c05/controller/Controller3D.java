package streda_16_35_c05.controller;

import streda_16_35_c05.model.Element;
import streda_16_35_c05.model.TopologyType;
import streda_16_35_c05.model.Vertex;
import streda_16_35_c05.rasterize.Raster;
import streda_16_35_c05.renderer.GPURenderer;
import streda_16_35_c05.renderer.RendererZBuffer;
import streda_16_35_c05.shader.BasicColorShader;
import streda_16_35_c05.shader.Shader;
import streda_16_35_c05.shader.TextureShader;
import streda_16_35_c05.view.Panel;
import transforms.*;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Controller3D {

    private final Panel panel;
    private final Raster<Integer> imageRaster;
    private final GPURenderer renderer;

    private final List<Element> elementBuffer;
    private final List<Element> elementBufferAxis;
    private final List<Element> elementBufferTexture;
    private final List<Integer> indexBuffer;
    private final List<Vertex> vertexBuffer;

    Shader<Vertex, Col> shader;
    Shader<Vertex, Col> shaderTexture;

    private Mat4 model;
    private Mat4 projection;
    private Vec3D transl;

    private Camera camera;

    double rotationX, rotationY, rotationZ;
    double scale = 1;
    boolean rh = true;
    int activeSolid = 0;
    Element element;
    int indexEditVertex;

    Timer timer;
    boolean timerRun = false;

    double x, y, xEdit, yEdit;

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.imageRaster = panel.getRaster();
        this.renderer = new RendererZBuffer(imageRaster);

        transl = new Vec3D(0, 0, 0);

        elementBuffer = new ArrayList<>();
        elementBufferAxis = new ArrayList<>();
        elementBufferTexture = new ArrayList<>();
        indexBuffer = new ArrayList<>();
        vertexBuffer = new ArrayList<>();

        initListener();

        initMatrices();

        createScene();
        display();
    }

    private void createScene() {
        vertexBuffer.add(new Vertex(new Point3D(.5, .0, .9), new Col(255, 0, 0)));
        vertexBuffer.add(new Vertex(new Point3D(.7, .7, .9), new Col(255, 120, 0)));
        vertexBuffer.add(new Vertex(new Point3D(.0, .5, .3), new Col(255, 255, 0)));

        vertexBuffer.add(new Vertex(new Point3D(.3, .8, .5), new Col(0, 255, 0)));
        vertexBuffer.add(new Vertex(new Point3D(.1, .2, 1), new Col(0, 255, 120)));
        vertexBuffer.add(new Vertex(new Point3D(.7, .3, .2), new Col(0, 255, 255)));

        vertexBuffer.add(new Vertex(new Point3D(0, 0, 0), new Col(255, 0, 0)));
        vertexBuffer.add(new Vertex(new Point3D(5, 0, 0), new Col(255, 0, 0)));
        vertexBuffer.add(new Vertex(new Point3D(4.8, .2, 0), new Col(255, 0, 0)));
        vertexBuffer.add(new Vertex(new Point3D(4.8, -.2, 0), new Col(255, 0, 0)));

        vertexBuffer.add(new Vertex(new Point3D(0, 0, 0), new Col(0, 255, 0)));
        vertexBuffer.add(new Vertex(new Point3D(0, 5, 0), new Col(0, 255, 0)));
        vertexBuffer.add(new Vertex(new Point3D(.2, 4.8, 0), new Col(0, 255, 0)));
        vertexBuffer.add(new Vertex(new Point3D(-.2, 4.8, 0), new Col(0, 255, 0)));

        vertexBuffer.add(new Vertex(new Point3D(0, 0, 0), new Col(0, 0, 255)));
        vertexBuffer.add(new Vertex(new Point3D(0, 0, 5), new Col(0, 0, 255)));
        vertexBuffer.add(new Vertex(new Point3D(-.1, .1, 4.8), new Col(0, 0, 255)));
        vertexBuffer.add(new Vertex(new Point3D(.1, -.1, 4.8), new Col(0, 0, 255)));

        vertexBuffer.add(new Vertex(new Point3D(1, 1, 2), new Col(100, 0, 0)));
        vertexBuffer.add(new Vertex(new Point3D(1, 1, 3), new Col(0, 100, 0)));
        vertexBuffer.add(new Vertex(new Point3D(2, 1, 3), new Col(0, 0, 100), new Vec2D(0, 1)));
        vertexBuffer.add(new Vertex(new Point3D(2, 1, 2), new Col(100, 100, 100), new Vec2D(1, 1)));

        vertexBuffer.add(new Vertex(new Point3D(1, 2, 2), new Col(200, 0, 0)));
        vertexBuffer.add(new Vertex(new Point3D(1, 2, 3), new Col(0, 200, 0)));
        vertexBuffer.add(new Vertex(new Point3D(2, 2, 3), new Col(0, 0, 200), new Vec2D(0, 0)));
        vertexBuffer.add(new Vertex(new Point3D(2, 2, 2), new Col(200, 200, 200), new Vec2D(1, 0)));

        indexBuffer.add(0);
        indexBuffer.add(2);
        indexBuffer.add(1);

        indexBuffer.add(3);
        indexBuffer.add(4);
        indexBuffer.add(5);

        indexBuffer.add(6);
        indexBuffer.add(7);

        indexBuffer.add(10);
        indexBuffer.add(11);

        indexBuffer.add(14);
        indexBuffer.add(15);

        indexBuffer.add(7);
        indexBuffer.add(8);
        indexBuffer.add(9);

        indexBuffer.add(11);
        indexBuffer.add(12);
        indexBuffer.add(13);

        indexBuffer.add(15);
        indexBuffer.add(16);
        indexBuffer.add(17);

        indexBuffer.add(18);
        indexBuffer.add(19);
        indexBuffer.add(20);

        indexBuffer.add(18);
        indexBuffer.add(20);
        indexBuffer.add(21);

        indexBuffer.add(22);
        indexBuffer.add(23);
        indexBuffer.add(24);

        indexBuffer.add(22);
        indexBuffer.add(24);
        indexBuffer.add(25);

        indexBuffer.add(18);
        indexBuffer.add(22);
        indexBuffer.add(25);

        indexBuffer.add(18);
        indexBuffer.add(21);
        indexBuffer.add(25);

        indexBuffer.add(19);
        indexBuffer.add(23);
        indexBuffer.add(24);

        indexBuffer.add(19);
        indexBuffer.add(24);
        indexBuffer.add(20);

        indexBuffer.add(18);
        indexBuffer.add(22);
        indexBuffer.add(19);

        indexBuffer.add(19);
        indexBuffer.add(22);
        indexBuffer.add(23);

        indexBuffer.add(21);
        indexBuffer.add(20);
        indexBuffer.add(25);

        indexBuffer.add(25);
        indexBuffer.add(24);
        indexBuffer.add(20);

        elementBufferAxis.add(new Element(TopologyType.LINE, 6, 6));
        elementBufferAxis.add(new Element(TopologyType.TRIANGLE, 12, 9));

        elementBuffer.add(new Element(TopologyType.TRIANGLE, 0, 3));
        elementBuffer.add(new Element(TopologyType.TRIANGLE, 3, 3));
        elementBuffer.add(new Element(TopologyType.TRIANGLE, 21, 28));

        elementBufferTexture.add(new Element(TopologyType.TRIANGLE, 51, 6));

        shader = new BasicColorShader();
        shaderTexture = new TextureShader();
    }

    public void initListener() {
        panel.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() > 0) {
                    scale = 0.9;
                } else if (e.getWheelRotation() < 0) {
                    scale = 1.1;
                }
                display();
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    x = e.getX();
                    y = e.getY();
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    xEdit = e.getX();
                    yEdit = e.getY();

                    findPoint();
                }
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    double speed = 0.1;

                    double azimuth = Math.toDegrees(camera.getAzimuth());
                    double zenith = Math.toDegrees(camera.getZenith());

                    double dx = e.getX() - x;
                    double dy = e.getY() - y;

                    zenith += dy * speed;

                    if (zenith > 90) zenith = 90;
                    if (zenith < -90) zenith = -90;

                    azimuth += dx * speed;
                    azimuth = azimuth % 360;

                    camera = camera
                            .withZenith(Math.toRadians(zenith))
                            .withAzimuth(Math.toRadians(azimuth));
                    x = e.getX();
                    y = e.getY();

                } else if (SwingUtilities.isRightMouseButton(e)) {
                    double speed = 0.01;

                    double dx = e.getX() - xEdit;
                    double dy = e.getY() - yEdit;

                    Vertex vertex = vertexBuffer.get(indexEditVertex);
                    Vertex vertexEdit = new Vertex(new Point3D(vertex.getX() - dx * speed, vertex.getY(), vertex.getZ() - dy * speed), vertex.getColor(), vertex.getTextCoord());
                    vertexBuffer.set(indexEditVertex, vertexEdit);

                    xEdit = e.getX();
                    yEdit = e.getY();
                }
                display();
            }
        });

        panel.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                double step = 0.2;
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    camera = camera.forward(step);
                } else if (e.getKeyCode() == KeyEvent.VK_S) {
                    camera = camera.backward(step);
                } else if (e.getKeyCode() == KeyEvent.VK_A) {
                    camera = camera.left(step);
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    camera = camera.right(step);
                } else if (e.getKeyCode() == KeyEvent.VK_E) {
                    camera = camera.up(step);
                } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                    camera = camera.down(step);
                } else if (e.getKeyCode() == KeyEvent.VK_I) {
                    transl = new Vec3D(0, 0 - step, 0);
                } else if (e.getKeyCode() == KeyEvent.VK_K) {
                    transl = new Vec3D(0, 0 + step, 0);
                } else if (e.getKeyCode() == KeyEvent.VK_L) {
                    transl = new Vec3D(0 - step, 0, 0);
                } else if (e.getKeyCode() == KeyEvent.VK_J) {
                    transl = new Vec3D(0 + step, 0, 0);
                } else if (e.getKeyCode() == KeyEvent.VK_U) {
                    transl = new Vec3D(0, 0, 0 - step);
                } else if (e.getKeyCode() == KeyEvent.VK_O) {
                    transl = new Vec3D(0, 0, 0 + step);
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    initMatrices();
                } else if (e.getKeyCode() == KeyEvent.VK_NUMPAD4) {
                    rotationX += 10;
                } else if (e.getKeyCode() == KeyEvent.VK_NUMPAD7) {
                    rotationX -= 10;
                } else if (e.getKeyCode() == KeyEvent.VK_NUMPAD5) {
                    rotationY += 10;
                } else if (e.getKeyCode() == KeyEvent.VK_NUMPAD8) {
                    rotationY -= 10;
                } else if (e.getKeyCode() == KeyEvent.VK_NUMPAD6) {
                    rotationZ += 10;
                } else if (e.getKeyCode() == KeyEvent.VK_NUMPAD9) {
                    rotationZ -= 10;
                }
                display();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    renderer.setWireframe(!renderer.isWireframe());
                } else if (e.getKeyCode() == KeyEvent.VK_B) {
                    rh = !rh;
                    projection();
                } else if (e.getKeyCode() == KeyEvent.VK_F) {
                    if (!timerRun) {
                        timerRun = true;
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                rotationX += 1;
                                rotationY += 2;
                                rotationZ += 3;
                                display();
                            }
                        }, 0, 1000 / 20);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_G) {
                    if (timerRun) {
                        timer.cancel();
                        timerRun = false;
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_1) {
                    activeSolid = 0;
                } else if (e.getKeyCode() == KeyEvent.VK_2) {
                    activeSolid = 1;
                } else if (e.getKeyCode() == KeyEvent.VK_3) {
                    activeSolid = 2;
                } else if (e.getKeyCode() == KeyEvent.VK_4) {
                    activeSolid = 3;
                }
                display();
            }
        });
    }

    private void initMatrices() {
        model = new Mat4Identity();

        Vec3D e = new Vec3D(7, 7, 2);
        camera = new Camera()
                .withPosition(e)
                .withAzimuth(Math.toRadians(-135))
                .withZenith(Math.toRadians(-5));

        for (Element element : elementBuffer) {
            element.setModel(model);
        }

        projection();
    }

    private void projection() {
        if (rh) {
            projection = new Mat4PerspRH(
                    Math.PI / 3,
                    imageRaster.getHeight() / (float) imageRaster.getWidth(),
                    0.5,
                    50
            );
        } else {
            projection = new Mat4OrthoRH(imageRaster.getWidth() / 100, imageRaster.getHeight() / 100, 0.1, 50);
        }
    }

    private void transform() {
        Mat4 mtScale = new Mat4Scale(scale);
        Mat4 mtRotationX = new Mat4RotX(Math.toRadians(rotationX));
        Mat4 mtRotationY = new Mat4RotY(Math.toRadians(rotationY));
        Mat4 mtRotationZ = new Mat4RotZ(Math.toRadians(rotationZ));
        Mat4 mtTransl = new Mat4Transl(transl);

        if (activeSolid == 0) {
            for (Element element : elementBuffer) {
                model = element.getModel().mul(mtScale).mul(mtTransl).mul(mtRotationX).mul(mtRotationY).mul(mtRotationZ);
                element.setModel(model);
                element = elementBufferTexture.get(0);
                element.setModel(model);
            }
        } else if (activeSolid == 3) {
            element = elementBuffer.get(activeSolid - 1);
            model = element.getModel().mul(mtScale).mul(mtTransl).mul(mtRotationX).mul(mtRotationY).mul(mtRotationZ);
            element.setModel(model);
            element = elementBufferTexture.get(0);
            element.setModel(model);
        } else {
            element = elementBuffer.get(activeSolid - 1);
            model = element.getModel().mul(mtScale).mul(mtTransl).mul(mtRotationX).mul(mtRotationY).mul(mtRotationZ);
            element.setModel(model);
        }
        transl = new Vec3D(0, 0, 0);
        scale = 1;
        rotationX = 0;
        rotationY = 0;
        rotationZ = 0;
    }

    private synchronized void display() {
        renderer.clear();
        imageRaster.clear();

        transform();

        draw();

        renderer.setView(camera.getViewMatrix());
        renderer.setProjection(projection);
        renderer.setShader(shader);

        renderer.draw(elementBufferAxis, indexBuffer, vertexBuffer);

        renderer.draw(elementBuffer, indexBuffer, vertexBuffer);

        if (!renderer.isWireframe()) {
            renderer.setShader(shaderTexture);
        }
        renderer.draw(elementBufferTexture, indexBuffer, vertexBuffer);

        panel.repaint();
    }

    private void draw() {
        imageRaster.getGraphics().drawString("Movement: [W] Forward [S] Backward [A] Right [D] Left [E] Up [Q] Down | [SPACE] Wireframe [B] Change projection [ENTER] RESET", 10, 20);
        imageRaster.getGraphics().drawString("Transform: [I] Transl y - 1 [K] Transl y + 1 [J] Transl x + 1 " +
                "[L] Transl x - 1 [O] Transl z + 1 [U] Transl z - 1", 10, 40);
        imageRaster.getGraphics().drawString("[Scroll up] Scale + 0,1 [Scroll down] Scale - 0,1 | Animation: [F] Start [G] Stop", 10, 60);
        imageRaster.getGraphics().drawString("Activation solid: [1] All" + element(), 10, 80);
    }

    private String element() {
        String element = "";
        int index;
        for (int i = 0; i < elementBuffer.size(); i++) {
            index = i + 2;
            element += " [" + index + "] " + elementBuffer.get(i).getTopologyType().toString();
        }
        return element;
    }

    private void findPoint() {
        double distance = 0;
        for (int i = 0; i < vertexBuffer.size(); i++) {
            Vertex vertex = vertexBuffer.get(i);
            vertex = renderer.findPoint(vertex);

            double d = Math.sqrt(Math.pow(xEdit - vertex.getX(), 2) + Math.pow(yEdit - vertex.getY(), 2));

            if (i == 0) {
                distance = d;
                indexEditVertex = i;
            }

            if (distance > d) {
                distance = d;
                indexEditVertex = i;
            }
        }
    }
}
