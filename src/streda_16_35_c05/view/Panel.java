package streda_16_35_c05.view;

import streda_16_35_c05.rasterize.Raster;
import streda_16_35_c05.rasterize.RasterBufferedImage;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {

    private RasterBufferedImage raster;

    private static final int WIDTH = 800, HEIGHT = 600;

    Panel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        raster = new RasterBufferedImage(WIDTH, HEIGHT);
        raster.setClearColor(Color.BLACK.getRGB());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        raster.repaint(g);
    }

    public void resize() {
        if (super.getWidth() < 1 || super.getHeight() < 1) return;

        RasterBufferedImage newRaster = new RasterBufferedImage(super.getWidth(), super.getHeight());
        newRaster.draw(raster);
        raster = newRaster;
    }

    public Raster getRaster() {
        return raster;
    }

}
