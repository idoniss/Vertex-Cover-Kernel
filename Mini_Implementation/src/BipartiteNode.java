
public class BipartiteNode {
    private int ver_num;
    private boolean left_vertex;

    public BipartiteNode(int ver, boolean left){
        ver_num = ver;
        left_vertex = left;
    }

    public int get_ver_num(){
        return ver_num;
    }
}
