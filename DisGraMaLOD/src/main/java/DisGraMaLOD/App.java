package DisGraMaLOD;


import org.apache.avro.generic.GenericData;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import sun.security.provider.certpath.Vertex;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {


    public static void main( String[] args ) {
        int slaveNumber = 4;
        List<WeightedGraph<String, DefaultEdge>> graphs = new ArrayList<WeightedGraph<String, DefaultEdge>>();


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
            //Master master = new Master(dataset.getGraph(), tripleQuery);
        //graphs = master.splitGraph(slaveNumber);

        //Each slave cares of the matching of a single sub-graph and return
        //master.slaveForwarding(graphs,slaveNumber); //should return a boolean value if the matching is present




       /* WeightedGraph<String, DefaultEdge> mainGraph = new DefaultDirectedWeightedGraph<String, DefaultEdge>(DefaultEdge.class);
        mainGraph.addVertex("a");
        mainGraph.addVertex("b");
        mainGraph.addVertex("c");
        mainGraph.addVertex("d");

        DefaultEdge e1 = new DefaultEdge("e1");
        mainGraph.addEdge("a", "b", e1);
        mainGraph.setEdgeWeight(e1,1);
        DefaultEdge e2 = new DefaultEdge("e2");
        mainGraph.addEdge("a", "c",e2);
        mainGraph.setEdgeWeight(e2, 2);
        DefaultEdge e3 = new DefaultEdge("e3");
        mainGraph.addEdge("a", "d",e3);
        mainGraph.setEdgeWeight(e3, 3);
        DefaultEdge e4 = new DefaultEdge("e4");
        mainGraph.addEdge("d", "b",e4);
        mainGraph.setEdgeWeight(e4,4);
        DefaultEdge e5 = new DefaultEdge("e5");
        mainGraph.addEdge("d", "c",e5);
        mainGraph.setEdgeWeight(e5, 5);
        DefaultEdge e6 = new DefaultEdge("e6");
        mainGraph.addEdge("b", "c",e6);
        mainGraph.setEdgeWeight(e6, 6);*/


        DefaultDirectedGraph <String, MyWeightedEdge> mainGraph =new DefaultDirectedGraph <String, MyWeightedEdge>(MyWeightedEdge.class);

        mainGraph.addVertex("v1");
        mainGraph.addVertex("v2");
        mainGraph.addVertex("v3");
        mainGraph.addVertex("v4");


        MyWeightedEdge e1 = new MyWeightedEdge();
        e1.setWeight(1);
        e1.setUserObject("e1");
        mainGraph.addEdge("v1", "v2", e1);
        MyWeightedEdge e2 = new MyWeightedEdge();
        e2.setWeight(2);
        e2.setUserObject("e2");
        mainGraph.addEdge("v1", "v3", e2);
        MyWeightedEdge e3 = new MyWeightedEdge();
        e3.setWeight(3);
        e3.setUserObject("e3");
        mainGraph.addEdge("v1", "v4", e3);
        MyWeightedEdge e4 = new MyWeightedEdge();
        e4.setWeight(4);
        e4.setUserObject("e4");
        mainGraph.addEdge("v4", "v2", e4);
        MyWeightedEdge e5 = new MyWeightedEdge();
        e5.setWeight(5);
        e5.setUserObject("e5");
        mainGraph.addEdge("v4", "v3", e5);
        MyWeightedEdge e6 = new MyWeightedEdge();
        e6.setWeight(6);
        e6.setUserObject("e6");
        mainGraph.addEdge("v2", "v3", e6);


        System.out.println("INItIAL MAIN GRAPH: "+mainGraph);

        Master master = new Master(mainGraph, tripleQuery);

        System.out.println("Final realxed query set:" + master.queryRelaxation(mainGraph,1, 2));
    }
}
