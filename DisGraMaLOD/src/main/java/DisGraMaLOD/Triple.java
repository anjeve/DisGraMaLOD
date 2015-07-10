package DisGraMaLOD;

import org.jgraph.graph.DefaultEdge;

public class Triple {
    private String subject;
    private DefaultEdge predicate;
    private String object;

    public Triple(String s, DefaultEdge p, String o) {
        this.subject = s;
        this.predicate = p;
        this.object = o;
    }

    public String getSubject() {
        return this.subject;
    }

    public DefaultEdge getPredicate() {
        return this.predicate;
    }

    public String getObject() {
        return this.object;
    }

    public String getTriple() {
        return this.object+" | "+this.predicate.getUserObject()+" | "+this.subject;
    }
}
