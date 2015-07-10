package DisGraMaLOD;


import org.apache.avro.generic.GenericData;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {


    public static void main( String[] args ) {
        int slaveNumber = 3;
        List<DirectedGraph<String, DefaultEdge>> graphs = new ArrayList<DirectedGraph<String, DefaultEdge>>();


        final String excludedNamespaces = "a,b,c,d";
        final List<String> excludedNamespacesList = Arrays.asList(excludedNamespaces.split(","));


        //Uploading dataset
        Dataset dataset = new Dataset("test","http://dbpedia.org/","ontologyNameSpace",excludedNamespacesList);
        dataset = dataset.fromFiles("src\\caterpillar.nt","test","http://dbpedia.org/","ontologyNameSpace",excludedNamespacesList);

        //Triple to search
        DefaultEdge edgeTest = new DefaultEdge("http://dbpedia.org/property/wikiPageUsesTemplate");
        edgeTest.setSource("http://dbpedia.org/resource/Testb");
        edgeTest.setTarget("http://dbpedia.org/resource/Testc");
        Triple tripleQuery = new Triple("http://dbpedia.org/resource/Testa", edgeTest,"http://dbpedia.org/resource/Testb");

        //Master splits the main graphs and return a list of sub-graphs
        Master master = new Master(dataset.getGraph(), tripleQuery);
        graphs = master.splitGraph(slaveNumber);

        //Each slave take care of the matching of a single sub-graph and return
        master.slaveForwarding(graphs,slaveNumber);
    }
}
