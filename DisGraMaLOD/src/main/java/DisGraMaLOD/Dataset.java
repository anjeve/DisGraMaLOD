package DisGraMaLOD;

import org.apache.commons.lang3.Validate;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.SimpleGraph;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Dataset {
    private DirectedGraph<String, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);
    private DirectedGraph<String, DefaultEdge> g2 = new DefaultDirectedGraph<>(DefaultEdge.class);
    private String namespace;
    private final Collection<String> excludedNamespaces;
    public String ontologyNamespace;
    private final SimpleGraph<String, DefaultEdge> simpleGraph = new SimpleGraph<>(DefaultEdge.class);
    private Set<String> removeVertices = new HashSet<>();
    private static HashMap<String, String> classes = new HashMap<>();
    public List<String> ontologyClasses = new ArrayList<>();
    private static HashMap<String, String> labels = new HashMap<>();
    private String name;

    public Dataset(String name, String namespace, String ontologyNamespace, Collection<String> excludedNamespaces) {
        Validate.notNull(namespace, "namespace must not be null");
        this.name = name;
        this.namespace = namespace;
        this.ontologyNamespace = ontologyNamespace;
        this.excludedNamespaces = excludedNamespaces;
    }

    public static Dataset fromFiles(String dataset, String name, String namespace, String ontologyNamespace, Collection<String> excludedNamespaces) {
        Validate.notNull(dataset, "datasets must not be null");
        Dataset s = new Dataset(name, namespace, ontologyNamespace,excludedNamespaces);
        Validate.isTrue(new File(dataset).exists(), "dataset not found: %s", dataset);
        NxParser nxp;
        try {
            nxp = new NxParser(new FileInputStream(dataset));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        s.readTriples(nxp);
        s.cleanup();
        return s;
    }

    private void readTriples(NxParser nxp) {

        while (nxp.hasNext()) {
            Node[] nodes = nxp.next();
            if (nodes.length != 3) {
                continue;
            }
            String subjectUri = nodes[0].toString();
            String propertyUri = nodes[1].toString();
            String objectUri = nodes[2].toString();

            if (propertyUri.equals("http://www.w3.org/2000/01/rdf-schema#label")) {
                labels.put(subjectUri, objectUri);
            }

            if (!isValid(subjectUri) || !isValid(propertyUri) || !isValid(objectUri)) {
                continue;
            }

            if (subjectUri.equals(objectUri)) {
                continue;
            }

            if (propertyUri.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
                if (objectUri.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#Property")) {
                    removeVertices.add(subjectUri);
                    removeVertices.add(objectUri);
                } else if (objectUri.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#Class")) {
                    removeVertices.add(subjectUri);
                    removeVertices.add(objectUri);
                } else if (objectUri.startsWith(ontologyNamespace) && !classes.containsKey(subjectUri)) {
                    // TODO find top classes for each class hierarchy tree path and only save top one
                    classes.put(subjectUri, objectUri);
                    if (!this.ontologyClasses.contains(objectUri)) {
                        this.ontologyClasses.add(objectUri);
                    }
                }
                // owl:DatatypeProperty
                // owl:ObjectProperty
            } else if (propertyUri.equals("http://www.w3.org/2002/07/owl#equivalentClass")) {
                removeVertices.add(subjectUri);
                removeVertices.add(objectUri);
            } else if (!subjectUri.startsWith(namespace)) {
                removeVertices.add(subjectUri);
            } else if (!objectUri.startsWith(namespace)) {
                removeVertices.add(objectUri);
            } else {
                boolean skip = false;
                if (skip) {
                    continue;
                }

                if (!g.containsVertex(subjectUri)) {
                    g.addVertex(subjectUri);
                    simpleGraph.addVertex(subjectUri);
                }
                if (!g.containsVertex(objectUri)) {
                    g.addVertex(objectUri);
                    simpleGraph.addVertex(objectUri);
                }
                if (g instanceof DirectedGraph) {
                    DefaultEdge e = new DefaultEdge(propertyUri);
                    e.setSource(subjectUri);
                    e.setTarget(objectUri);
                    g.addEdge(subjectUri, objectUri, e);
                } else {
                    if (!g.containsEdge(subjectUri, objectUri) && !g.containsEdge(objectUri, subjectUri)) {
                        g.addEdge(subjectUri, objectUri);
                        simpleGraph.addEdge(subjectUri, objectUri);
                    }
                }
            }
        }
    }

    public static String getClass(String subjectUri) {
        return classes.get(subjectUri);
    }

    private void cleanup() {
        for (String vertex : removeVertices) {
            if (g.containsVertex(vertex)) {
                g.removeVertex(vertex);
            }
        }
    }

    private boolean isValid(String url) {
        try {
            URL checked = new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public DirectedGraph<String, DefaultEdge> getGraph() {
        return this.g;
    }

    public SimpleGraph<String, DefaultEdge> getSimpleGraph() {
        return this.simpleGraph;
    }

    public String getName() {
        return this.name;
    }

    public String getLabel(String uri) {
        return (labels.get(uri));
    }


    public boolean isTriplePresent(Triple t){
        boolean nodeAreConnected = g.containsEdge(t.getSubject(),t.getObject());
        boolean edgeIsExact = (g.getEdge(t.getSubject(),t.getObject()).getUserObject()).equals((t.getPredicate().getUserObject()));
        if (!nodeAreConnected || !edgeIsExact){
            return false;
        }
        return true;
    }

/*
    public DirectedGraph<String, DefaultEdge> createRandomGraph(){

        g2.addVertex("a");
        g2.addVertex("b");
        g2.addVertex("b");
        g2.addVertex("c");
        g2.addVertex("d");
        g2.addVertex("e");
        g2.addVertex("f");
        g2.addVertex("g");
        g2.addVertex("h");
        g2.addVertex("i");
        DefaultEdge e = new DefaultEdge("LinkAB");
        e.setSource("a");
        e.setTarget("b");
        g2.addEdge("a", "b", e);
        DefaultEdge e1 = new DefaultEdge("LinkBD");
        e1.setSource("b");
        e1.setTarget("d");
        g2.addEdge("b", "d", e1);
        DefaultEdge e2 = new DefaultEdge("LinkDC");
        e2.setSource("d");
        e2.setTarget("c");
        g2.addEdge("d", "c", e2);
        DefaultEdge e3 = new DefaultEdge("LinkCA");
        e3.setSource("c");
        e3.setTarget("a");
        g2.addEdge("c", "a", e3);
        DefaultEdge e4 = new DefaultEdge("LinkED");
        e4.setSource("e");
        e4.setTarget("d");
        g2.addEdge("e", "d", e4);
        DefaultEdge e5 = new DefaultEdge("LinkEF");
        e5.setSource("e");
        e5.setTarget("f");
        g2.addEdge("e", "f", e5);
        DefaultEdge e6 = new DefaultEdge("LinkEG");
        e6.setSource("e");
        e6.setTarget("g");
        g2.addEdge("e", "g", e6);
        DefaultEdge e7 = new DefaultEdge("LinkGE");
        e7.setSource("g");
        e7.setTarget("e");
        g2.addEdge("g", "e", e7);
        DefaultEdge e8 = new DefaultEdge("LinkEA");
        e8.setSource("e");
        e8.setTarget("a");
        g2.addEdge("e", "a", e8);
        DefaultEdge e9 = new DefaultEdge("LinkEH");
        e9.setSource("e");
        e9.setTarget("h");
        g2.addEdge("e", "h", e9);

        return g2;
    }*/


}