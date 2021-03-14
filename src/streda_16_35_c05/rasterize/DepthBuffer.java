package streda_16_35_c05.rasterize;

import java.awt.*;
import java.util.Optional;

public class DepthBuffer implements Raster<Double> {

    private final double[][] data;
    private final int width;
    private final int height;
    private Double clearValue;

    public DepthBuffer(Raster<?> raster) {
        width = raster.getWidth();
        height = raster.getHeight();
        data = new double[width][height];

        setClearValue(1d);
        clear();
    }

    @Override
    public void clear() {
        for (double[] d : data) {
            for (int i = 0; i < d.length; i++) {
                d[i] = clearValue;
            }
        }
    }

    @Override
    public void setClearValue(Double clearValue) {
        this.clearValue = clearValue;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Optional<Double> getElement(int x, int y) {
        if (x >= 0 && y >= 0 && x < getWidth() && y < getHeight()) {
            return Optional.of(data[x][y]);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void setElement(int x, int y, Double z) {
        if (x >= 0 && y >= 0 && x < getWidth() && y < getHeight()) {
            data[x][y] = z;
        }
    }

    @Override
    public Graphics getGraphics() {
        return null;
    }
}
