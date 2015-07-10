package DisGraMaLOD;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;


public class printResults {
    public synchronized void print(String slaveID, Triple triple, DirectedGraph<String, DefaultEdge> graph, boolean connectedNode, boolean exactEdge){
        System.out.print("Thread: " + slaveID);
        if (!connectedNode || !exactEdge) {
            System.out.println("-----> MATCHING NOT FOUND");
        } else {
            System.out.println("-----> MATCHING FOUND");
        }
        System.out.println("    Triple: ");
        System.out.println("        S: " + triple.getSubject());
        System.out.println("        P: " + triple.getPredicate());
        System.out.println("        O: " + triple.getObject());
        System.out.println("    Graph: ");
        System.out.println("        " + graph);
    }
}
