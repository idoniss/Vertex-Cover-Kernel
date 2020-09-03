
import javafx.util.Pair;
import java.util.HashSet;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Edge {
    private Vertex first;
    private Vertex second;

    public Edge(Vertex v1, Vertex v2){
        this.first = v1;
        this.first = v2;
        v1.addNeighbor(v2, this);
        v2.addNeighbor(v1, this);
    }

    public Vertex getFirst() {
        return first;
    }

    public Vertex getSecond() {
        return second;
    }
}

