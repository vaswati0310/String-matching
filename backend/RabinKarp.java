public class RabinKarp {
  public final static int d = 256;
    static int ERROR_FLAG = 0;
    static int ALGO_FLAG = 1;
    static int DATA_FLAG = 2;
    static int TIME_FLAG = 3;
    static int PATTERN_FOUND = 4;
    static int PATTERN_NOTFOUND = 5;
  public static void RabinKarpSearch(String pattern, String text, int q) {
      try{

          int m = pattern.length();
          int n = text.length();
          int i, j;
          int p = 0;
          int t = 0;
          int h = 1;
    
          for (i = 0; i < m - 1; i++)
              h = (h * d) % q;
    
          for (i = 0; i < m; i++) {
              p = (d * p + pattern.charAt(i)) % q;
              t = (d * t + text.charAt(i)) % q;
          }
    
          boolean flag = false;
    
          for (i = 0; i <= n - m; i++) {
              if (p == t) {
                  for (j = 0; j < m; j++) {
                      if (text.charAt(i + j) != pattern.charAt(j))
                          break;
                  }
                  if (j == m){
                    flag = true;
                    // System.out.println("Rabin-Karp: Pattern found at index " + i);
                  }
              }
    
              if (i < n - m) {
                  t = (d * (t - text.charAt(i) * h) + text.charAt(i + m)) % q;
                  if (t < 0)
                      t = (t + q);
              }
          }
    
        if(!flag){
            System.out.println(DATA_FLAG+": "+PATTERN_NOTFOUND);
        }else{
            System.out.println(DATA_FLAG+": "+PATTERN_FOUND);
        }
      }catch(Exception e){
        System.out.println(ERROR_FLAG+": An error occured at RabinKarp algo");
    }
  }
}
