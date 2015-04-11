public class Edge {
    Node src;
    Node tgt;
    int weight;

    Edge(Node src, Node tgt, int weight) {
        this.src = src;
        this.tgt = tgt;
        this.weight = weight;
    }

    public Node getSrc() {
        return src;
    }

    public Node getTgt() {
        return tgt;
    }

    public int getWeight() {
        return weight;
    }
}
