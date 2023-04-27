import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

// Outcast detection.
// Given a list of WordNet nouns x1, x2, ..., xn,
// which noun is the least related to the others?

public class Outcast {

    private final WordNet wordnet;

    public Outcast(WordNet wordnet) {         // constructor takes a WordNet object
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {   // given an array of WordNet nouns, return an outcast
        int maxSumOfDists = 0;
        String outcast = "";

        // To identify an outcast, compute the sum of the distances between each noun and every other one
        // and return a noun for which the sum of the distances is maximum.
        for (int i = 0; i < nouns.length; i++) {

            int sumOfDistsBetweenIAndOthers = 0;

            for (int j = 0; j < nouns.length; j++) {

                sumOfDistsBetweenIAndOthers += wordnet.distance(nouns[i], nouns[j]);

                if (j == nouns.length - 1 && sumOfDistsBetweenIAndOthers > maxSumOfDists) {
                    maxSumOfDists = sumOfDistsBetweenIAndOthers;
                    outcast = nouns[i];
                    break;
                }
            }
        }

        return outcast;
    }

    public static void main(String[] args) {        // test client
        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);
        for (int t = 0; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
