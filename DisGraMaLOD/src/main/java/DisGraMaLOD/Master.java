package DisGraMaLOD;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.ArrayList;
import java.util.List;

public class Master {
    private DirectedGraph<String, DefaultEdge> mainGraph;


    public Master (DirectedGraph<String, DefaultEdge> mainGraph){
        this.mainGraph = mainGraph;
    }


    public List<DirectedGraph<String, DefaultEdge>> splitGraph(int n){
         List<DirectedGraph<String, DefaultEdge>> graphs = new ArrayList<DirectedGraph<String, DefaultEdge>>();
         DirectedGraph<String, DefaultEdge> graphTemp = new DefaultDirectedGraph<>(DefaultEdge.class);

        graphTemp = mainGraph;
        //Divide the main graph in n subgraphs (Returned as List of graphs). Each graph will be passed to a slave

        for (int i=0; i<n; i++){
            graphs.add(graphTemp);
        }

        return graphs;
    }
}
