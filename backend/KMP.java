

public class KMP {
    static int ERROR_FLAG = 0;
    static int ALGO_FLAG = 1;
    static int DATA_FLAG = 2;
    static int TIME_FLAG = 3;
    static int PATTERN_FOUND = 4;
    static int PATTERN_NOTFOUND = 5;
    public static void KMPSearch(String pattern, String text) {
        try{

            int M = pattern.length();
            int N = text.length();
            int[] lps = new int[M];
            int j = 0; // index for pattern
        computeLPSArray(pattern, M, lps);
        int i = 0; // index for text
        
        boolean flag = false;
        
        while (i < N) {
            if (pattern.charAt(j) == text.charAt(i)) {
                j++;
                i++;
            }
            if (j == M) {
                // System.out.println("Found pattern at index " + (i - j));
                flag = true;
                j = lps[j - 1];
            } else if (i < N && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0)
                j = lps[j - 1];
                else
                i++;
            }
        }
        
        if(!flag){
            System.out.println(DATA_FLAG+": "+PATTERN_NOTFOUND);
        }else{
            System.out.println(DATA_FLAG+": "+PATTERN_FOUND);
        }
    }catch(Exception e){
        System.out.println(ERROR_FLAG+": An error occured at KMP algo");
    }
}

    private static void computeLPSArray(String pattern, int M, int[] lps) {
        int len = 0;
        int i = 1;
        lps[0] = 0;
        while (i < M) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
    }
}
