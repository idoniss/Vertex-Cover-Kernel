
import java.util.LinkedList;
import java.util.List;

public class Vertex {
    private int label;
    private List<Edge> edges;
    private List<Vertex> neighbors;

    public Vertex(int l){
        this.label = l;
        edges = new LinkedList<>();
        neighbors = new LinkedList<>();
    }

    public void addNeighbor(Vertex v,Edge e){
        this.edges.add(e);
        this.neighbors.add(v);
    }

    public List<Vertex> getNeighbors(){
        return this.neighbors;
    }

    public List<Edge> getEdges(){
        return this.edges;
    }
}
