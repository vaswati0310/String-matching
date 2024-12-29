import java.util.HashMap;

public class BoyerMoore {
    static int ERROR_FLAG = 0;
    static int ALGO_FLAG = 1;
    static int DATA_FLAG = 2;
    static int TIME_FLAG = 3;
    static int PATTERN_FOUND = 4;
    static int PATTERN_NOTFOUND = 5;
  public static void BoyerMooreSearch(String text, String pattern) {
        try{

            int m = pattern.length();
            int n = text.length();
            HashMap<Character, Integer> badChar = new HashMap<>();
            for (int i = 0; i < m; i++) {
                badChar.put(pattern.charAt(i), i);
            }
            int s = 0;
    
            boolean flag = false;
    
            while (s <= (n - m)) {
                int j = m - 1;
                while (j >= 0 && pattern.charAt(j) == text.charAt(s + j)) {
                    j--;
                }
                if (j < 0) {
                  flag = true;
                    // System.out.println("Boyer-Moore: Pattern occurs at index " + s);
                    s += (s + m < n) ? m - badChar.getOrDefault(text.charAt(s + m), -1) : 1;
                } else {
                    s += Math.max(1, j - badChar.getOrDefault(text.charAt(s + j), -1));
                }
            }
    
            
            if(!flag){
                System.out.println(DATA_FLAG+": "+PATTERN_NOTFOUND);
            }else{
                System.out.println(DATA_FLAG+": "+PATTERN_FOUND);
            }
        }catch(Exception e){
            System.out.println(ERROR_FLAG+": An error occured at Boyer Moore algo");
        }
    }
}
