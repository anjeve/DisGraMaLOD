package DisGraMaLOD;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;


public class Synchronizer {

    public synchronized void print(String slaveID, DirectedGraph<String, DefaultEdge> graph, Triple triple, boolean connectedNode, boolean exactEdge ){


        System.out.print(slaveID+":  Thread: " + slaveID);
        if (!connectedNode || !exactEdge) {
            System.out.println("-----> MATCHING NOT FOUND");
        } else {
            System.out.println("-----> MATCHING FOUND");
        }
        System.out.println(slaveID+":        Triple: ");
        System.out.println(slaveID+":            S: " + triple.getSubject());
        System.out.println(slaveID+":            P: " + triple.getPredicate());
        System.out.println(slaveID+":            O: " + triple.getObject());
        System.out.println(slaveID+":        Graph: ");
        System.out.println(slaveID+":            " + graph);
        System.out.println("");
    }
   
}
