package DisGraMaLOD;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.ArrayList;
import java.util.List;

public class Master {
    Synchronizer sync = new Synchronizer();
    private DirectedGraph<String, DefaultEdge> mainGraph;
    private Triple tripleQuery;

    public Master (DirectedGraph<String, DefaultEdge> mainGraph, Triple tripleQuery){
        this.mainGraph = mainGraph;
        this.tripleQuery = tripleQuery;
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

    public void slaveForwarding (List<DirectedGraph<String, DefaultEdge>> graphs, int slaveNumber){

        List<Slave> slaveList = new ArrayList<Slave>();
        for (int i=0;i<slaveNumber;i++){
            slaveList.add(i, new Slave("Slave_"+(i+1), graphs.get(i), this.tripleQuery,sync));
            slaveList.get(i).start();
        }


    }

}
