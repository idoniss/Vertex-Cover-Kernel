import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class Main {
    public static int policy = 2;
    public static int currNumOfGroups = 0;
    public static void main(String[] args) throws IOException {
        int num_of_nodes = 20;
        int k = 10;
        Graph g1 = new Graph(num_of_nodes);
        for(int i = 0; i < num_of_nodes/2; i++) {
            for (int j = i + 1; j < num_of_nodes; j++) {
                if(Math.random() < 0.35)
                 g1.addEdge(i, j);
            }
        }
        Graph g2 = new Graph();
        g2.deepCopy(g1);
        Graph g3 = new Graph();
        g3.deepCopy(g1);
        if(policy == 1) {
            long g1_start = System.currentTimeMillis();
            boolean ans1 = check_for_cover_in_graph(g1, k);
            long end = System.currentTimeMillis();
            long timeElapsedNaive = end - g1_start;
            System.out.println("time elapsed g1: " + timeElapsedNaive + " milliseconds");
            System.out.println("answer for naive algo: " + ans1 + "\n");
        }
        if(policy == 2) {
            long g2_start = System.currentTimeMillis();
            boolean ans2 = lecture_kernel(g2, k);
            long end2 = System.currentTimeMillis();
            long timeElapsedLecture = end2 - g2_start;
            System.out.println("time elapsed g2: " + timeElapsedLecture + " milliseconds");
            System.out.println("answer for class kernel: " + ans2 + "\n");
        }
        if (policy == 3) {
            long g3_start = System.currentTimeMillis();
            BipartiteGraph h = new BipartiteGraph(g3);
            h.hopcroft_karp();
            int pairU[] = h.getPairU();
            int pairV[] = h.getPairV();
            LinkedList<BipartiteNode> vertex_cover_of_h = new LinkedList<>();
            h.find_augmenting_path(vertex_cover_of_h);
            LinkedList<Integer> v1 = new LinkedList<>();
            LinkedList<Integer> v_half = new LinkedList<>();
            LinkedList<Integer> v0 = new LinkedList<>();
            for (int i = 0; i < g3.getNum_of_vertices(); i++) {
                v0.add(i);
            }
            Iterator ver_iter = vertex_cover_of_h.iterator();
            BipartiteNode tmp;
            int tmp_node;
            while (ver_iter.hasNext()) {
                tmp = (BipartiteNode) ver_iter.next();
                tmp_node = tmp.get_ver_num();
                if (v0.contains((Integer) tmp_node)) {
                    v0.remove((Integer) tmp_node);
                    v_half.add((Integer) tmp_node);
                } else if (v_half.contains((Integer) tmp_node)) {
                    v_half.remove((Integer) tmp_node);
                    v1.add((Integer) tmp_node);
                }
            }
            //leaving only vertices which only one of their copies is in the matching
            Iterator v1_iter = v1.iterator();
            while (v1_iter.hasNext()) {
                g3.remove_vertex((Integer) v1_iter.next());
                k--;
            }
            Iterator v0_iter = v0.iterator();
            while (v0_iter.hasNext()) {
                g3.remove_vertex((Integer) v0_iter.next());
            }
            boolean ans3;
            if (k < 0) {
                ans3 = false;
            } else {
                ans3 = check_for_cover_in_graph(g3, k);
            }
            long end3 = System.currentTimeMillis();
            long timeElapsedBook = end3 - g3_start;
            System.out.println("time elapsed g3: " + timeElapsedBook + " milliseconds");
            System.out.println("answer for book kernel: " + ans3);
        }
    }

    public static boolean lecture_kernel(Graph g, int param_k) {
        if(param_k <= 0 && g.getNum_of_edges() > 0)
            return false;
        if(param_k <= 0 && g.getNum_of_edges() > 0)
            return false;
        LinkedList<Integer> adj[] = g.getAdjacencies();
        for (int i = 0; i < adj.length; i++){
            if (adj[i].size() == 0) {
                g.remove_vertex(i);
            }
        }
        for (int i = 0; i < adj.length; i++) {
            if (adj[i].size() > param_k) {
                g.remove_vertex(i);
                return lecture_kernel(g, param_k - 1);
            }
        }
        if(g.getNum_of_edges() > param_k*param_k)
            return false;
        else {
            if (param_k < 0)
                return false;
//            long num_of_groups = fact(g.getNum_of_vertices()) /
//                    (fact(param_k) * fact(g.getNum_of_vertices() - param_k));
            if(g.getNum_of_vertices() <= 0)
                return true;
            return check_for_cover_in_graph(g, param_k);
        }
    }

//    public static boolean lec1_kernel(Graph g, int param_k){
//        if(g.getNum_of_edges() == 0)
//            return true;
//        if(param_k == 0)
//            return false;
//        LinkedList<Integer> adj[] = g.getAdjacencies();
//        for(int i = 0; i < adj.length; i++){
//            if(adj[i].size() > 0){
//                Graph g1 = new Graph();
//                Graph g2 = new Graph();
//                g1.deepCopy(g);
//                g2.deepCopy(g);
//                g1.remove_vertex(i);
//                g2.remove_vertex(adj[i].get(0));
//                return ((lec1_kernel(g1,param_k-1))
//                        | lec1_kernel(g2,param_k - 1));
//            }
//        }
//        return true;
//    }

//    public static boolean lec2_kernel(Graph g, int param_k){
//        if(g.getNum_of_edges() == 0)
//            return true;
//        if(param_k == 0)
//            return false;
//        LinkedList<Integer> adj[] = g.getAdjacencies();
//        for(int i = 0; i < adj.length; i++) {
//            if (adj[i].size() == 0) {
//                Graph g1 = new Graph();
//                g1.deepCopy(g);
//                g1.remove_vertex(i);
//                return lec2_kernel(g1, param_k);
//            }
//        }
//        for(int i = 0; i < adj.length; i++){
//            if(adj[i].size() == 1){
//                Graph g2 = new Graph();
//                g2.deepCopy(g);
//                g2.remove_vertex(adj[i].get(0));
//                return lec1_kernel(g2,param_k - 1);
//            }
//        }
//        return true;
//    }

    public static boolean check_for_cover_in_graph(Graph g, int param_k){
        long num_of_groups = fact(g.getNum_of_vertices())/
                (fact(param_k) * fact(g.getNum_of_vertices() - param_k));
        LinkedList<Integer> groups[] = new LinkedList[Math.toIntExact(num_of_groups)];
        for(int i = 0; i < num_of_groups; i++){
            groups[i] = new LinkedList<>();
        }
        all_sub_groups_of_size_k(g.getNum_of_vertices(), param_k, num_of_groups, groups);
        for(int i = 0; i < num_of_groups; i++){
            LinkedList<Integer> tmp = groups[i];
            if(checkIfCover(g, tmp)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkIfCover(Graph g, LinkedList group){
        LinkedList<Integer> adjacencies[] = g.getAdjacencies();
        for(int i = 0; i < g.getNum_of_vertices(); i++){
            Iterator iter = adjacencies[i].iterator();
            while(iter.hasNext()){
                if (!((group.contains(iter.next())) | group.contains((Integer) i)))
                    return false;
            }
        }
        return true;
    }

    public void kernelGraph(Graph g) {
    }

    public boolean checkForCover(Graph g, int k) {
        // Initialize all vertices as not visited.
        int num_of_vertices = g.getNum_of_vertices();
        boolean visited[] = new boolean[num_of_vertices];
        LinkedList<Integer>[] adjacencies = g.getAdjacencies();
        for (int i = 0; i < num_of_vertices; i++) {
            visited[i] = false;
        }

        Iterator<Integer> i;
        //iterate over vertices
        for (int u = 0; u < num_of_vertices; u++) {
            // An edge is picked only if both visited[u] and visited[v] are false
            if (visited[u] == false) {
                // Go through all neighbours of u and pick the
                // first not yet visited vertex (We are basically
                //  picking an edge (u, v) from remaining edges.
                //this
                i = adjacencies[u].iterator();
                while (i.hasNext()) {
                    int v = i.next();
                    if (visited[v] == false) {
                        // Add the vertices (u, v) to the result
                        // set. We make the vertex u and v visited
                        // so that all edges from/to them would
                        // be ignored
                        visited[v] = true;
                        visited[u] = true;
                        break;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    static void getAllGroupsOfSizeK(int arr[], int n, int k,
                                    int index, int data[],
                                    int i,  long numOfGroups,
                                    LinkedList<Integer>[] groups) {
        if (currNumOfGroups > numOfGroups)
            return;

        // Current combination is already of size k, add it to list
        //after adding it,
        if (index == k) {
            for (int j = 0; j < k; j++) {
                int tmp = data[j];
                groups[currNumOfGroups].add(tmp);
            }
            currNumOfGroups++;
            return;
        }

        // When no more elements are there to put in data[]
        if (i >= n)
            return;

        // current is included, put next at next
        // location
        data[index] = arr[i];
        getAllGroupsOfSizeK(arr, n, k, index + 1,
                data, i + 1, numOfGroups
                , groups);

        // current is excluded, replace it with
        // next (Note that i+1 is passed, but
        // index is not changed)
        getAllGroupsOfSizeK(arr, n, k, index, data, i + 1,
                numOfGroups, groups);
    }

    // The main function that prints all combinations
    // of size r in arr[] of size n. This function
    // mainly uses combinationUtil()
    static void all_sub_groups_of_size_k(int num_of_nodes,
                                         int k, long numOfGroups,
                                         LinkedList<Integer>[] groups)
    {
        //create array of all the nodes
        int arr[] = new int[num_of_nodes];
        for (int i = 0; i < num_of_nodes; i++){
            arr[i] = i;
        }

        int data[] = new int[k]; // temporary array which will hold the subgroup

        currNumOfGroups = 0;
        // Print all combination using temprary
        // array 'data[]'
        getAllGroupsOfSizeK(arr, num_of_nodes, k, 0, data, 0,
                numOfGroups, groups);
    }
    static long fact(int n){
        if(n <= 1)
            return 1;
        else
            return (n * fact(n-1));
    }
}
