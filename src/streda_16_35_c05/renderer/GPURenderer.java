package streda_16_35_c05.renderer;

import streda_16_35_c05.model.Element;
import streda_16_35_c05.model.Vertex;
import streda_16_35_c05.shader.Shader;
import transforms.Col;
import transforms.Mat4;

import java.util.List;

public interface GPURenderer {

    void draw(List<Element> elements, List<Integer> ib, List<Vertex> vb);

    void clear();

    void setShader(Shader<Vertex, Col> shader);
    
    void setView(Mat4 view);

    void setProjection(Mat4 projection);

    boolean isWireframe();

    void setWireframe(boolean wireframe);

    Vertex findPoint(Vertex vertex);
}
