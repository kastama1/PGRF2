package streda_16_35_c05.view;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private final Panel panel;

    public Window() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("UHK FIM PGRF2: " + getClass().getName());

        panel = new Panel();

        add(panel, BorderLayout.CENTER);
        pack();

        setLocationRelativeTo(null);

        panel.setFocusable(true);
        panel.grabFocus();
    }

    public Panel getPanel() {
        return panel;
    }

}
