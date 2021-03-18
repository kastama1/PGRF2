package streda_16_35_c05.model;

import transforms.Mat4;
import transforms.Mat4Identity;

public class Element {

    private final TopologyType topologyType;
    private final int start;
    private final int count;
    private Mat4 model;

    public Element(TopologyType topologyType, int start, int count) {
        this.topologyType = topologyType;
        this.start = start;
        this.count = count;

        model = new Mat4Identity();
    }

    public TopologyType getTopologyType() {
        return topologyType;
    }

    public int getStart() {
        return start;
    }

    public int getCount() {
        return count;
    }

    public Mat4 getModel() {
        return model;
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }
}
