package streda_16_35_c05.shader;

import streda_16_35_c05.model.Vertex;
import transforms.Col;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TextureShader implements Shader<Vertex, Col> {

    BufferedImage image;

    public TextureShader() {
        try {
            image = ImageIO.read(new File("576.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Col shade(Vertex vertex) {
        int width = image.getWidth();
        int height = image.getHeight();
        int texX = (int) Math.floor(vertex.getTextCoord().getX() * width);
        int texY = (int) Math.floor(vertex.getTextCoord().getY() * height);
        return new Col(image.getRGB(texX, texY));
    }
}
