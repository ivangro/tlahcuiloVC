package ivangro.utils;

import com.google.common.collect.Collections2;
import java.util.*;

public class Permutator<T> {
    public Permutator() {
        
    }
    
    public Collection<List<T>> obtainPermutations(Collection<T> elements) {
        return Collections2.permutations(elements);
    }
    
    public static void main(String[] args) {
        Permutator<String> p = new Permutator<>();
        Collection<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        for (List<String> l : p.obtainPermutations(list)) {
            System.out.println(l);
        }
    }
}