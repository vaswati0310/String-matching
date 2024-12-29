import java.util.*;
class KMP {
    public static void KMPSearch(String pattern, String text) {
        int M = pattern.length();
        int N = text.length();
        int[] lps = new int[M];
        int j = 0; // index for pattern
        computeLPSArray(pattern, M, lps);
        int i = 0; // index for text
        while (i < N) {
            if (pattern.charAt(j) == text.charAt(i)) {
                j++;
                i++;
            }
            if (j == M) {
                System.out.println("KMP: Found pattern at index " + (i - j));
                j = lps[j - 1];
            } else if (i < N && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0)
                    j = lps[j - 1];
                else
                    i++;
            }
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

class BoyerMoore {
    public static void BoyerMooreSearch(String text, String pattern) {
        int m = pattern.length();
        int n = text.length();
        HashMap<Character, Integer> badChar = new HashMap<>();
        for (int i = 0; i < m; i++) {
            badChar.put(pattern.charAt(i), i);
        }
        int s = 0;
        while (s <= (n - m)) {
            int j = m - 1;
            while (j >= 0 && pattern.charAt(j) == text.charAt(s + j)) {
                j--;
            }
            if (j < 0) {
                System.out.println("Boyer-Moore: Pattern occurs at index " + s);
                s += (s + m < n) ? m - badChar.getOrDefault(text.charAt(s + m), -1) : 1;
            } else {
                s += Math.max(1, j - badChar.getOrDefault(text.charAt(s + j), -1));
            }
        }
    }
}

class RabinKarp {
    public final static int d = 256;
    public static void RabinKarpSearch(String pattern, String text, int q) {
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

        for (i = 0; i <= n - m; i++) {
            if (p == t) {
                for (j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j))
                        break;
                }
                if (j == m)
                    System.out.println("Rabin-Karp: Pattern found at index " + i);
            }

            if (i < n - m) {
                t = (d * (t - text.charAt(i) * h) + text.charAt(i + m)) % q;
                if (t < 0)
                    t = (t + q);
            }
        }
    }
}

class AhoCorasick {
    static final int MAXS = 500;
    static final int MAXC = 26;

    static int[] out = new int[MAXS];
    static int[] f = new int[MAXS];
    static int[][] g = new int[MAXS][MAXC];

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
        buildMatchingMachine(arr, k);
        int currentState = 0;
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
                        System.out.println("Aho-Corasick: Word " + arr[j] + " appears from " + (i - arr[j].length() + 1) + " to " + i);
                    }
                }
            }
        }
    }
}

class Levenshtein {
    public static int levenshteinDistance(String str1, String str2) {
        int len1 = str1.length();
        int len2 = str2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            for (int j = 0; j <= len2; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j], Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
                }
            }
        }
        return dp[len1][len2];
    }
}
public class PlagiarismDetectionAlgorithms {
    public static void main(String[] args) {
        String text = "this is a simple example of a text to test pattern matching algorithms";
        String pattern = "pattern";
        String[] patternsAhoCorasick = {"pattern", "text", "simple"};

        long startTime, endTime, timeElapsed;

        // KMP
        System.out.println("### Test Case 1: Small Text ('pattern') ###");
        startTime = System.nanoTime();
        KMP.KMPSearch(pattern, text);
        endTime = System.nanoTime();
        timeElapsed = endTime - startTime;
        System.out.println("KMP Time: " + timeElapsed + "ns\n");

        // Boyer-Moore
        startTime = System.nanoTime();
        BoyerMoore.BoyerMooreSearch(text, pattern);
        endTime = System.nanoTime();
        timeElapsed = endTime - startTime;
        System.out.println("Boyer-Moore Time: " + timeElapsed + "ns\n");

        // Rabin-Karp
        startTime = System.nanoTime();
        RabinKarp.RabinKarpSearch(pattern, text, 101);
        endTime = System.nanoTime();
        timeElapsed = endTime - startTime;
        System.out.println("Rabin-Karp Time: " + timeElapsed + "ns\n");

        // Aho-Corasick
        startTime = System.nanoTime();
        AhoCorasick.searchWords(patternsAhoCorasick, patternsAhoCorasick.length, text);
        endTime = System.nanoTime();
        timeElapsed = endTime - startTime;
        System.out.println("Aho-Corasick Time: " + timeElapsed + "ns\n");

        System.out.println("### Test Case 2: Large Text ###");
        text = "this is a large example text used to test pattern matching algorithms on large inputs. "
                + "Pattern matching is a crucial component in many computer science applications. "
                + "This large input text is designed to challenge the performance and accuracy of the algorithms. "
                + "We aim to find the pattern 'large' within this extended input. "
                + "Let's evaluate how efficiently the algorithms handle this increased text size.";
        pattern = "large";
        startTime = System.nanoTime();
        KMP.KMPSearch(pattern, text);
        endTime = System.nanoTime();
        timeElapsed = endTime - startTime;
        System.out.println("KMP Time: " + timeElapsed + "ns");

        startTime = System.nanoTime();
        BoyerMoore.BoyerMooreSearch(text, pattern);
        endTime = System.nanoTime();
        timeElapsed = endTime - startTime;
        System.out.println("Boyer-Moore Time: " + timeElapsed + "ns");

        startTime = System.nanoTime();
        RabinKarp.RabinKarpSearch(pattern, text, 101);
        endTime = System.nanoTime();
        timeElapsed = endTime - startTime;
        System.out.println("Rabin-Karp Time: " + timeElapsed + "ns");

        startTime = System.nanoTime();
        AhoCorasick.searchWords(new String[] {"large", "text", "inputs"}, 3, text);
        endTime = System.nanoTime();
        timeElapsed = endTime - startTime;
        System.out.println("Aho-Corasick Time: " + timeElapsed + "ns\n");

        System.out.println("### Test Case 3: No Match ###");
        text = "this is a text with no matching pattern";
        pattern = "nomatch";
        startTime = System.nanoTime();
        KMP.KMPSearch(pattern, text);
        endTime = System.nanoTime();
        timeElapsed = endTime - startTime;
        System.out.println("KMP Time: " + timeElapsed + "ns");

        startTime = System.nanoTime();
        BoyerMoore.BoyerMooreSearch(text, pattern);
        endTime = System.nanoTime();
        timeElapsed = endTime - startTime;
        System.out.println("Boyer-Moore Time: " + timeElapsed + "ns");

        startTime = System.nanoTime();
        RabinKarp.RabinKarpSearch(pattern, text, 101);
        endTime = System.nanoTime();
        timeElapsed = endTime - startTime;
        System.out.println("Rabin-Karp Time: " + timeElapsed + "ns");

        startTime = System.nanoTime();
        AhoCorasick.searchWords(new String[] {"nomatch"}, 1, text);
        endTime = System.nanoTime();
        timeElapsed = endTime - startTime;
        System.out.println("Aho-Corasick Time: " + timeElapsed + "ns\n");

        System.out.println("### Test Case 4: Levenshtein Distance ###");
        String str1 = "kitten";
        String str2 = "sitting";
        startTime = System.nanoTime();
        int distance = Levenshtein.levenshteinDistance(str1, str2);
        endTime = System.nanoTime();
        timeElapsed = endTime - startTime;
        System.out.println("Levenshtein Distance between '" + str1 + "' and '" + str2 + "': " + distance + " (Time: " + timeElapsed + "ns)");
    }
}