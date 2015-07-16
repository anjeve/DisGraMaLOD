package DisGraMaLOD;


import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;
import org.omg.PortableInterceptor.ObjectReferenceFactory;

public class Slave extends Thread {
    protected Synchronizer sync = null;
    private String slaveID;
    private DirectedGraph<String, DefaultEdge> graph;
    private Triple triple;

    public Slave(String slaveID, DirectedGraph<String, DefaultEdge> graph, Triple triple, Synchronizer sync) {
        this.slaveID = slaveID;
        this.graph = graph;
        this.triple = triple;
        this.sync = sync;
    }

    public void run() {
        boolean exactEdge = true;
        boolean connectedNode = graph.containsEdge(triple.getSubject(),triple.getObject());
        if (connectedNode) {
            exactEdge = (graph.getEdge(triple.getSubject(), triple.getObject()).getUserObject()).equals((triple.getPredicate().getUserObject()));
        }

        sync.print(this.slaveID,this.graph,this.triple,connectedNode,exactEdge);
    }

}
