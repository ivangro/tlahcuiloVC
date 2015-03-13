package svg.reflection;

import java.util.Objects;
import svg.core.SVGElement;

/**
 *
 * @author Ivan Guerrero
 */
public class SVGPair {
    private SVGElement a, b;
    
    public SVGPair(SVGElement a, SVGElement b) {
        this.a = a;
        this.b = b;
    }
    
    public SVGElement getA() { return a; }
    public SVGElement getB() { return b; }
    
    @Override
    public boolean equals(Object obj) {
        boolean res = false;
        if (obj instanceof SVGPair) {
            SVGPair pair = (SVGPair)obj;
            res = (pair.a.equals(a) && pair.b.equals(b)) || (pair.a.equals(b) && pair.b.equals(a));
        }
        return res;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.a);
        hash = 71 * hash + Objects.hashCode(this.b);
        return hash;
    }
}