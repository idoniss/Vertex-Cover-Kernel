

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class Graph {

    private int num_of_vertices;
    private int num_of_edges;
    private LinkedList<Integer> adjacencies[];

    Graph(){
        num_of_vertices = 0;
        num_of_edges = 0;
        adjacencies = new LinkedList[0];
    }
    Graph(int n){
        this.num_of_vertices = n;
        this.num_of_edges = 0;
        adjacencies = new LinkedList[n];
        for(int i = 0; i < n; i++){
            adjacencies[i] = new LinkedList<>();
        }
    }

    void addEdge(int u, int v){
        this.adjacencies[u].add(v);
        this.adjacencies[v].add(u);
        num_of_edges++;
    }

    void createH(Graph g2){
        this.num_of_vertices = 2*g2.getNum_of_vertices();
        this.num_of_edges = 2*g2.getNum_of_edges();
        LinkedList<Integer> g2_adjacencies[] = g2.getAdjacencies();
        adjacencies = new LinkedList[num_of_vertices];
        for(int i = 0; i < num_of_vertices; i++){
            adjacencies[i] = new LinkedList<>();
            Iterator iter = g2_adjacencies[i].iterator();
            while(iter.hasNext()){
                adjacencies[i].add((Integer) iter.next());
            }
        }
    }

    void deepCopy(Graph g2){
        this.num_of_vertices = g2.getNum_of_vertices();
        this.num_of_edges = g2.getNum_of_edges();
        LinkedList<Integer> g2_adjacencies[] = g2.getAdjacencies();
        adjacencies = new LinkedList[num_of_vertices];
        for(int i = 0; i < num_of_vertices; i++){
            adjacencies[i] = new LinkedList<>();
            Iterator iter = g2_adjacencies[i].iterator();
            while(iter.hasNext()){
                adjacencies[i].add((Integer) iter.next());
            }
        }
    }

    void remove_vertex(int vertex){
        //remove this vertex from lists.
        //lower by one this vertex, in the lists
        // that their label (int) is greater than vertex.
        for(int i = 0; i < num_of_vertices; i++) {
            if(i == vertex){
                adjacencies[i].clear();
                continue;
            }
            LinkedList<Integer> tmp = adjacencies[i];
            if(tmp.remove((Integer) vertex))
                this.num_of_edges--;
            ListIterator<Integer> iter = tmp.listIterator();
            while (iter.hasNext()) {
                Integer tmp_vertex = (Integer) iter.next();
                if (tmp_vertex > ((Integer) vertex)) {
                    iter.remove();
                    iter.add(tmp_vertex - 1);
                }
            }
        }
        //fixing adjacencies
        for(int j = vertex; j < num_of_vertices - 1; j++) {
            adjacencies[j] = adjacencies[j + 1];
        }
        num_of_vertices = num_of_vertices - 1;

    }

    int getNum_of_vertices(){
        return this.num_of_vertices;
    }

    int getNum_of_edges(){
        return this.num_of_edges;
    }

    LinkedList<Integer>[] getAdjacencies(){
        return this.adjacencies;
    }
}

