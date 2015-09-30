package eu.disi.unitn.swip.algorithms;

import eu.disi.unitn.swip.model.Echocardiogram;
import eu.disi.unitn.swip.generators.PrePostFix;
import eu.disi.unitn.swip.generators.SlidingWindowWithPivot;
import eu.disi.unitn.swip.verifiers.ODMinimalityVerifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NAryDependency {

    private static Map<String, String> naryDependencies = new HashMap<>();
    private static Map<Integer, String> candidateAttributeList = null;
    private static Map<String, List<Echocardiogram>> attrListToEchocardiogramTupleMap;
    private static Map<Integer, String> prunedAttributesList = new HashMap<>();
    private static Map<Integer, Boolean> attributeListExitStatus;

    /**
     * For each level n, starting from 2, the method controls the whole n-ary
     * order dependency execution
     *
     * @param n                            the current level
     * @param previousDependencies         order dependencies that are discovered before the current
     *                                     level
     * @param ListOfcandidateAttributeList list of candidate minimal attribute list from previous iterations
     */

    public static void execute(int n, Map<String, String> previousDependencies,
                               Map<Integer, String> ListOfcandidateAttributeList) {
        //naryDependencies = new HashMap<>();
        //naryDependencies.putAll(previousDependencies);
        if (naryDependencies.size()==0)
            naryDependencies = previousDependencies;
        attributeListExitStatus = new HashMap<>();
        int numberOfDependenciesAtLevel = 0;
        System.out.println("[Info]: Detecting  order dependencies at level " + n + ": ...");
        System.out.println("N:" + n);
        int prefixLength = n - 1;
        int postfixLength = n - prefixLength;

        /**
         * A hash map for a list of attributes and the associated values (tuple).
         */
        attrListToEchocardiogramTupleMap = new HashMap<>();

        candidateAttributeList = new HashMap<>();

        boolean exitWithDependency;

        for (String attrList : ListOfcandidateAttributeList.values()) {
            for (String attrList1 : ListOfcandidateAttributeList.values()) {
                if (!attrList.equals(attrList1)) {
                    String[] preAndPostFix = PrePostFix.getPreAndPostFix(attrList, attrList1, prefixLength, postfixLength);
                    if (preAndPostFix != null) {

                        Map<String, String> lhsRhs = SlidingWindowWithPivot.generateLhsAndRhs(preAndPostFix,
                                naryDependencies, candidateAttributeList, prunedAttributesList);
                        for (String lhs : lhsRhs.keySet()) {
                            String rhs = lhsRhs.get(lhs);

                            exitWithDependency = checkCompletelyNonTrivialMinimalOD(lhs, rhs);
                            if (exitWithDependency) {
                                numberOfDependenciesAtLevel++;
                                System.out.println("[Info]: " + n + "-ary Order dependency discovered [" + lhs
                                        + rhs + "]" + lhs + " --> " + rhs);
                                String attributeListKey = lhs.replaceAll(",", "")
                                        + rhs.replaceAll(",", "");
                                String attributeListValue = lhs + "-->" + rhs;
                                naryDependencies.put(attributeListKey, attributeListValue);
                                int prunedKey = (lhs+"pruned"+rhs).hashCode();
                                prunedAttributesList.put(prunedKey, lhs + rhs);
                                //System.out.println(candidateAttributeList.get(lhs+"cand"+rhs));
                                //candidateAttributeList.get()
                                //System.out.println("");
                            }
                        }
                    }
                }
            }
        }
        if (numberOfDependenciesAtLevel==0)
            candidateAttributeList = new HashMap<>();
    }

    /**
     * The Method checks if a completely non trivial order dependency exists
     * between the left-hand side right-hand side pair of its argument,
     *
     * @param lhs left-hand side
     * @param rhs right-hand side
     * @return true if there exists a completely non trivial order dependency
     * otherwise it returns false if the dependency doesn't exist or the
     * dependency is already tested
     */
    private static boolean checkCompletelyNonTrivialMinimalOD(String lhs, String rhs) {
        String rhsCurrentValue;
        String rhsPrevValue = null;
        boolean exitStatus = true;
        //TODO: The verify method is not complete yet
        int testedKey = (lhs+"tested"+rhs).hashCode();
        int prunedKey = (lhs+"pruned"+rhs).hashCode();
        if (prunedAttributesList.get(prunedKey)!=null){
            attributeListExitStatus.put(testedKey, false);
            return false;
        }
        if (ODMinimalityVerifier.verify(lhs, rhs, naryDependencies)) {
            Boolean val = attributeListExitStatus.get(testedKey);
            if (val == null) {
                List<Echocardiogram> echocardiogramTuples = attrListToEchocardiogramTupleMap.get(lhs);
                if (echocardiogramTuples == null) {
                    echocardiogramTuples = Echocardiogram.orderBy(lhs);
                }

                attrListToEchocardiogramTupleMap.put(lhs, echocardiogramTuples);
                for (int index=0;index<echocardiogramTuples.size();index++) {
                    Echocardiogram echocardiogramTuple = echocardiogramTuples.get(index);
                    rhsCurrentValue = Echocardiogram.getTupleCorrespondingToAttributeList(echocardiogramTuple, rhs);
                    if (rhsPrevValue == null) {
                        rhsPrevValue = rhsCurrentValue;
                        continue;
                    }

                    if (Echocardiogram.compareTuples(rhsCurrentValue, rhsPrevValue) < 0) {
                        exitStatus = false;
                        break;
                    }
                    rhsPrevValue = rhsCurrentValue;
                }
                attributeListExitStatus.put(testedKey, exitStatus);

            } else {
                return attributeListExitStatus.get(testedKey);
            }
        } else {
            prunedAttributesList.put(prunedKey, lhs+rhs);
            exitStatus = false;
            attributeListExitStatus.put(testedKey, exitStatus);
        }
        return exitStatus;
    }

    public static Map<String, String> getNaryDependencies() {
        return naryDependencies;
    }

    public static Map<Integer, String> getCandidateAttributeList() {
        return candidateAttributeList;
    }
}
