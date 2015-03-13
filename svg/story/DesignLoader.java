package svg.story;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.*;
import svg.actions.design.*;
import svg.core.SVGDAction;
import svg.core.SVGRepository;

/**
 * Class to reproduce a drawing from a story saved into a file
 * @author Ivan Guerrero
 */
public class DesignLoader {
    private Scanner scanner;
        
    public DesignLoader() {
    }
    
    public void loadStory(File file, SVGRepository repo, boolean execute) throws FileNotFoundException {
        scanner = new Scanner(file);
        //repo.generateSVGDocument();
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            if (!nextLine.startsWith("//")) {
                boolean result = parseAction(nextLine, repo, execute);
                if (!result) {
                    Logger.getGlobal().log(Level.WARNING, "Action not parsed {0}", nextLine);
                }
            }
        }
    }

    public boolean parseAction(String nextLine, SVGRepository repo, boolean execute) {
        //Class[] classes = getAllClasses("svg.actions.design");
        Class[] classes = getAllClasses();
        for (Class myClass : classes) {
            if (myClass.getSuperclass().equals(SVGDAction.class)) {
                try {
                    SVGDAction action;
                    action = (SVGDAction)myClass.newInstance();
                    if (action.parseAction(nextLine, repo, execute)) {
                        repo.runDetectors();
                        return true;
                    }
                } catch (InstantiationException | IllegalAccessException ex) {
                    Logger.getGlobal().log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    private Class[] getAllClasses() {
        Class[] classes = {DActionAlign.class, DActionBalance.class, DActionCopyUnit.class,
                           DActionCreateUnit.class, DActionDelete.class, DActionDistribute.class,
                           DActionHMirroring.class, DActionHSymmetry.class, DActionRadialMirroring.class,
                           DActionVMirroring.class, DActionVSymmetry.class};
        return classes;
    }

    /**
     * Method obtained from: http://www.coderanch.com/t/328491/java/java/classes-package-programatically
     * @author Jaikiran Pai
     */
    private Class[] getAllClasses(String pckgname) {  
        try{  
            ArrayList classes=new ArrayList();   
            // Get a File object for the package   
            File directory=null;   
            try {   
                directory=new File(Thread.currentThread().getContextClassLoader().getResource(pckgname.replace('.', '/')).getFile());   
            } catch(NullPointerException x) {   
                System.out.println("Nullpointer");  
                throw new ClassNotFoundException(pckgname+" does not appear to be a valid package");   
            }   
            if(directory.exists()) {   
                // Get the list of the files contained in the package   
                String[] files=directory.list();   
                for(int i=0; i<files.length; i++) {   
                    // we are only interested in .class files   
                    if(files[i].endsWith(".class")) {   
                        // removes the .class extension   
                        classes.add(Class.forName(pckgname+'.'+files[i].substring(0, files[i].length()-6)));   
                    }   
                }   
            } 
            else {   
                System.out.println("Directory does not exist");  
                throw new ClassNotFoundException(pckgname+" does not appear to be a valid package");   
            }   
            
            Class[] classesA=new Class[classes.size()];   
            classes.toArray(classesA);   
            return classesA;  
        } catch (Exception e) {  
            Logger.getGlobal().log(Level.SEVERE, "Error in action classes loading process");
        } 
        return null;
    }  
}