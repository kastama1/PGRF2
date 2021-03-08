package streda_16_35_c05.movement;

import streda_16_35_c05.controller.Controller3D;
import streda_16_35_c05.renderer.GPURenderer;
import streda_16_35_c05.renderer.RendererZBuffer;
import streda_16_35_c05.view.Panel;
import transforms.Camera;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Movement {

    private Camera camera;
    private Controller3D controller;
    private Panel panel;
    private double x, y;
    private boolean wireframe;

    public Movement(Camera camera, Controller3D controller, Panel panel) {
        this.camera = camera;
        this.controller = controller;
        this.panel = panel;
    }

    public void initListener() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    double speed = 0.0015;

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

                }
                controller.display(camera);
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
                }
                controller.display(camera);
            }


            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    wireframe = !isWireframe();
                }
            }
        });
    }

    public boolean isWireframe() {
        return wireframe;
    }

}
