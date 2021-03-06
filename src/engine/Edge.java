package engine;

/**
 * This class provides an Edge object, with associated methods.
 *
 */
class Edge {
    private Node src;
    private Node tgt;
    private double weight;
    private int flow;

    Edge(Node src, Node tgt, double weight) {
        this.src = src;
        this.tgt = tgt;
        this.weight = weight;
        this.flow = 0;
    }

    void setFlow(int flow) {
        this.flow = flow;
    }

    public Node getSrc() {
        return src;
    }

    public Node getTgt() {
        return tgt;
    }

    public double getWeight() {
        return weight;
    }

    public int getFlow() {
        return flow;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "flow=" + flow +
                ", weight=" + weight +
                ", src=" + src +
                ", tgt=" + tgt +
                '}';
    }
}
