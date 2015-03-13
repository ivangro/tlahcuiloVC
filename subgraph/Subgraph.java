package subgraph;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
/**
 *
 * @author Ivan Guerrero
 */
public class Subgraph {

    public static void main(String[] args) {
        Graph<INode, IEdge> g1 = new SparseMultigraph<>();
        Node[] nodes1 = new Node[] {new Node("1"), new Node("2")};
        for (Node n : nodes1) {
            g1.addVertex(n);
        }
        
        Graph<INode, IEdge> g2 = new SparseMultigraph<>();
        Node[] nodes2 = new Node[] {new Node("3")};
        for (Node n : nodes2) {
            g2.addVertex(n);
        }
        
        Solution maxSimilarity = new SimilarityEvaluator().evaluate(g2, g1);
        System.out.println(maxSimilarity);
    }
    
    public static void main1(String[] args) {
        Graph<INode, IEdge> g1 = new SparseMultigraph<>();
        Node[] nodes1 = new Node[] {new Node("1"), new Node("2"), new Node("3"), new Node("4")};
        for (Node n : nodes1) {
            g1.addVertex(n);
        }
        g1.addEdge(new Edge("1", "2", 1), nodes1[0], nodes1[1]);
        g1.addEdge(new Edge("1", "2", 2), nodes1[0], nodes1[1]);
        g1.addEdge(new Edge("1", "2", 3), nodes1[0], nodes1[1]);
        
        Graph<INode, IEdge> g2 = new SparseMultigraph<>();
        Node[] nodes2 = new Node[] {new Node("3"), new Node("4"), new Node("5")};
        for (Node n : nodes2) {
            g2.addVertex(n);
        }
        g2.addEdge(new Edge("3", "4", 1), nodes2[0], nodes2[1]);
        g2.addEdge(new Edge("3", "4", 4), nodes2[0], nodes2[1]);
        
        Solution maxSimilarity = new SimilarityEvaluator().evaluate(g2, g1);
        System.out.println(maxSimilarity);
    }
    
    public static void main3(String[] args) {
        Graph<INode, IEdge> g1 = new SparseMultigraph<>();
        Node[] nodes1 = new Node[] {new Node("1"), new Node("2"), new Node("3"), new Node("4")};
        for (Node n : nodes1) {
            g1.addVertex(n);
        }
        g1.addEdge(new Edge("1", "2", 1), nodes1[0], nodes1[1]);
        g1.addEdge(new Edge("1", "3", 3), nodes1[1], nodes1[2]);
        g1.addEdge(new Edge("2", "3", 2), nodes1[0], nodes1[2]);
        Graph<INode, IEdge> g2 = new SparseMultigraph<>();
        Node[] nodes2 = new Node[] {new Node("5"), new Node("6")};
        for (Node n : nodes2) {
            g2.addVertex(n);
        }
        
        Solution maxSimilarity = new SimilarityEvaluator().evaluate(g1, g2);
        System.out.println(maxSimilarity);
    }
    
    public static void main2(String[] args) {
        Graph<INode, IEdge> g1 = new SparseMultigraph<>();
        Node[] nodes1 = new Node[] {new Node("1"), new Node("2"), new Node("3")};
        for (Node n : nodes1) {
            g1.addVertex(n);
        }
        g1.addEdge(new Edge("1", "2", 1), nodes1[0], nodes1[1]);
        g1.addEdge(new Edge("1", "3", 3), nodes1[1], nodes1[2]);
        g1.addEdge(new Edge("2", "3", 2), nodes1[0], nodes1[2]);
        Graph<INode, IEdge> g2 = new SparseMultigraph<>();
        Node[] nodes2 = new Node[] {new Node("4"), new Node("5"), new Node("6"), new Node("7")};
        for (Node n : nodes2) {
            g2.addVertex(n);
        }
        g2.addEdge(new Edge("4", "5", 1), nodes2[0], nodes2[1]);
        g2.addEdge(new Edge("4", "6", 3), nodes2[0], nodes2[2]);
        g2.addEdge(new Edge("4", "7", 1), nodes2[0], nodes2[3]);
        g2.addEdge(new Edge("5", "6", 2), nodes2[1], nodes2[2]);
        g2.addEdge(new Edge("5", "7", 2), nodes2[1], nodes2[3]);
        g2.addEdge(new Edge("6", "7", 3), nodes2[2], nodes2[3]);
        
        Solution maxSimilarity = new SimilarityEvaluator().evaluate(g1, g2);
        System.out.println(maxSimilarity);
    }
}
