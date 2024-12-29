import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class AhoCorasick {
    static final int MAXS = 500;
    static final int MAXC = 26;

    static int[] out = new int[MAXS];
    static int[] f = new int[MAXS];
    static int[][] g = new int[MAXS][MAXC];

    static int ERROR_FLAG = 0;
    static int ALGO_FLAG = 1;
    static int DATA_FLAG = 2;
    static int TIME_FLAG = 3;
    static int PATTERN_FOUND = 4;
    static int PATTERN_NOTFOUND = 5;
    static int buildMatchingMachine(String[] arr, int k) {
        Arrays.fill(out, 0);
        for (int i = 0; i < MAXS; i++) {
            Arrays.fill(g[i], -1);
        }
        int states = 1;

        for (int i = 0; i < k; i++) {
            String word = arr[i];
            int currentState = 0;

            for (int j = 0; j < word.length(); j++) {
                int ch = word.charAt(j) - 'a';
                if (ch < 0 || ch >= MAXC) continue; // Ignore non-lowercase alphabetic characters
                if (g[currentState][ch] == -1) {
                    g[currentState][ch] = states++;
                }
                currentState = g[currentState][ch];
            }
            out[currentState] |= (1 << i);
        }

        for (int ch = 0; ch < MAXC; ch++) {
            if (g[0][ch] == -1) {
                g[0][ch] = 0;
            }
        }

        Arrays.fill(f, -1);

        Queue<Integer> q = new LinkedList<>();
        for (int ch = 0; ch < MAXC; ch++) {
            if (g[0][ch] != 0) {
                f[g[0][ch]] = 0;
                q.add(g[0][ch]);
            }
        }

        while (!q.isEmpty()) {
            int state = q.poll();
            for (int ch = 0; ch < MAXC; ch++) {
                if (g[state][ch] != -1) {
                    int failure = f[state];
                    while (g[failure][ch] == -1) {
                        failure = f[failure];
                    }
                    failure = g[failure][ch];
                    f[g[state][ch]] = failure;
                    out[g[state][ch]] |= out[failure];
                    q.add(g[state][ch]);
                }
            }
        }
        return states;
    }

    static void searchWords(String[] arr, int k, String text) {
        try{

            buildMatchingMachine(arr, k);
            int currentState = 0;
    
            boolean flag = false;
    
            for (int i = 0; i < text.length(); i++) {
                char currentChar = text.charAt(i);
                if (currentChar >= 'a' && currentChar <= 'z') {
                    int ch = currentChar - 'a';
                    while (g[currentState][ch] == -1) {
                        currentState = f[currentState];
                    }
                    currentState = g[currentState][ch];
                    if (out[currentState] == 0) {
                        continue;
                    }
                    for (int j = 0; j < k; j++) {
                        if ((out[currentState] & (1 << j)) > 0) {
                            flag = true;
                            // System.out.println("Aho-Corasick: Word " + arr[j] + " appears from " + (i - arr[j].length() + 1) + " to " + i);
                        }
                    }
                }
            }
    
            if(!flag){
            System.out.println(DATA_FLAG+": "+PATTERN_NOTFOUND);
        }else{
            System.out.println(DATA_FLAG+": "+PATTERN_FOUND);
        }
        }catch(Exception e){
            System.out.println(ERROR_FLAG+": An error occured at AhoCorasick algo");
        }   
    }
}
