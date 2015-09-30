package eu.disi.unitn.swip.verifiers;


import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by zack on 08/09/15.
 */
public class ODMinimalityVerifier {

    private static Pattern PATTERN = Pattern.compile(",");
    private static Map<String, String> naryDependencies = null;

    /**
     * Verifies if the order dependency is minimal by testing it against previously detected completely
     * non-trivial minimal order dependencies
     *
     * @param lhs The list of attributes that are on the left hand side
     * @param rhs The list of attributes that are on the right side
     * @param dependencies previously discovered completely non-trivial minimal order dependencies
     * @return true if the order dependency is minimal otherwise false
     */

    public static boolean verify(String lhs,
                                 String rhs,
                                 Map<String, String> dependencies) {

        naryDependencies = dependencies;

        String[] lhsAttributes = PATTERN.split(lhs);
        String[] rhsAttributes = PATTERN.split(rhs);

        String[] lhsPrefixes = Arrays.copyOfRange(lhsAttributes, 0, lhsAttributes.length - 1);
        String[] rhsPrefixes = Arrays.copyOfRange(rhsAttributes, 0, rhsAttributes.length - 1);

        if (lhsPrefixesToRhsVerified(lhsPrefixes, rhs)){
            if (rhsPrefixesToLhsVerified(rhsPrefixes, lhs)){
                return true;
            }
        }
        return false;
    }

    /**
     * For all the prefixes <em>p</em> of a left-hand side attribute list, the method verifies if there exists
     * an order dependency to a right hand side attribute list.
     * @param lhsPrefixes an array of attributes that will be used to construct all the prefixes of the
     *                    left-hand side attribute list
     * @param rhs the right-hand side attribute list
     * @return true if the required dependency exists otherwise false
     */
    private static boolean lhsPrefixesToRhsVerified(String[] lhsPrefixes, String rhs){
        String attributeList = "";
        int i = 0;
        int numberOfPrefixes;
        int numberOfSuccessfulODs = 0;
        if (lhsPrefixes!=null && lhsPrefixes.length > 0){
            attributeList = lhsPrefixes[i];
            numberOfPrefixes = 1;
        } else {
            return true;
        }
        while (true) {
            for (String key : naryDependencies.keySet()) {
                String val = naryDependencies.get(key);
                String[] lhsRhsVal = val.split("-->");
                if (attributeList.equals(lhsRhsVal[0])) {
                    if (rhs.equals(lhsRhsVal[1])){
                        numberOfSuccessfulODs++;
                        break;
                    }
                }
            }
            i++;
            if (lhsPrefixes.length > i) {
                numberOfPrefixes++;
                attributeList += "," + lhsPrefixes[i];
            } else {
                if (numberOfPrefixes==numberOfSuccessfulODs){
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    /**
     * For all the prefixes <em>p</em> of a right-hand side attribute list, the method verifies if there exists
     * no order dependency from a left-hand side attribute list to each prefix
     * @param rhsPrefixes an array of attribute that will be used to construct all the prefixes of the
     *                    right-hand side attribute list
     * @param lhs the left-hand side attribute list
     * @return true if the required dependency exists otherwise false
     */
    private static boolean rhsPrefixesToLhsVerified(String[] rhsPrefixes, String lhs){
        String attributeList = "";
        int i = 0;
        if (rhsPrefixes!=null && rhsPrefixes.length > 0){
            attributeList = rhsPrefixes[i];
        } else {
            return true;
        }
        while (true){
            for (String key: naryDependencies.keySet()) {
                String val = naryDependencies.get(key);
                String[] lhsRhsVal = val.split("-->");
                if (lhs.equals(lhsRhsVal[0])){
                    if (attributeList.equals(lhsRhsVal[1])) {
                        return false;
                    }
                }
            }
            i++;
            if (rhsPrefixes.length > i){
                attributeList += "," + rhsPrefixes[i];
            } else {
                break;
            }
        }
        return true;
    }

}
