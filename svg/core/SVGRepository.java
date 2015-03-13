package svg.core;

import ivangro.shapes.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.*;
import org.w3c.dom.svg.SVGDocument;
import svg.context.CanvasContext;
import svg.detect.*;
import svg.detect.design.*;
import svg.elems.SVGElementSelector;
import svg.elems.SVGUnit;
import svg.reflection.Guidelines;
import svg.skill.*;

/**
 * Main repository for the elements of a drawing
 * @author Ivan Guerrero
 */
public class SVGRepository {
    private static SVGRepository instance = new SVGRepository();
    private List<SVGAction> actions;
    private List<SVGDAction> designActions;
    private List<List<SVGElement>> elements;
    private String svgNS;
    private SVGDocument doc;
    private Element root;
    private List<IDetector> detectors;
    private boolean displayAxis;
    private int lastCreationID = 0;
    private int currentLevel = 0;
    private CanvasContext context;
    private String selectedElement;
    private Guidelines guidelines;
    private IDrawingGenerator drawingGenerator;
    private SVGElementSelector selector;
    private ShapeStore shapeStore;
    
    private SVGRepository() {
        actions = new ArrayList<>();
        designActions = new ArrayList<>();
        elements = new ArrayList<>();
        //Adds the list for the first level elements
        elements.add(new ArrayList<SVGElement>());
        
        detectors = new ArrayList<>();
        detectors.add(new MultiElementsUnitDetector());
        detectors.add(new DensityDetector());
        detectors.add(new CollisionDetector());
        detectors.add(new HSymmetryDetector());
        detectors.add(new VSymmetryDetector());
        detectors.add(new RadialSymmetryDetector());
        detectors.add(new RhythmDetector());
        detectors.add(new InlineDetector());
        detectors.add(new PatternDetector());
        
        guidelines = new Guidelines();
        context = new CanvasContext();
        drawingGenerator = new BackgroundImageDrawingGenerator();
        //drawingGenerator = new SimpleDrawingGenerator();
        shapeStore = new ShapeStore(SVGConfig.SHAPE_STORE_FILE);
        shapeStore.loadShapes();
    }
    
    public static SVGRepository getInstance() {
        return instance;
    }
    
    public static SVGRepository getNewInstance() {
        return new SVGRepository();
    }
    
    /**
     * @return the actions
     */
    public List<SVGAction> getActions() {
        return actions;
    }

    /**
     * @return the elements
     */
    public List<SVGElement> getElements(int level) {
        List<SVGElement> list;
        list = (elements.size() > level) ? elements.get(level) : elements.get(elements.size()-1);
        
        return list;
    }
    
    public List<List<SVGElement>> getElements() {
        return elements;
    }
    
    /**
     * Applies the given action, to the given element. If it's a design action, the detectors are not run
     * @param action
     * @param element
     * @param designAction
     * @return 
     */
    public boolean applyAction(SVGAction action, SVGElement element, boolean designAction) {
        action.setRepository(this);
        action.applyAction(element);
        if (currentLevel == 0 && action.getActionText() != null)
            actions.add(action);

        if (!designAction) {
            runDetectors();
        }   
        
        return true;
    }
    
    public void runDetectors() {
        for (IDetector detector : detectors) {
            detector.detect(elements, this);
        }
    }
    
    public boolean applyAction(SVGAction action, SVGElement element) {
        return applyAction(action, element, false);
    }
    
    public void generateSVGDocument(boolean keepDesignActions) {
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        doc = (SVGDocument)impl.createDocument(svgNS, "svg", null);
        
        root = doc.getDocumentElement();
        root.setAttributeNS(null, "width", SVGConfig.CANVAS_WIDTH + "");
        root.setAttributeNS(null, "height", SVGConfig.CANVAS_HEIGHT + "");
        
        clearData(keepDesignActions);
    }
    
    public void generateSVGDocument() {
        generateSVGDocument(false);
    }
    
    public SVGDocument getDocument() {
        NodeList list = root.getChildNodes();
        for (int i=0; i<list.getLength(); i++) {
            root.removeChild(list.item(i));
        }
        
        drawingGenerator.setNameSpace(svgNS);
        drawingGenerator.setSVGDocument(doc);
        drawingGenerator.draw(root, elements, displayAxis);
        if (selector != null)
            selector.drawSelectionBoxes();
        
        return doc;
    }
    
    public SVGElement getElementAt(int x, int y) {
        for (SVGElement elem : elements.get(currentLevel)) {
            if (elem.containsPoint(x, y))
                return elem;
        }
        return null;
    }

    public void setDisplayAxis(boolean displayAxis) {
        this.displayAxis = displayAxis;
    }

