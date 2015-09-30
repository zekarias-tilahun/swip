package eu.disi.unitn.swip.generators;

import java.util.regex.Pattern;

/**
 * Created by zack on 08/09/15.
 */
public class PrePostFix {

    private static Pattern PATTERN = Pattern.compile(",");

    /**
     * For two given attribute lists, the method checks if they have a common
     * prefix, according to the prefix length specified in the method argument,
     * and then creates a new attribute list by taking the common prefix and a
     * post-fix, which is obtained by taking the non-prefix part of the two
     * attribute lists
     *
     * @param attributeList1 the first attribute list
     * @param attributeList2 the second attribute list
     * @param prefixLength   the length of the common prefix that should be extracted from
     *                       the attribute lists
     * @param postfixLength  the length of the post-fixes from the attribute lists
     * @return an array (size 3) of attribute list where the first index (0) is
     * the common prefix, the second index (1) is the post-fix from the
     * first attribute list, and the third (2) is the post-fix from the
     * second attribute list
     */
    public static String[] getPreAndPostFix(String attributeList1, String attributeList2, int prefixLength,
                                             int postfixLength) {

        String[] str1Attributes = attributeList1.replaceAll(",", "").split("");
        String[] str2Attributes = attributeList2.replaceAll(",", "").split("");
        String str1Prefix = "";
        String str2Prefix = "";
        String str1Postfix = "";
        String str2Postfix = "";
        int i = 0;
        for (; i < prefixLength; i++) {
            if (i == 0) {
                str1Prefix = str1Attributes[i];
                str2Prefix = str2Attributes[i];
            } else {
                str1Prefix += "," + str1Attributes[i];
                str2Prefix += "," + str2Attributes[i];
            }
        }
        int j = i;
        for (; j < (prefixLength + postfixLength); j++) {
            if (j == i) {
                str1Postfix = str1Attributes[j];
                str2Postfix = str2Attributes[j];
            } else {
                str1Postfix += str1Attributes[j];
                str2Postfix += str2Attributes[j];
            }
        }
        if (str1Prefix.equals(str2Prefix)) {
            return new String[]{str1Prefix, str1Postfix, str2Postfix};
        }
        return null;
    }
}
