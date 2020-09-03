
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class BipartiteGraph {
    private int num_of_nodes_at_each_side;
    private int marked[];
    private LinkedList<Integer> adj[]; //adj[u] stores all adjacents of left side vertex u.
    private int pairU[]; //will hold the pairs of each left side vertex in the match (if this vertex will be icluded in the match)
    private int pairV[]; //will hold the pairs of each right side vertex in the match (if this vertex will be icluded in the match)
    private int dist[];
    private int prevU[];
    private int prevV[];

    public BipartiteGraph(int num) {
        this.num_of_nodes_at_each_side = num;
        this.adj = new LinkedList[num + 1]; // since we are adding a dummy vertex - nil
        for(int i = 0; i < num + 1; i++){
            adj[i] = new LinkedList<>();
        }
    }

    public BipartiteGraph(Graph g){
        this.num_of_nodes_at_each_side = g.getNum_of_vertices();
        adj = new LinkedList[this.num_of_nodes_at_each_side + 1];
        marked = new int[this.num_of_nodes_at_each_side + 1];
        adj[0] = new LinkedList<>();
        for(int i = 1; i < num_of_nodes_at_each_side + 1; i++){
            marked[i] = 0;
            LinkedList<Integer> tmp_adj = g.getAdjacencies()[i - 1];
            Iterator iter = tmp_adj.iterator();
            adj[i] = new LinkedList<>();
            //adding all adjacencies from original graph
            while(iter.hasNext()){
                adj[i].add((Integer) (iter.next()) + 1);
            }
        }
    }

    public boolean find_augmenting_path(LinkedList<BipartiteNode> list){
        //this list holds all unmatched vertices from left side of the graph
        LinkedList<Integer> r = new LinkedList<>();
        for(int i = 1; i < pairU.length; i++){
            if(pairU[i] == 0)
                r.add(i);
        }
        LinkedList<Integer> t = new LinkedList<>();
        prevU = new int[num_of_nodes_at_each_side + 1];
        prevV = new int[num_of_nodes_at_each_side + 1];
        for(int i = 0; i < num_of_nodes_at_each_side + 1; i++){
            prevU[i] = 0;
            prevV[i] = 0;
        }
        Iterator iter = r.iterator();
        LinkedList<Integer> to_add_to_r = new LinkedList<>();
        LinkedList<Integer> u_neighbors;
        int u;
//        while(iter.hasNext()) {
        for(int i = 0; i < r.size(); i++){
            u = r.get(i);
            u_neighbors = adj[u];
            Iterator adj_iter = u_neighbors.iterator();
            int tmp_neighbor = 0;
            while (adj_iter.hasNext()) {
                tmp_neighbor = (Integer) adj_iter.next();
                if(pairU[u] == tmp_neighbor)
                    continue;
                else if(!t.contains(tmp_neighbor)) {
                    prevV[tmp_neighbor] = u;
                    t.add(tmp_neighbor);
                    if (pairV[tmp_neighbor] == 0) {
                        BipartiteNode vNode = new BipartiteNode(tmp_neighbor-1, false);
                        int next_node = prevV[tmp_neighbor];
                        BipartiteNode uNode = new BipartiteNode(next_node-1, true);
                        list.add(vNode);
                        while (next_node != 0) {
                            list.add(uNode);
                            next_node = prevU[next_node];
                            vNode = new BipartiteNode(next_node-1, false);
                            if (next_node != 0) {
                                list.add(vNode);
                                next_node = prevV[next_node];
                                uNode = new BipartiteNode(next_node-1, true);
                            }
                        }
                        return true;
                    } else {
                        int x_matched = pairV[tmp_neighbor];
                        r.add(x_matched);
//                        to_add_to_r.add(x_matched);
                        prevU[x_matched] = tmp_neighbor;
                    }
                }
            }
        }
//        r.addAll(to_add_to_r);
        //return t U {left vertices \ r}
        Iterator t_iter = t.iterator();
        while(t_iter.hasNext()) {
            BipartiteNode node = new BipartiteNode((Integer) t_iter.next() - 1 , false);
            list.add(node);
        }
        for(int i = 1; i < num_of_nodes_at_each_side + 1; i++){
            if(!r.contains(i))
                list.add(new BipartiteNode(i - 1, true));
        }
        return true;
    }

    public int[] getPairU(){
        return this.pairU;
    }

    public int[] getPairV(){
        return this.pairV;
    }

    public void add_edge(int u, int v) {
        adj[u].add(v);
    }

    public int hopcroft_karp(){
        // will hold the number of edges in the matching.
        int result = 0;

        //if u is in matching - pairU[u] will hold the pair of u in the mathcing.
        //else - pair[u] will hold 0
        pairU = new int [this.num_of_nodes_at_each_side + 1];

        //if v is in matching - pairV[v] will hold the pair of v in the mathcing.
        //else - pair[v] will hold 0
        pairV = new int [this.num_of_nodes_at_each_side + 1];

        //init dummy vertex as neighbor of all vertices
        for(int i = 0; i < this.num_of_nodes_at_each_side + 1; i++){
            pairU[i] = 0;
            pairV[i] = 0;
        }

        this.dist = new int[this.num_of_nodes_at_each_side + 1];
        for(int i = 1; i < dist.length; i++){
            dist[i] = 0;
        }
        dist[0] = Integer.MAX_VALUE;

        while(is_there_augmenting_path()){
            for(int u = 1; u <= this.num_of_nodes_at_each_side; u++){
                //if vertex u is a free vertex, and there is an augmenting
                //path that starts at u
                if((pairU[u] == 0) && (find_augmenting_path(u))){
                    result++;
                }
            }
        }
        return result;
    }

    //this function check weather is there an augmenting path
    boolean is_there_augmenting_path() {
        Queue<Integer> q = new ArrayBlockingQueue<Integer>(2 * num_of_nodes_at_each_side);
        for (int u = 1; u <= num_of_nodes_at_each_side; u++) {
            if (pairU[u] == 0) { //meaning: u is a free vertex
                dist[u] = 0;
                q.add(u);
            } else
                dist[u] = Integer.MAX_VALUE;
        }
        dist[0] = Integer.MAX_VALUE; //distnance to the dummy vertex
        while (!q.isEmpty()) {
            int u = q.poll();
            if (dist[u] < dist[0]) {
                Iterator iter = adj[u].iterator();
                int curr_neighbor;
                while (iter.hasNext()) {
                    curr_neighbor = (Integer) iter.next();
                    if (dist[pairV[curr_neighbor]] == Integer.MAX_VALUE) {//meaning: (v,pair(v)) is not yet explored edge
                        //then: consider the pair and add it to queue
                        dist[pairV[curr_neighbor]] = dist[u] + 1;
                        q.add(pairV[curr_neighbor]);
                    }
                }
            }
        }
        //if we could get to the dummy vertex using alternating path
        //of distnict vertices then there is an augmenting path
        return (dist[0] != Integer.MAX_VALUE);
    }

    //this function check for augmenting path for specific vertex
    //in there is such path, it will update pairV and pairU
    //fields of the graph so that we can extract from them the maximal match
    public boolean find_augmenting_path(int u) {
        if (u != 0) {//if u = 0, then this is the dummy frame
            Iterator iter = adj[u].iterator();
            int v;
            while (iter.hasNext()) {
                v = (Integer) iter.next();
                if (dist[pairV[v]] == dist[u] + 1) {
                    //meaning: bfs found that this edge belongs to an augmenting path
                    if (find_augmenting_path(pairV[v])){
                        pairV[v] = u;
                        pairU[u] = v;
                        return true;
                    }
                }
            }
            //if there is no augmenting path begining with vertex u - return false and set its dist value to max
            dist[u] = Integer.MAX_VALUE;
            return false;
        }
        return true;
    }
}
