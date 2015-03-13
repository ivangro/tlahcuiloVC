package svg.engagement;

import edu.uci.ics.jung.graph.Graph;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import svg.context.CanvasContext;
import svg.context.SVGEdge;
import svg.core.SVGElement;

/**
 * Class employed to store the contexts of a drawing as atoms
 * @author Ivan Guerrero
 */
public class SVGAtomStore {
    private static SVGAtomStore instance = new SVGAtomStore();
    private Map<Integer, SVGCell> cells;
    public Map<SVGAtom, Graph<SVGElement, SVGEdge>> atomContexts;
    /** Stores the number of contexts analyzed to generate the stored atoms. */
    private int contextCount;
    
    private SVGAtomStore() {
        cells = new HashMap<>();
    }
    
    public static SVGAtomStore getInstance() {
        return instance;
    }
    
    public void generateAtoms(CanvasContext context) {
        int level = 0;
        for (Graph<SVGElement, SVGEdge> graph : context.getContexts()) {
            if (graph.getVertexCount() > 0) {
                SVGCell cell = getCell(graph.getEdgeCount());
                SVGAtom atom = new SVGAtom(graph, level);
                cell.addAtom(atom);
            }
            level++;
        }
    }
    
    public Map<Integer, SVGCell> getCells() {
        return cells;
    }
    
    private SVGCell getCell(int elements) {
        SVGCell cell;
        if (cells.containsKey(elements)) {
            cell = cells.get(elements);
        }
        else {
            cell = new SVGCell(elements);
            cells.put(elements, cell);
        }
        return cell;
    }
    
    public void saveAtoms(String filename) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(filename);
            oos = new ObjectOutputStream(fos);
            for (SVGCell cell : cells.values()) {
                oos.writeObject(cell);
                Logger.getLogger(SVGAtomStore.class.getName()).log(Level.INFO, "Stored {0}", cell.toString());
            }
        } catch (IOException ex) {
            Logger.getLogger(SVGAtomStore.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(SVGAtomStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void loadAtoms(String filename) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(filename);
            ois = new ObjectInputStream(fis);
            cells.clear();
            SVGCell cell;
            do {
                cell = (SVGCell)ois.readObject();
                if (cell != null)
                    cells.put(cell.getElements(), cell);
            } while (cell != null);
        } catch (EOFException eof) {
            if (fis != null && ois != null) {
                try {
                    fis.close();
                    ois.close();
                    generateAtomContexts();
                    Logger.getLogger(SVGAtomStore.class.getName()).log(Level.INFO, "Cells loaded: {0}", cells.size());
                } catch (IOException ex) {
                    Logger.getLogger(SVGAtomStore.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SVGAtomStore.class.getName()).log(Level.WARNING, "No atoms found");
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(SVGAtomStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addAtom(SVGAtom atom) {
        SVGCell cell = getCell(atom.getGraph().getEdgeCount());
        atom.setID("C" + cell.getElements() + "-A" + atom.getLevel() + "-" + cell.getAtoms().size());
        cell.addAtom(atom);
    }
    
    public void generateAtomContexts() {
        atomContexts = new HashMap<>();
        
        for (SVGCell cell : getCells().values()) {
            for (SVGAtom atom : cell.getAtoms()) {
                atomContexts.put(atom, atom.getGraph());
                int atomContext;
                try {
                    atomContext = Integer.parseInt(atom.getContextID().split("-")[1]);
                    if (atomContext > contextCount)
                        contextCount = atomContext;
                } catch (Exception e) {}
            }
        }
    }
    
    public Map<SVGAtom, Graph<SVGElement, SVGEdge>> getAtomContexts() {
        if (atomContexts == null)
            generateAtomContexts();
        return atomContexts;
    }
    
    public int getContextCount() {
        return contextCount;
    }

    /**
     * Obtains an atom that shares the same context with the given atom and 
     * that contains the context where the design action took place.
     * @param atom An atom with the same context than the atom to be retrieved.
     * @return An atom with the same context than the given one and that 
     * also contains the context where the design action took place.
     */
    public SVGAtom findActionAtom(SVGAtom atom) {
        SVGAtom actionAtom = null;
        for (SVGCell cell : cells.values()) {
            for (SVGAtom a : cell.getAtoms()) {
                if (a.getContextID().equals(atom.getContextID()) && a.isActionLevel())
                    return a;
            }
        }
        return actionAtom;
    }
}