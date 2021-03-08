package streda_16_35_c05.rasterize;

import java.util.Optional;

public interface Raster<T> {

    /**
     * Clear raster
     */
    void clear();

    /**
     * Set clear color
     *
     * @param clearValue clear color
     */
    void setClearValue(T clearValue);

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
     * Get value at [x,y] position
     *
     * @param x horizontal coordinate
     * @param y vertical coordinate
     * @return pixel color
     */
    Optional<T> getElement(int x, int y);

    void setElement(int x, int y, T value);

}