    /**
     * Obtains the element with the given ID
     * @param id
     * @return 
     */
    public SVGElement getElementByID(String id) {
        for (SVGElement elem : elements.get(currentLevel)) {
            if (elem.getID().equals(id))
                return elem;
        }
        return null;
    }
    
    /**
     * Finds the group that contains the given element at the given level
     * @param elem The element to be contained in a group
     * @param level The level to which the group belongs to
     * @return The group containing the given element
     */
    public SVGElement findContainerAtLevel(SVGElement elem, int level) {
        //Obtain the first element of the group (if the element is a group)
        SVGElement simple = (elem instanceof SVGUnit) ? ((SVGUnit)elem).getSimpleElements().get(0) : elem;
        //Look for the element inside the groups at the given level
        if (level > 0 && elements.size() > level) {
            for (SVGElement e : elements.get(level)) {
                SVGUnit u = (SVGUnit)e;
                if (u.getSimpleElements().contains(simple))
                    return u;
            }
        }
        return null;
    }
    
    public String getActionDescriptions() {
        String text = "";
        for (SVGAction action : actions) {
            text += action.getActionText() + "\n";
        }
        return text;
    }
    
    public String getDetectorAnalysis() {
        String text = "";
        if (selectedElement != null && selectedElement.length() > 0)
            text += selectedElement + "\n";
        for (IDetector detector : detectors) {
            text += detector.getDescription() + "\n";
        }
        
        return text;
    }

    public void clearData(boolean keepDesignActions) {
        Logger.getGlobal().log(Level.FINE, "Initializing repository...");
        lastCreationID = 0;
        currentLevel = 0;
        actions.clear();
        if (!keepDesignActions)
            designActions.clear();
        elements.clear();
        elements.add(new ArrayList<SVGElement>());
        context.clearContexts();
        guidelines = new Guidelines();
    }
    
    public IDetector getDetector(Class detectorClass) {
        for (IDetector detector : detectors) {
            if (detectorClass.isInstance(detector))
                return detector;
        }
        
        return null;
    }
    
    public int getCurrentLevel() {
        return currentLevel;
    }
    
    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }
    
    public void selectElement(SVGElement selection) {
        selectElement(selection, true);
    }
    
    /**
     * Stores the given element to be marked as the seleted element
     * @param selection Element to be displayed as selected
     * @param unselectPrevious If TRUE the previously selected element is removed
     */
    public void selectElement(SVGElement selection, boolean unselectPrevious) {
        if (unselectPrevious) {
            //Unselects all the previously selected elements
            for (SVGElement elem : getElements(currentLevel)) {
                if (elem != null && elem.getColor().equals(SVGConfig.SELECTION_COLOR))
                    elem.setColor(SVGConfig.FORE_COLOR);
            }
        }
        //Adds the selection to the selector to create a surrounding box
        if (selection != null) {
            selection.setColor(SVGConfig.SELECTION_COLOR);
            selectedElement = (selection instanceof SVGUnit) ? ((SVGUnit)selection).toStringWithElements() : "";
            if (unselectPrevious)
                selector = new SVGElementSelector(svgNS, doc, root);
            selector.addSelection(selection);
        }
        else {
            selectedElement = "";
            if (selector != null)
                selector.clearSelection();
        }
    }
    
    /**
     * Obtains the element employed to keep track of the selected elements
     * @return 
     */
    public SVGElementSelector getElementSelector() {
        return selector;
    }
    
    /**
     * Returns the number of detected levels in the drawing
     * @return Number of levels
     */
    public int getLevels() {
        return elements.size();
    }
    
    public CanvasContext getContext() {
        return context;
    }

    public int getLastCreationID() {
        return lastCreationID;
    }
    
    /**
     * Generates a new Element to be included in a SVG drawing with the given data
     * and increments the lastCreationID in one
     * @param myElement
     * @return 
     */
    public Element createElement(SVGElement myElement) {
        lastCreationID++;
        return doc.createElementNS(svgNS, myElement.getSVGType());
    }
    
    public Guidelines getGuidelines() {
        return guidelines;
    }
    
    public void addDesignAction(SVGDAction action) {
        designActions.add(action);
    }
    
    public String getDesignActionDescriptions() {
        String text = "";
        for (SVGDAction action : designActions) {
            text += action.getActionText() + "\n";
        }
        return text;
    }

    public List<SVGDAction> getDesignActions() {
        return designActions;
    }
    
    public void setDrawingGenerator(IDrawingGenerator drawingGenerator) {
        this.drawingGenerator = drawingGenerator;
    }
    
    public ShapeStore getShapeStore() {
        return shapeStore;
    }
}