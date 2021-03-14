package streda_16_35_c05.shader;

import streda_16_35_c05.model.Vertex;
import transforms.Col;

public class BasicColorShader implements Shader<Vertex, Col> {

    @Override
    public Col shade(Vertex v) {
        return v.getColor();
    }
}
