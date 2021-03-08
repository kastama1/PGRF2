package streda_16_35_c05.renderer;

import streda_16_35_c05.model.Element;
import streda_16_35_c05.model.Vertex;
import transforms.Mat4;

import java.util.List;

public interface GPURenderer {

    void draw(List<Element> elements, List<Integer> ib, List<Vertex> vb);

    void clear();

    void setModel(Mat4 model);

    void setView(Mat4 view);

    void setProjection(Mat4 projection);

    void setWireframe(boolean wireframe);
}
