package DisGraMaLOD;


import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;
import org.omg.PortableInterceptor.ObjectReferenceFactory;

public class Slave extends Thread {

    private static boolean free=true;
    private String slaveID;
    private DirectedGraph<String, DefaultEdge> graph;
    private Triple triple;
    private printResults results = new printResults();

    public Slave(String slaveID, DirectedGraph<String, DefaultEdge> graph, Triple triple) {
        this.slaveID = slaveID;
        this.graph = graph;
        this.triple = triple;
    }

    public void run() {
        boolean exactEdge = true;
        boolean connectedNode = graph.containsEdge(triple.getSubject(),triple.getObject());
        if (connectedNode) {
            exactEdge = (graph.getEdge(triple.getSubject(), triple.getObject()).getUserObject()).equals((triple.getPredicate().getUserObject()));
        }
       results.print(this.slaveID,this.triple,this.graph,connectedNode,exactEdge);
    }

}
