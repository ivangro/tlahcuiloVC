package ivangro.utils;

import java.util.List;

/**
 *
 * @author Ivan Guerrero
 */
public class Statistics {
    
    /**
     * Method for calculating the standard deviation from a list of numbers
     * @param list The list of numbers
     * @return The standard deviation from the list
     */
    public static double standardDeviation(List<Double> list) {
        double[] arr = new double[list.size()];
        for (int i=0; i<arr.length; i++) {
            arr[i] = list.get(i);
        }
        return standardDeviation(arr);
    }
    
    /**
     * Method for calculating the standard deviation from a list of numbers
     * @param list The list of numbers
     * @return The standard deviation from the list
     */
    public static double standardDeviation(double[] list) {
        double sd = 0;
        double avg = 0;
        
        if (list.length > 0) {
            for (double number : list) {
                avg += number;
            }
            avg /= list.length;

            for (double number : list) {
                sd += Math.pow(number - avg, 2);
            }

            sd /= list.length;
            sd = Math.sqrt(sd);
        }
        
        return sd;
    }
}
