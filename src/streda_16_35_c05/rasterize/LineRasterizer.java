package streda_16_35_c05.rasterize;

import java.awt.*;

public abstract class LineRasterizer {

    Raster raster;
    Color color;

    public LineRasterizer(Raster raster) {
        this.raster = raster;
    }

    public final void setColor(Color color) {
        this.color = color;
    }

    public final void setColor(int color) {
        this.color = new Color(color);
    }

    public abstract void rasterize(int x1, int y1, int x2, int y2, Color color);

}
