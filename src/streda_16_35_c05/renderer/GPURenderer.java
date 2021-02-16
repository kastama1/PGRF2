package streda_16_35_c05.renderer;

import transforms.Mat4;

public interface GPURenderer {

    void draw();

    void clear();

    void setModel(Mat4 model);

    void setView(Mat4 view);

    void setProjection(Mat4 projection);

}
