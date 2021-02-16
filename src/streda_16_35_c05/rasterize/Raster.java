package streda_16_35_c05.rasterize;

import java.util.Optional;

public interface Raster {

    /**
     * Clear canvas
     */
    void clear();

    /**
     * Set clear color
     *
     * @param clearColor clear color
     */
    void setClearColor(int clearColor);

    /**
     * Get horizontal size
     *
     * @return width
     */
    int getWidth();

    /**
     * Get vertical size
     *
     * @return height
     */
    int getHeight();

    /**
     * Get pixel color at [x,y] position
     *
     * @param x horizontal coordinate
     * @param y vertical coordinate
     * @return pixel color
     */
    Optional<Integer> getPixel(int x, int y);

    /**
     * Set pixel color at [x,y] position
     *
     * @param x     horizontal coordinate
     * @param y     vertical coordinate
     * @param color pixel color
     */
    void setPixel(int x, int y, int color);

}
