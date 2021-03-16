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
import java.util.*;
import java.util.Timer;

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

    private Mat4 model, projection, mtScale, mtRotationX, mtRotationY, mtRotationZ, mtTransl;
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
        vertexBuffer.add(new Vertex(new Point3D(.5, .0, .9), new Col(255, 0, 0), new Vec2D(0, 0))); //0
        vertexBuffer.add(new Vertex(new Point3D(.7, .7, .9), new Col(255, 120, 0), new Vec2D(0, 0))); //1
        vertexBuffer.add(new Vertex(new Point3D(.0, .5, .3), new Col(255, 255, 0), new Vec2D(0, 0))); //2

        vertexBuffer.add(new Vertex(new Point3D(.3, .8, .5), new Col(0, 255, 0), new Vec2D(0, 0))); //3
        vertexBuffer.add(new Vertex(new Point3D(.1, .2, 1), new Col(0, 255, 120), new Vec2D(0, 0))); //4
        vertexBuffer.add(new Vertex(new Point3D(.7, .3, .2), new Col(0, 255, 255), new Vec2D(0, 0))); //5

        vertexBuffer.add(new Vertex(new Point3D(0, 0, 0), new Col(255, 0, 0), new Vec2D(0, 0))); //6
        vertexBuffer.add(new Vertex(new Point3D(5, 0, 0), new Col(255, 0, 0), new Vec2D(0, 0))); //7
        vertexBuffer.add(new Vertex(new Point3D(4.8, .2, 0), new Col(255, 0, 0), new Vec2D(0, 0))); //8
        vertexBuffer.add(new Vertex(new Point3D(4.8, -.2, 0), new Col(255, 0, 0), new Vec2D(0, 0))); //9

        vertexBuffer.add(new Vertex(new Point3D(0, 0, 0), new Col(0, 255, 0), new Vec2D(0, 0))); //10
        vertexBuffer.add(new Vertex(new Point3D(0, 5, 0), new Col(0, 255, 0), new Vec2D(0, 0))); //11
        vertexBuffer.add(new Vertex(new Point3D(.2, 4.8, 0), new Col(0, 255, 0), new Vec2D(0, 0))); //12
        vertexBuffer.add(new Vertex(new Point3D(-.2, 4.8, 0), new Col(0, 255, 0), new Vec2D(0, 0))); //13

        vertexBuffer.add(new Vertex(new Point3D(0, 0, 0), new Col(0, 0, 255), new Vec2D(0, 0))); //14
        vertexBuffer.add(new Vertex(new Point3D(0, 0, 5), new Col(0, 0, 255), new Vec2D(0, 0))); //15
        vertexBuffer.add(new Vertex(new Point3D(-.1, .1, 4.8), new Col(0, 0, 255), new Vec2D(0, 0))); //16
        vertexBuffer.add(new Vertex(new Point3D(.1, -.1, 4.8), new Col(0, 0, 255), new Vec2D(0, 0))); //17

        vertexBuffer.add(new Vertex(new Point3D(1, 1, 2), new Col(100, 0, 0), new Vec2D(0, 0))); //18
        vertexBuffer.add(new Vertex(new Point3D(1, 1, 3), new Col(0, 100, 0), new Vec2D(0, 0))); //19
        vertexBuffer.add(new Vertex(new Point3D(2, 1, 3), new Col(0, 0, 100), new Vec2D(0, 1))); //20 textura
        vertexBuffer.add(new Vertex(new Point3D(2, 1, 2), new Col(100, 100, 100), new Vec2D(1, 1))); //21 textura

        vertexBuffer.add(new Vertex(new Point3D(1, 2, 2), new Col(200, 0, 0), new Vec2D(0, 0))); //22
        vertexBuffer.add(new Vertex(new Point3D(1, 2, 3), new Col(0, 200, 0), new Vec2D(0, 0))); //23
        vertexBuffer.add(new Vertex(new Point3D(2, 2, 3), new Col(0, 0, 200), new Vec2D(0, 0))); //24 textura
        vertexBuffer.add(new Vertex(new Point3D(2, 2, 2), new Col(200, 200, 200), new Vec2D(1, 0))); //25 textura

        indexBuffer.add(0); //0
        indexBuffer.add(2); //1
        indexBuffer.add(1); //2

        indexBuffer.add(3); //3
        indexBuffer.add(4); //4
        indexBuffer.add(5); //5

        indexBuffer.add(6); //6
        indexBuffer.add(7); //7

        indexBuffer.add(10); //8
        indexBuffer.add(11); //9

        indexBuffer.add(14); //10
        indexBuffer.add(15); //11

        indexBuffer.add(7); //12
        indexBuffer.add(8); //13
        indexBuffer.add(9); //14

        indexBuffer.add(11); //15
        indexBuffer.add(12); //16
        indexBuffer.add(13); //17

        indexBuffer.add(15); //18
        indexBuffer.add(16); //19
        indexBuffer.add(17); //20

        indexBuffer.add(18); //21
        indexBuffer.add(19); //22
        indexBuffer.add(20); //23

        indexBuffer.add(18); //24
        indexBuffer.add(20); //25
        indexBuffer.add(21); //26

        indexBuffer.add(22); //27
        indexBuffer.add(23); //28
        indexBuffer.add(24); //29

        indexBuffer.add(22); //30
        indexBuffer.add(24); //31
        indexBuffer.add(25); //32

        indexBuffer.add(18); //33
        indexBuffer.add(22); //34
        indexBuffer.add(25); //35

        indexBuffer.add(18); //36
        indexBuffer.add(21); //37
        indexBuffer.add(25); //38

        indexBuffer.add(19); //39
        indexBuffer.add(23); //40
        indexBuffer.add(24); //41

        indexBuffer.add(19); //42
        indexBuffer.add(24); //43
        indexBuffer.add(20); //44

        indexBuffer.add(18); //45
        indexBuffer.add(22); //46
        indexBuffer.add(19); //47

        indexBuffer.add(19); //48
        indexBuffer.add(22); //49
        indexBuffer.add(23); //50

        indexBuffer.add(21); //51
        indexBuffer.add(20); //52
        indexBuffer.add(25); //53

        indexBuffer.add(25); //54
        indexBuffer.add(24); //55
        indexBuffer.add(20); //56

        elementBufferAxis.add(new Element(TopologyType.LINE, 6, 6));
        elementBufferAxis.add(new Element(TopologyType.TRIANGLE, 12, 9));

        elementBuffer.add(new Element(TopologyType.TRIANGLE, 0, 3));
        elementBuffer.add(new Element(TopologyType.TRIANGLE, 3, 3));
        elementBuffer.add(new Element(TopologyType.TRIANGLE, 21, 28));

        shader = v -> new Col(255, 255, 255);

        elementBufferTexture.add(new Element(TopologyType.TRIANGLE, 51, 6));

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

                    double distance = 0;
                    for (int i = 0; i < vertexBuffer.size(); i++) {
                        Vertex vertex = vertexBuffer.get(i);
                        Optional<Vertex> oV = vertex.dehomog();
                        vertex = oV.get();
                        vertex = renderer.findPoint(vertex);
                        double dx = xEdit - vertex.getX();
                        double dy = yEdit - vertex.getZ();
                        double d = Math.sqrt((dx * dx) + (dy + dy));
                        if (d  < 0) {
                            d = -d;
                        }

                        if (i == 0) {
                            distance = d;
                        }

                        if (distance > d) {
                            distance = d;
                            indexEditVertex = i;
                        }
                    }
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

                } else if(SwingUtilities.isRightMouseButton(e)) {
                    double speed = 0.01;

                    double dx = e.getX() - xEdit;
                    double dy = e.getY() - yEdit;

                    Vertex vertex = vertexBuffer.get(indexEditVertex);
                    Vertex vertexEdit = new Vertex(new Point3D(vertex.getX() - dx * speed, vertex.getY(), vertex.getZ() - dy * speed), vertex.getColor(), vertex.getTextCoord());
                    vertexBuffer.set(indexEditVertex, vertexEdit);
                }
                display();
                x = e.getX();
                y = e.getY();
                xEdit = e.getX();
                yEdit = e.getY();
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
                    if (timerRun == false) {
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
                    if (timerRun == true) {
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

        for (int i = 0; i < elementBuffer.size(); i++) {
            element = elementBuffer.get(i);
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
        mtScale = new Mat4Scale(scale);
        mtRotationX = new Mat4RotX(Math.toRadians(rotationX));
        mtRotationY = new Mat4RotY(Math.toRadians(rotationY));
        mtRotationZ = new Mat4RotZ(Math.toRadians(rotationZ));
        mtTransl = new Mat4Transl(transl);

        if (activeSolid == 0) {
            for (int i = 0; i < elementBuffer.size(); i++) {
                element = elementBuffer.get(i);
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

        renderer.setShader(new BasicColorShader());
        renderer.draw(elementBufferAxis, indexBuffer, vertexBuffer);

        if (renderer.isWireframe()) {
            renderer.setShader(shader);
        }
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
}
