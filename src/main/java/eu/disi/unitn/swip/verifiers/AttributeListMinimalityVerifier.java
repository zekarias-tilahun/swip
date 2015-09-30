package eu.disi.unitn.swip.verifiers;

import eu.disi.unitn.swip.util.Utility;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zack on 08/09/15.
 */
public class AttributeListMinimalityVerifier {

    /**
     * Checks whether an attribute list is minimal by leveraging previously
     * identified order dependencies and prune it if it is not minimal.
     *
     * @param attributeList an attribute list
     * @param naryDependencies All the previously discovered dependencies
     * @return true if an attribute list is minimal otherwise the attribute list
     * is pruned/ignored and the caller is notified by returning false
     */
    public static boolean verify(String attributeList, Map<String, String> naryDependencies){

        String reverseString = attributeList.substring(attributeList.indexOf(',') + 1, attributeList.length());
        reverseString += "," + attributeList.substring(0, attributeList.indexOf(','));
        String patternForAnyLetter = "[A-Z]*";
        Pattern pattern = Pattern.compile(
                patternForAnyLetter + attributeList.replaceAll(",", patternForAnyLetter) + patternForAnyLetter);
        Pattern pattern1 = Pattern.compile(
                patternForAnyLetter + reverseString.replaceAll(",", patternForAnyLetter) + patternForAnyLetter);
        for (String dependency : naryDependencies.keySet()) {

            String attrList = naryDependencies.get(dependency).replaceAll("-->", ",");
            Matcher match = pattern.matcher(attrList);
            /**
             * If the argument attribute list embeds an already existing
             * dependency pattern it is not minimal, hence return true
             * to flag that it is pruned
             */
            if (match.find()) {
                return false;
            } else {
                match = pattern1.matcher(attrList);
                if (match.find()) {
                    return false;
                }
            }
        }
        /**
         * Note pruned
         */

        return true;
    }

    public static boolean isMinimalList(String attributeList, Map<String, String> dependencies){

        Set<String> attributes = Utility.stringToSet(attributeList);
        //System.out.println(attributeList);
        for (String dependency: dependencies.keySet()) {
            Set<String> dattributes = Utility.stringToSet(dependency);
            attributes.retainAll(dattributes);
            if (attributes.size()>1) {
                return false;
            }
        }
        return true;
    }
}
