package DisGraMaLOD;

import org.apache.commons.math.optimization.linear.Relationship;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.Edge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.WeightedGraph;
import org.jgrapht.event.GraphVertexChangeEvent;
import org.jgrapht.graph.*;
import sun.security.provider.certpath.Vertex;

import java.io.IOException;
import java.util.*;

public class Master {
    Synchronizer sync = new Synchronizer();
    private DefaultDirectedGraph<String, MyWeightedEdge> mainGraph;
    private Triple tripleQuery;

    public Master (DefaultDirectedGraph<String, MyWeightedEdge> mainGraph, Triple tripleQuery){
        this.mainGraph = mainGraph;
        this.tripleQuery = tripleQuery;
    }

/*
    public List<WeightedGraph<String, DefaultEdge>> splitGraph(int n){
        List<WeightedGraph<String, DefaultEdge>> graphs = new ArrayList<WeightedGraph<String, DefaultEdge>>();
        WeightedGraph<String, DefaultEdge> graphTemp = new DefaultDirectedWeightedGraph<>(DefaultEdge.class);
        Subgraph subgraph;
        graphTemp = mainGraph;

        //Divide the main graph in n subgraphs (Returned as List of graphs). Each graph will be passed to a slave

        Set<Vertex> vertexSet;
        int numberOfNodes = graphTemp.vertexSet().size();
        int subgraphNumberOfNodes = numberOfNodes/n;
        int surplusNodes = numberOfNodes%n;
        System.out.println("Numero di nodi per ogni sottografo: "+subgraphNumberOfNodes);
        System.out.println("Nodi che avanzano: "+surplusNodes);
        int j=0;
        int temp=0;
        try {
            while (graphTemp.vertexSet().toArray()[j] != null) {
                System.out.println("NODE[" + j + "] = " + graphTemp.vertexSet().toArray()[j]);
                j++;
            }
        }
        catch (Exception e){
            System.out.println("Number of nodes: "+j);
            System.out.println("");
        }


        for (int i=0; i<n; i++){
            graphs.add(graphTemp);
        }

        return graphs;
    }*/


    public List<DefaultDirectedGraph<String, MyWeightedEdge>> queryRelaxation(DefaultDirectedGraph<String, MyWeightedEdge> graph, int i, int threshold) {
        List<DefaultDirectedGraph<String, MyWeightedEdge>> relaxedQuerySet = new ArrayList<DefaultDirectedGraph<String, MyWeightedEdge>>();
        List<MyWeightedEdge> newEdgeList = new ArrayList<MyWeightedEdge>();
        int m = graph.edgeSet().size();
        if (i < m - 1) {
            System.out.println("Level_"+i+" IN_GO_DOWN;");
            relaxedQuerySet.addAll(queryRelaxation(graph, i + 1, threshold));
            System.out.println("Level_" + i + " OUT_GO_DOWN;");
        }
        // if exist j, so that, p[j] < e_m-(l-j) the x <- MAX{j: p[j] < e_m-(l-j)

        int l = m - threshold;
        int j = 1;
        int x = 0; //max j so that p[j] < e_m-(l-j)
        MyWeightedEdge element1 = new MyWeightedEdge();
        MyWeightedEdge element2 = new MyWeightedEdge();
        MyWeightedEdge e_t = new MyWeightedEdge();
        MyWeightedEdge e_tp1 = new MyWeightedEdge();
        Iterator<MyWeightedEdge> itr1 = graph.edgeSet().iterator();
        Iterator<MyWeightedEdge> itr2 = graph.edgeSet().iterator();
        Iterator<MyWeightedEdge> itr_tp1 = graph.edgeSet().iterator();
        boolean found = false;
        if (itr1.hasNext()) {
            element1 = itr1.next();
        }
        if (itr2.hasNext()) {
            element2 = itr2.next();
        }
        for (int temp = 0; temp < threshold && itr2.hasNext(); temp++) {
            element2 = itr2.next();
        }
        System.out.println("Level_"+i+". Checkin edges weight:");
        while (j <= l) {
            //I should get the weight of each edge
            System.out.println("          "+element1.getWeight()+"<"+element2.getWeight());
            if (element1.getWeight() < element2.getWeight()) { //simulating weight
                x = j;
                e_t = element1;
                found = true;
            }

            if (itr1.hasNext()) {
                element1 = itr1.next();
            }
            if (found) {
                e_tp1 = element1;
                found = false;
            }
            if (itr2.hasNext()) {
                element2 = itr2.next();
            }
            j++;

        }

        if (x!=0) {
            DefaultDirectedGraph<String, MyWeightedEdge> newGraph1 = new DefaultDirectedGraph<String, MyWeightedEdge>(MyWeightedEdge.class);
            Iterator<MyWeightedEdge> itr3 = graph.edgeSet().iterator();
            MyWeightedEdge element3 = new MyWeightedEdge();
            DefaultDirectedGraph<String, MyWeightedEdge> newGraph2 = new DefaultDirectedGraph<String, MyWeightedEdge>(MyWeightedEdge.class);
            Iterator<MyWeightedEdge> itr4 = graph.edgeSet().iterator();
            MyWeightedEdge element4 = new MyWeightedEdge();
            if (x >= l - threshold) {
                for (int temp = 1; temp <= x; temp++) {
                    if (itr3.hasNext()) {
                        element3 = itr3.next();
                        newGraph1.addVertex(element3.getSource().toString());
                        newGraph1.addVertex(element3.getTarget().toString());
                        newGraph1.addEdge(element3.getSource().toString(), element3.getTarget().toString(), element3);
                    }
                }for (int temp = 1; temp <= x-1; temp++) {
                    if (itr4.hasNext()) {
                        element4 = itr4.next();
                        newGraph2.addVertex(element4.getSource().toString());
                        newGraph2.addVertex(element4.getTarget().toString());
                        newGraph2.addEdge(element4.getSource().toString(), element4.getTarget().toString(), element4);
                    }
                }
                newGraph2.addVertex( e_tp1.getSource().toString());
                newGraph2.addVertex(e_tp1.getTarget().toString());
                newGraph2.addEdge(e_tp1.getSource().toString(), e_tp1.getTarget().toString(), e_tp1);
                relaxedQuerySet.add(newGraph1);
                relaxedQuerySet.add(newGraph2);
                System.out.println("Level_"+i+" IN_ALTERNATING");
                relaxedQuerySet.addAll(queryRelaxation(newGraph2, i, threshold));
                System.out.println("Level_" + i + " OUT_ALTERNATING;");
            }
        }
        return relaxedQuerySet;
    }
/*
    public void slaveForwarding (List<DirectedGraph<String, MyWeightedEdge>> graphs, int slaveNumber){
        List<Slave> slaveList = new ArrayList<Slave>();
        for (int i=0;i<slaveNumber;i++){
            slaveList.add(i, new Slave("Slave_"+(i+1), graphs.get(i), this.tripleQuery,sync));
            slaveList.get(i).start();
        }
    }*/

}