package eu.disi.unitn.swip.generators;

import eu.disi.unitn.swip.verifiers.AttributeListMinimalityVerifier;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zack on 08/09/15.
 */
public class SlidingWindowWithPivot {
    private static Map<Integer, String> prunedAttributesList;
    private static Map<Integer, String> candidateAttributeList = null;
    private static Map<String, String> naryDependencies = null;

    /**
     * Given an attribute list, the method generates all possible left-hand and
     * right-hand sides
     *
     * @param attributes list of attributes
     * @return a hash table of the left-hand and right-hand side attribute list
     */
    public static Map<String, String> generateLhsAndRhs(String[] attributes,
                                                        Map<String, String> naryD,
                                                        Map<Integer, String> candidateAtList,
                                                        Map<Integer, String> prunedList) {
        naryDependencies = naryD;
        candidateAttributeList = candidateAtList;
        prunedAttributesList = prunedList;
        Map<String, String> lhsToRhsMap = new HashMap<>();
        int largestWindowSize = attributes.length - 1;
        for (int i = 1; i <= largestWindowSize; i++) {
            lhsToRhsMap.putAll(lhsRhsGeneratorSlidingWindowWithPivot(attributes, i));
        }
        return lhsToRhsMap;
    }

    /**
     * The following is the sliding window with fixed and sliding pivots algorithm, given a list of attributes the
     * algorithm generates all the possible left-hand right-hand side pairs for the given window size.
     * The generation of the pairs is carried out by respecting the attribute order induced by the specified list of
     * attributes.
     * <p>
     * <emphasize>Example1:</emphasize>  given list of attributes = [A,B,C] the result will look like
     * <p>
     * (A,BC), (B,AC), (C=AB) => for window size = 1 and
     * </p>
     * <p>
     * (BC,A), (AB,C), (AC,B) => for window size = 2
     * </p>
     * </p>
     * <p>
     * <p>
     * Example2: given list of attributes = [C,B,A] the result will look like
     * (A,CB), (B,CA), (C,BA) => for window size = 1 and
     * (CA,B), (BA,C), (CB,A) => for window size = 2
     * </p>
     *
     * @param attributes the list of attributes
     * @param windowSize a window size which determines the number of attributes which are on the left-hand size
     * @return a list left-hand right-hand side attribute list pairs internally represented as a hash map
     */
    private static Map<String, String> lhsRhsGeneratorSlidingWindowWithPivot(String[] attributes, int windowSize) {
        Map<String, String> lhsToRhsMap = new HashMap<>();
        if (windowSize == 1 || windowSize == attributes.length - 1) {
            lhsToRhsMap.putAll(generateSpecialCaseLhsRhs(attributes, windowSize));
        }
        /**
         * For every fixed pivot index
         */
        else {
            for (int fixedPivot = 0; fixedPivot < attributes.length; fixedPivot++) {
                /**
                 * For each sliding pivot index
                 */
                for (int slidingPivot = 0; slidingPivot < attributes.length; slidingPivot++) {
                    Map<Integer, String> lhsAttributesIndex = new HashMap<>();
                    String lhs = "";
                    String rhs = "";
                    if (fixedPivot == slidingPivot)
                        continue;

                    /**
                     * For every attribute taken as the first in the sliding window
                     */
                    for (int firstAttributeIndex = 0; firstAttributeIndex < attributes.length; firstAttributeIndex++) {
                        if (firstAttributeIndex == fixedPivot || firstAttributeIndex == slidingPivot)
                            continue;
                        String firstAttribute = attributes[firstAttributeIndex];
                        lhs = firstAttribute;

                        lhsAttributesIndex.put(firstAttributeIndex, lhs);
                        /**
                         * For all the trailing attributes in the sliding window
                         */
                        for (int trailingAttributesIndex = 0; trailingAttributesIndex < attributes.length; trailingAttributesIndex++) {
                            if (trailingAttributesIndex == fixedPivot || trailingAttributesIndex == slidingPivot || trailingAttributesIndex <= firstAttributeIndex)
                                continue;
                            if (lhs.length() == windowSize) {
                                rhs = RHS.generateRhs(attributes, lhsAttributesIndex);
                                int candidatesKey = (lhs+"cand"+rhs).hashCode();
                                String attrList = lhs + rhs;
                                /**
                                 * Test if the attribute has already been pruned
                                 */
                                if (prunedAttributesList.get((lhs+"pruned"+rhs).hashCode()) == null) {
                                    /**
                                     * Verification comment
                                     * Verifying if the attribute list is minimal, if the verification failed then
                                     * the attribute list will be pruned and added to the pruned attributes list,
                                     * otherwise it will be added to the left-hand right-hand side pairs to be checked
                                     * for order dependency and the candidate attribute list that will be consumed
                                     * in the next level.
                                     */
                                    if (AttributeListMinimalityVerifier.isMinimalList(lhs, naryDependencies) &&
                                            AttributeListMinimalityVerifier.isMinimalList(rhs, naryDependencies)) {
                                        prunedAttributesList.put((lhs+"pruned"+rhs).hashCode(), attrList);
                                    } else {
                                        lhsToRhsMap.put(lhs, rhs);
                                        if (AttributeListMinimalityVerifier.isMinimalList(attrList, naryDependencies))
                                            candidateAttributeList.put(candidatesKey, attrList);
                                    }
                                }
                                lhs = firstAttribute;
                                lhsAttributesIndex = new HashMap<>();
                            } else {
                                if (lhs.equals("")) {
                                    lhs = attributes[trailingAttributesIndex];
                                    lhsAttributesIndex.put(trailingAttributesIndex, attributes[trailingAttributesIndex]);
                                } else {
                                    lhs += "," + attributes[trailingAttributesIndex];
                                    lhsAttributesIndex.put(trailingAttributesIndex, attributes[trailingAttributesIndex]);
                                }
                                if (lhs.length() == windowSize) {
                                    rhs = RHS.generateRhs(attributes, lhsAttributesIndex);
                                    String attrList = lhs + rhs;
                                    int candidatesKey = (lhs+"cand"+rhs).hashCode();
                                    /**
                                     * Check the comment above
                                     */
                                    if (prunedAttributesList.get((lhs+"pruned"+rhs).hashCode()) == null) {
                                        /**
                                         * Check the comment above, Verification comment
                                         */
                                        if (AttributeListMinimalityVerifier.isMinimalList(lhs, naryDependencies) &&
                                                AttributeListMinimalityVerifier.isMinimalList(rhs, naryDependencies)) {
                                            prunedAttributesList.put((lhs+"pruned"+rhs).hashCode(), attrList);
                                        } else {
                                            lhsToRhsMap.put(lhs, rhs);
                                            if (AttributeListMinimalityVerifier.isMinimalList(attrList, naryDependencies))
                                                candidateAttributeList.put(candidatesKey, attrList);
                                        }
                                    }
                                    lhs = firstAttribute;
                                    lhsAttributesIndex = new HashMap<>();
                                }
                            }
                        }
                        if (lhs.length() == windowSize) {
                            rhs = RHS.generateRhs(attributes, lhsAttributesIndex);
                            String attrList = lhs + rhs;
                            int candidatesKey = (lhs+"cand"+rhs).hashCode();
                            /**
                             * Check the comment above for the same condition
                             */
                            if (prunedAttributesList.get((lhs+"pruned"+rhs).hashCode()) == null) {
                                /**
                                 * Check the comment above, Verification comment
                                 */
                                if (AttributeListMinimalityVerifier.isMinimalList(lhs, naryDependencies) &&
                                        AttributeListMinimalityVerifier.isMinimalList(rhs, naryDependencies)) {
                                    prunedAttributesList.put((lhs+"pruned"+rhs).hashCode(), attrList);
                                } else {
                                    lhsToRhsMap.put(lhs, rhs);
                                    if (AttributeListMinimalityVerifier.isMinimalList(attrList, naryDependencies))
                                        candidateAttributeList.put(candidatesKey, attrList);
                                }
                            }
                            lhsAttributesIndex = new HashMap<>();
                        }
                    }
                }
            }
        }
        return lhsToRhsMap;
    }




