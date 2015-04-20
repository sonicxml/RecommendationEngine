public class Edge implements Comparable<Edge> {
    Node src;
    Node tgt;
    int weight;
    int flow;

    Edge(Node src, Node tgt, int weight) {
        this.src = src;
        this.tgt = tgt;
        this.weight = weight;
        this.flow = 0;
    }

    public void setFlow(int flow) {
        this.flow = flow;
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

    public int getFlow() {
        return flow;
    }

	@Override
	public int compareTo(Edge other) {
		if (weight < other.getWeight()) {
			return -1;
		} else if (weight > other.getWeight()) {
			return 1;
		}
		return 0;
	}
}
