package streda_16_35_c05.model;

public class Element {

    private final TopologyType topologyType;
    private final int start; // na jakém indexu v ib začít
    private final int count; // kolik indexů z IB celkem použit

    public Element(TopologyType topologyType, int start, int count) {
        this.topologyType = topologyType;
        this.start = start;
        this.count = count;
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
}
