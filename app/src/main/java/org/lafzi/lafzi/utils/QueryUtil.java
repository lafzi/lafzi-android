package org.lafzi.lafzi.utils;

/**
 * Created by alfat on 21/04/17.
 */

public class QueryUtil {

    public static final String normalizeQuery(final String query, final boolean isVocal){

        final String firstTransform = query.toUpperCase()
                .replaceAll("\\s+", " ") // all whitespace -> single space
                .replaceAll("\\-", " ") // char (-) -> single space
                .replaceAll("[^A-Z`'\\-\\s]", ""); // all chars beside alphabet, (`), (') -> removed

        final String secondTransform = firstTransform
                .replace("O", "A")
                .replace("E", "I");

        final String thirdTransform = secondTransform
                .replaceAll("(B|C|D|F|G|H|J|K|L|M|N|P|Q|R|S|T|V|W|X|Y|Z)\\s?\\1+", "$1")
                .replaceAll("(KH|CH|SH|TS|SY|DH|TH|ZH|DZ|GH)\\s?\\1+", "$1");

        final String fourthTransform = thirdTransform
                .replaceAll("(A|I|U|E|O)\\1+", "$1");

        final String fifthTransform = fourthTransform
                .replace("AI", "AY")
                .replace("AU", "AW");

        final String sixthTransform = fifthTransform
                .replaceAll("^(A|I|U)", "X$1")
                .replaceAll("\\s(A|I|U)", "X$1")
                .replaceAll("I(A|U)", "IX$1")
                .replaceAll("U(A|I)", "UX$1");

        final String seventhTransform = sixthTransform
                .replaceAll("(A|I|U)NG\\s?(D|F|J|K|P|Q|S|T|V|Z)", "$1N$2");

        final String eightTransform = seventhTransform
                .replaceAll("N\\s?B", "MB");

        final String nineTransform = eightTransform
                .replace("DUNYA", "DUN_YA")
                .replace("BUNYAN", "BUN_YAN")
                .replace("QINWAN", "KIN_WAN")
                .replace("KINWAN", "KIN_WAN")
                .replace("SINWAN", "SIN_WAN")
                .replace("SHINWAN", "SIN_WAN")
                .replaceAll("N\\s?(N|M|L|R|Y|W)", "$1")
                .replace("DUN_YA", "DUNYA")
                .replace("BUN_YAN", "BUNYAN")
                .replace("KIN_WAN", "KINWAN")
                .replace("SIN_WAN", "SINWAN");

        final String tenthTransform = nineTransform
                .replaceAll("KH|CH", "H")
                .replaceAll("SH|TS|SY", "S")
                .replaceAll("DH", "D")
                .replaceAll("ZH|DZ", "Z")
                .replaceAll("TH", "T")
                .replaceAll("NG(A|I|U)", "X$1")
                .replaceAll("GH", "G");

        final String eleventhTransform = tenthTransform
                .replaceAll("'|`", "X")
                .replaceAll("Q|K", "K")
                .replaceAll("F|V|P", "F")
                .replaceAll("J|Z", "Z");

        final String twelfthTransform = eleventhTransform
                .replaceAll("\\s", "");

        if (!isVocal){
            final String thirteenthTransform = twelfthTransform
                    .replaceAll("A|I|U", "");
            return thirteenthTransform;
        }
        return twelfthTransform;
    }

}
