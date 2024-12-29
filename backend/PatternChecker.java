

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class PatternChecker {
    static int ERROR_FLAG = 0;
    static int ALGO_FLAG = 1;
    static int DATA_FLAG = 2;
    static int TIME_FLAG = 3;
    static int PATTERN_FOUND = 4;
    static int PATTERN_NOTFOUND = 5;
    public static void main(String[] args) throws Exception {
        try{

            if (args.length != 2) {
                System.out.println("Usage: java PatternChecker <file-path> <pattern>");
            System.exit(1);
        }
        
        String filePath = args[0];
        String pattern = args[1];
        
        // Read file content
        String content = new String(Files.readAllBytes(Paths.get(filePath)));

        // Time
        long startTime, endTime;
        
        // Run search algorithms
        System.out.println(ALGO_FLAG+": KMP");
        startTime = System.nanoTime();
        KMP.KMPSearch(pattern, content);
        endTime = System.nanoTime();
        System.out.println(TIME_FLAG+": "+ TimeUnit.NANOSECONDS.toMillis(endTime - startTime));

        System.out.println(ALGO_FLAG+": Boyer-Moore");
        startTime = System.nanoTime();
        BoyerMoore.BoyerMooreSearch(content, pattern);
        endTime = System.nanoTime();
        System.out.println(TIME_FLAG+": "+ TimeUnit.NANOSECONDS.toMillis(endTime - startTime));

        System.out.println(ALGO_FLAG+": Rabin-Karp");
        startTime = System.nanoTime();
        RabinKarp.RabinKarpSearch(pattern, content, 101);
        endTime = System.nanoTime();
        System.out.println(TIME_FLAG+": "+ TimeUnit.NANOSECONDS.toMillis(endTime - startTime));

        System.out.println(ALGO_FLAG+": AhoCorasick");
        startTime = System.nanoTime();
        AhoCorasick.searchWords(new String[]{pattern}, 1, content);
        endTime = System.nanoTime();
        System.out.println(TIME_FLAG+": "+ TimeUnit.NANOSECONDS.toMillis(endTime - startTime));

        System.out.println(ALGO_FLAG+": Levenshtein Distance");
        startTime = System.nanoTime();
        // For every substring run levenstein distance
        int contentLen = content.length();
        int patternLen = pattern.length();

        boolean flag = false;
        for (int i = 0; i <= contentLen - patternLen; i++) {
            // Extract a substring of the same length as the pattern
            String substring = content.substring(i, i + patternLen);

            // Calculate Levenshtein distance between the substring and the pattern
            int distance = Levenshtein.levenshteinDistance(substring, pattern);

            // Check if the distance is within the allowed maximum distance(0)
            if (distance == 0) {
                // System.out.println("Pattern found at index " + i + " with distance " + distance);
                flag = true; // Pattern found
            }    
        }

        if(!flag){
            System.out.println(DATA_FLAG+": "+ PATTERN_NOTFOUND);
        }else{
            System.out.println(DATA_FLAG+": "+PATTERN_FOUND);
        }

        endTime = System.nanoTime();
        System.out.println(TIME_FLAG+": "+ TimeUnit.NANOSECONDS.toMillis(endTime - startTime));

    }catch(Exception e){
        System.out.println(ERROR_FLAG+": An error occured. Please check the input and try again");
    }
    }   
}

