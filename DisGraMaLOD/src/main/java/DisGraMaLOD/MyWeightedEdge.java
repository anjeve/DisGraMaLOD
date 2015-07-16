package DisGraMaLOD;

import org.apache.hadoop.yarn.state.Graph;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Iterator;

/**
 * Created by Pierpaolo.Troiano on 15.07.2015.
 */
public class MyWeightedEdge extends DefaultEdge {

    private double weight;

    public double getWeight(){
        return weight;
    }

    public void setWeight(double weight){
        this.weight = weight;
    }
}
