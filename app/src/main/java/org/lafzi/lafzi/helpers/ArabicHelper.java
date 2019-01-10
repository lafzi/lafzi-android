package org.lafzi.lafzi.helpers;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArabicHelper {

    private static String SYADDAH = "ّ";
    private static String SUKUN = "ْ";

    private static String FATHAH = "َ";
    private static String KASRAH = "ِ";
    private static String DHAMMAH = "ُ";

    private static String FATHATAIN = "ً";
    private static String KASRATAIN = "ٍ";
    private static String DHAMMATAIN = "ٌ";

    private static String ALIF = "ا";
    private static String ALIF_MAQSURA = "ى";
    private static String ALIF_MAD = "آ";
    private static String BA = "ب";
    private static String TA = "ت";
    private static String TA_MARBUTAH = "ة";
    private static String TSA = "ث";
    private static String JIM = "ج";
    private static String HA = "ح";
    private static String KHA = "خ";
    private static String DAL = "د";
    private static String DZAL = "ذ";
    private static String RA = "ر";
    private static String ZA = "ز";
    private static String SIN = "س";
    private static String SYIN = "ش";
    private static String SHAD = "ص";
    private static String DHAD = "ض";
    private static String THA = "ط";
    private static String ZHA = "ظ";
    private static String AIN = "ع";
    private static String GHAIN = "غ";
    private static String FA = "ف";
    private static String QAF = "ق";
    private static String KAF = "ك";
    private static String LAM = "ل";
    private static String MIM = "م";
    private static String NUN = "ن";
    private static String WAU = "و";
    private static String YA = "ي";
    private static String HHA = "ه";

    private static String HAMZAH = "ء";
    private static String HAMZAH_MAQSURA = "ئ";
    private static String HAMZAH_WAU = "ؤ";
    private static String HAMZAH_ALIF_A = "أ";
    private static String HAMZAH_ALIF_I = "إ";

    private static String UTHMANI_HIZB = "۞";
    private static String UTHMANI_SAJDAH = "۩";
    private static String UTHMANI_ALIF = "ٱ";
    private static String UTHMANI_SMALL_HAMZAH = "ٔ";
    private static String UTHMANI_SMALL_YA = "ۧ";
    private static String UTHMANI_SMALL_YA2 = "ۦ";
    private static String UTHMANI_SMALL_NUN = "ۨ";
    private static String UTHMANI_IMALAH = "۪";

    private static String[] UTHMANI_DIAC = new String[]{"\u0653", "\u0670", "\u06D6", "\u06D7", "\u06D8", "\u06D9", "\u06DA", "\u06DB", "\u06DC", "\u06DE", "\u06DF", "\u06E0", "\u06E1", "\u06E2", "\u06E3", "\u06E5", "\u06E6", "\u06E7", "\u06E8", "\u06E9", "\u06EA", "\u06EB", "\u06EC", "\u06ED"};

    public static boolean textIsLatin(CharSequence text) {
        return Charset.forName("US-ASCII").newEncoder().canEncode(text);
    }

    public static boolean isContainHarakat(String arabic) {
        for (Character c : arabic.toCharArray()) {
            if (isHurf(c)) return true;
        }
        return false;
    }

    public static String getPhonetic(String source, boolean isVocal) {
        source = uthmaniNormalize(source);
        
        source = arabicRemoveWhitespace(source);

        if (!isVocal)
        
        source = arabicRemoveTasydid(source);
        
        source = joinHurufMati(source);
        
        source = endOfAyat(source);
        
        source = tanwinSubtitution(source);
        
        source = removeMad(source);
        
        source = removeCharacterTidakDibaca(source);
        
        source = iqlabSubtitution(source);
        
        source = idghamSubtitution(source);
        
        if (!isVocal) source = removeHarakat(source);
        
        source = phoneticEncode(source).replace("X", "");
        
        return source;
    }

    /*private static String putFathah(String source) {
        final List<Character> arr = new ArrayList<>();
        for (Character c : source.toCharArray()) {
            arr.add(c);
        }

        final StringBuilder result = new StringBuilder();

        for (int i = 0; i < arr.size(); i++) {
            char curr = arr.get(i);
            char next1 = i + 1 < arr.size() ? arr.get(i + 1) : arr.get(i);
            char next2 = i + 2 < arr.size() ? arr.get(i + 2) : arr.get(i);

            if (curr == ALIF.charAt(0) && next1 == LAM.charAt(0) && next2 != ' ') {
                if
            }
        }
    }*/

    private static String uthmaniNormalize(String source) {
        source = source.replace(UTHMANI_HIZB, "");
        source = source.replace(UTHMANI_SAJDAH, "");

        source = source.replace(UTHMANI_ALIF, ALIF);
        source = source.replace(UTHMANI_SMALL_HAMZAH, HAMZAH);

        source = source.replace(UTHMANI_SMALL_YA + KASRAH, YA + KASRAH);
        source = source.replace(UTHMANI_SMALL_YA + SYADDAH + KASRAH, YA + KASRAH);
        source = source.replace(UTHMANI_SMALL_YA2 + FATHAH, YA + FATHAH);

        source = source.replace(UTHMANI_IMALAH, KASRAH);
        source = source.replace(UTHMANI_SMALL_NUN, NUN + SUKUN);

        source = source.replace("ي۟", "");
        source = source.replace(TSA + " ", TSA + SUKUN);

        for (String diac : UTHMANI_DIAC) {
            source = source.replace(diac, "");
        }

        source = source.replace("^اقْتَرَبَ", "إِقْتَرَبَ");
        return source.replace("^اقْرَ", "إِقْرَ");
    }

    private static String arabicRemoveWhitespace(String source) {
        char x = source.charAt(0);
        return source.replaceAll("\\s*", "");
    }

    private static String arabicRemoveTasydid(String source) {
        return source.replace(SYADDAH, "");
    }

    /**
     * Menggabungkan huruf idgham mutamatsilain
     * @param source: string arabic
     * @return : string arabic
     */
    private static String joinHurufMati(String source) {
        final char[] arr = source.toCharArray();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            char curr = arr[i];
            char next1 = i + 1 < source.length() ? arr[i+1] : arr[i];
            char next2 = i + 2 < source.length() ? arr[i+2] : arr[i];

            if (next1 == SUKUN.charAt(0) && curr == next2) {
                result.append(curr);
                i += 2;
            } else if (curr == next1) {
                result.append(curr);
                i += 1;
            } else {
                result.append(curr);
            }
        }

        return result.toString();
    }

    private static String endOfAyat(String source) {
        final List<Character> arr = new ArrayList<>();
        for (char c : source.toCharArray()) {
            arr.add(c);
        }

        int len = arr.size();

        if (arr.get(len-1) == ALIF.charAt(0) || arr.get(len-1) == ALIF_MAQSURA.charAt(0)) {
            arr.remove(len-1);
        } else if (arr.get(len-1) == FATHAH.charAt(0) || arr.get(len-1) == KASRAH.charAt(0) || arr.get(len-1) == DHAMMAH.charAt(0)
                || arr.get(len-1) == FATHATAIN.charAt(0) || arr.get(len-1) == KASRATAIN.charAt(0) || arr.get(len-1) == DHAMMATAIN.charAt(0)) {
            arr.set(len-1, SUKUN.charAt(0));
        }

        len = arr.size();

        if (arr.get(len-1) == FATHATAIN.charAt(0)) arr.set(len-1, FATHAH.charAt(0));
        if (arr.get(len-2) == TA_MARBUTAH.charAt(0)) arr.set(len-2, HHA.charAt(0));

        if (arr.get(0) == ALIF.charAt(0)) {
            arr.remove(0);
            arr.add(0, FATHAH.charAt(0));
            arr.add(0, HAMZAH_ALIF_A.charAt(0));
        }

        final StringBuilder sb = new StringBuilder();
        for (Character c : arr) {
            sb.append(c);
        }

        return sb.toString();
    }

    private static String tanwinSubtitution(String source) {
        source = source.replace(FATHATAIN, FATHAH + NUN + SUKUN);
        source = source.replace(KASRATAIN, KASRAH + NUN + SUKUN);
        return source.replace(DHAMMATAIN, DHAMMAH+ NUN + SUKUN);
    }

    private static String removeMad(String source) {
        final List<Character> arr = new ArrayList<>();
        for (char c : source.toCharArray()) {
            arr.add(c);
        }
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < arr.size(); i++) {
            char curr = arr.get(i);
            char next1 = i + 1 < source.length() ? arr.get(i+1) : arr.get(i);
            char next2 = i + 2 < source.length() ? arr.get(i+2) : arr.get(i);

            if ((curr == FATHAH.charAt(0) && (next1 == ALIF.charAt(0)) && (next2 != FATHAH.charAt(0) && next2 != KASRAH.charAt(0) && next2 != DHAMMAH.charAt(0)))
                    ||
                    (curr == KASRAH.charAt(0) && (next1 == YA.charAt(0)) && (next2 != FATHAH.charAt(0) && next2 != KASRAH.charAt(0) && next2 != DHAMMAH.charAt(0)))
                    ||
                    (curr == DHAMMAH.charAt(0) && (next1 == WAU.charAt(0)) && (next2 != FATHAH.charAt(0) && next2 != KASRAH.charAt(0) && next2 != DHAMMAH.charAt(0)))
                    ){
                result.append(arr.get(i));
                i += 2;
                result.append(arr.get(i));
            } else result.append(arr.get(i));
        }

        String str = result.toString();
        return str.replace(ALIF_MAD, HAMZAH_ALIF_A + FATHAH);
    }

    private static boolean isHurf(char c) {
        return c == FATHAH.charAt(0) || c == KASRAH.charAt(0) || c == DHAMMAH.charAt(0)
                || c == FATHATAIN.charAt(0) || c == KASRATAIN.charAt(0) || c == DHAMMATAIN.charAt(0)
                || c == SUKUN.charAt(0) || c == SYADDAH.charAt(0);
    }

    private static String removeCharacterTidakDibaca(String source) {
        List<Character> arr = new ArrayList<>();
        for (char c : source.toCharArray()) {
            arr.add(c);
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < arr.size(); i++) {
            char curr = arr.get(i);
            char next = i+1 < arr.size() ? arr.get(i+1) : curr;

            if (isHurf(curr) && isHurf(next) && curr != NUN.charAt(0) && curr != MIM.charAt(0) && curr != DAL.charAt(0)) {
                result.append(next);
                i++;
            } else result.append(curr);
        }

        arr.clear();
        for (char c : result.toString().toCharArray()) {
            arr.add(c);
        }

        result = new StringBuilder();
        for (int i = 0; i < arr.size(); i++) {
            char curr = arr.get(i);
            char next = i+1 < arr.size() ? arr.get(i+1) : curr;

            if (isHurf(curr) && isHurf(next) && curr != NUN.charAt(0) && curr != MIM.charAt(0) && curr != DAL.charAt(0)) {
                result.append(next);
                i++;
            } else result.append(curr);
        }
        return result.toString();
    }

    private static String iqlabSubtitution(String source) {
        source = source.replace(NUN + SUKUN + BA, MIM + SUKUN + BA);
        return source.replace(NUN + BA, MIM + SUKUN + BA);
    }

    private static String idghamSubtitution(String source) {
        source = source.replace(NUN + SUKUN + NUN, NUN);
        source = source.replace(NUN + SUKUN + MIM, MIM);
        source = source.replace(NUN + SUKUN + LAM, LAM);
        source = source.replace(NUN + SUKUN + RA, RA);

        source = source.replace(NUN + NUN, NUN);
        source = source.replace(NUN + MIM, MIM);
        source = source.replace(NUN + LAM, LAM);
        source = source.replace(NUN + RA, RA);

        source = source.replace("دُنْي", "DUNYA");
        source = source.replace("بُنْيَن", "BUNYAN");
        source = source.replace("صِنْوَن", "SINWAN");
        source = source.replace("قِنْوَن", "QINWAN");
        source = source.replace("نُنْوَلْقَلَمِ", "NUNWALQALAMI");

        source = source.replace(NUN + SUKUN + YA, YA);
        source = source.replace(NUN + SUKUN + WAU, WAU);

        source = source.replace("DUNYA", "دُنْي");
        source = source.replace("BUNYAN", "بُنْيَن");
        source = source.replace("SINWAN", "صِنْوَن");
        source = source.replace("QINWAN", "قِنْوَن");
        return source.replace("NUNWALQALAMI", "نُنْوَلْقَلَمِ");
    }

    private static String removeHarakat(String source) {
        source = source.replace(FATHAH, "");
        source = source.replace(KASRAH, "");
        source = source.replace(DHAMMAH, "");
        return source.replace(SUKUN, "");
    }

    private static String phoneticEncode(String source) {
        List<Character> arr = new ArrayList<>();
        for (char c : source.toCharArray()) arr.add(c);

        StringBuilder result = new StringBuilder();
        Map<String, Character> charMap = new HashMap<>();
        charMap.put(JIM, 'Z');
        charMap.put(ZA, 'Z');
        charMap.put(ZHA, 'Z');
        charMap.put(DZAL, 'Z');

        charMap.put(HHA, 'H');
        charMap.put(KHA, 'H');
        charMap.put(HA, 'H');

        charMap.put(HAMZAH, 'X');
        charMap.put(HAMZAH_ALIF_A, 'X');
        charMap.put(HAMZAH_ALIF_I, 'X');
        charMap.put(HAMZAH_MAQSURA, 'X');
        charMap.put(HAMZAH_WAU, 'X');
        charMap.put(ALIF, 'X');
        charMap.put(AIN, 'X');

        charMap.put(SHAD, 'S');
        charMap.put(TSA, 'S');
        charMap.put(SYIN, 'S');
        charMap.put(SIN, 'S');

        charMap.put(DHAD, 'D');
        charMap.put(DAL, 'D');

        charMap.put(TA_MARBUTAH, 'T');
        charMap.put(TA, 'T');
        charMap.put(THA, 'T');

        charMap.put(QAF, 'K');
        charMap.put(KAF, 'K');

        charMap.put(GHAIN, 'G');
        charMap.put(FA, 'F');
        charMap.put(MIM, 'M');
        charMap.put(NUN, 'N');
        charMap.put(LAM, 'L');
        charMap.put(BA, 'B');
        charMap.put(YA, 'Y');
        charMap.put(ALIF_MAQSURA, 'Y');
        charMap.put(WAU, 'W');
        charMap.put(RA, 'R');

        charMap.put(FATHAH, 'A');
        charMap.put(KASRAH, 'I');
        charMap.put(DHAMMAH, 'U');
        charMap.put(SUKUN, Character.MIN_VALUE);

        for (int i = 0; i < arr.size(); i++) {
            Character c = charMap.get(String.valueOf(arr.get(i)));
            if (c != null) result.append(c);
        }

        return result.toString();
    }

}
