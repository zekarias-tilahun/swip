package eu.disi.unitn.swip.generators;

import java.util.Map;

/**
 * Created by zack on 08/09/15.
 */
public class RHS {
    /**
     * Generates the right-hand side out of the list of attributes by ignoring those whose indexes are given. Note that
     * the order the attributes are specified in the input is respected in the output
     *
     * @param attributes         the list of attributes
     * @param lhsAttributesIndex the index of the attributes that are used in the left hand side
     * @return a string sequence of attributes
     * @see {SlidingWindowWithPivot.
     * generateLhsAndRhs(String[] attributes, Map<String, String> naryD,List<String> candidateAtList)}
     */
    public static String generateRhs(String[] attributes, Map<Integer, String> lhsAttributesIndex) {
        String rhs = "";
        /**
         * Generate the right hand side attribute list from the remaining index list
         */
        for (int i = 0; i < attributes.length; i++) {
            /**
             * if index i is contained in the lhsAttributesIndex, that is the attribute corresponding to this
             * index has been used on the left-hand side, otherwise the attribute in the index i should be
             * used for the right hand side attribute list generation
             */
            if (lhsAttributesIndex.get(i) == null) {
                if (rhs.equals("")) {
                    rhs = attributes[i];
                } else {
                    rhs += "," + attributes[i];
                }
            }
        }
        return rhs;
    }
}