    /**
     * A special method to generate the left-hand right hand side pairs for window sizes 1 and attribute.length - 1
     *
     * @param attributes list of attributes
     * @param windowSize window size
     * @return a list of left-hand right-hand side pairs internally represented as hash map.
     */
    private static Map<String, String> generateSpecialCaseLhsRhs(String[] attributes, int windowSize) {
        Map<String, String> lhsToRhsMap = new HashMap<>();
        String oneSide = "";
        String theOtherSide = "";

        for (int fixedPoint = 0; fixedPoint < attributes.length; fixedPoint++) {
            int i = 0;
            for (; i < attributes.length; i++) {
                if (i != fixedPoint) {
                    if (oneSide.equals("")) {
                        oneSide = attributes[i];
                    } else {
                        oneSide += "," + attributes[i];
                    }
                } else {
                    theOtherSide = attributes[fixedPoint];
                }
            }
            if (windowSize == attributes.length - 1) {
                String attrList = oneSide + theOtherSide;
                int candidatesKey = (oneSide+"cand"+theOtherSide).hashCode();
                if (AttributeListMinimalityVerifier.isMinimalList(oneSide, naryDependencies) &&
                        AttributeListMinimalityVerifier.isMinimalList(theOtherSide, naryDependencies)){
                    lhsToRhsMap.put(oneSide, theOtherSide);
                    if (AttributeListMinimalityVerifier.isMinimalList(attrList, naryDependencies))
                        candidateAttributeList.put(candidatesKey, attrList);
                }
            } else {
                String attrList = theOtherSide + oneSide;
                int candidatesKey = (theOtherSide+"cand"+oneSide).hashCode();
                if (AttributeListMinimalityVerifier.isMinimalList(theOtherSide, naryDependencies) &&
                        AttributeListMinimalityVerifier.isMinimalList(oneSide, naryDependencies)) {
                    lhsToRhsMap.put(theOtherSide, oneSide);
                    if (AttributeListMinimalityVerifier.isMinimalList(attrList, naryDependencies)) {
                        candidateAttributeList.put(candidatesKey, attrList);
                    }
                }
            }
            oneSide = "";
            theOtherSide = "";
        }
        return lhsToRhsMap;
    }
}
