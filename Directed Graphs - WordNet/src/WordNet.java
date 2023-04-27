import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

// The WordNet digraph.
// each vertex v is an integer that represents a synset,
// and each directed edge v → w represents that w is a hypernym of v.
// The WordNet digraph is a rooted DAG:
// it is acyclic and has one vertex—the root—that is an ancestor of every other vertex.

public class WordNet {

    private final List<String> synsetData;
    private final Map<String, List<Integer>> nounMap;
    private final SAP sap;

    public WordNet(String synsets, String hypernyms) {
        checkNotNull(synsets, hypernyms);

        this.synsetData = new ArrayList<>();
        this.nounMap = new HashMap<>();

        // parse synsets and hypernyms files to create the WordNet digraph
        parseSynsets(synsets);
        Digraph wordNetDigraph = parseHypernyms(hypernyms);

        // check if it's a rooted DAG
        DirectedCycle wordNetCycleChecker = new DirectedCycle(wordNetDigraph);
        if (wordNetCycleChecker.hasCycle()) {
            throw new IllegalArgumentException("Input does not represent a DAG as it contains cycles");
        }

        int roots = 0;

        for (int v = 0; v < wordNetDigraph.V(); v++) {
            if (!wordNetDigraph.adj(v).iterator().hasNext()) {
                roots++;

                if (roots > 1) {
                    throw new IllegalArgumentException("Input does not represent a rooted DAG as it has multiple roots");
                }
            }
        }

        if (roots == 0) {
            throw new IllegalArgumentException("Input does not represent a rooted DAG as it has no root");
        }

        // construct a new SAP object that can be used to answer queries about
        // the shortest ancestral path between different nouns in the WordNet
        this.sap = new SAP(wordNetDigraph);
    }

    private void parseSynsets(String synsets) {
        In synsetsInput = new In(synsets);

        while (synsetsInput.hasNextLine()) {
            String[] line = synsetsInput.readLine().split(",");
            int synsetID = Integer.parseInt(line[0]);
            String synset = line[1];
            this.synsetData.add(synsetID, synset);

            String[] nounsInSynset = line[1].split(" ");
            for (String nounInSynSet : nounsInSynset) {
                nounMap.computeIfAbsent(nounInSynSet, k -> new ArrayList<>()).add(synsetID);
            }
        }
    }

    private Digraph parseHypernyms(String hypernyms) {
        Digraph wordNetDigraph = new Digraph(synsetData.size());
        In hypernymsInput = new In(hypernyms);

        while (hypernymsInput.hasNextLine()) {
            String[] line = hypernymsInput.readLine().split(",");
            int synsetID = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                int hypernymID = Integer.parseInt(line[i]);
                wordNetDigraph.addEdge(synsetID, hypernymID);
            }
        }

        return wordNetDigraph;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        checkNotNull(word);
        return nounMap.containsKey(word);
    }

    // length of shortest ancestral path between nounA and nounB
    public int distance(String nounA, String nounB) {
        checkNotNull(nounA, nounB);
        validateNounsInWordNet(nounA, nounB);

        List<Integer> idsOfNounA = nounMap.get(nounA);
        List<Integer> idsOfNounB = nounMap.get(nounB);
        return sap.length(idsOfNounA, idsOfNounB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path
    public String sap(String nounA, String nounB) {
        checkNotNull(nounA, nounB);
        validateNounsInWordNet(nounA, nounB);

        List<Integer> idsOfNounA = nounMap.get(nounA);
        List<Integer> idsOfNounB = nounMap.get(nounB);
        String shortestCommonAncestor = synsetData.get(sap.ancestor(idsOfNounA, idsOfNounB));
        return shortestCommonAncestor;
    }

    private void checkNotNull(String... args) {
        for (String arg : args) {
            if (arg == null) {
                throw new IllegalArgumentException("Arguments cannot be null");
            }
        }
    }

    private void validateNounsInWordNet(String... args) {
        for (String arg : args) {
            if (!nounMap.containsKey(arg)) {
                throw new IllegalArgumentException("Arguments must be WordNet nouns");
            }
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // Test 1: Construct WordNet
//        System.out.println("\n[Test 1] Construct WordNet");
//        WordNet wordnet1 = new WordNet("synsets.txt", "hypernyms.txt");
//        System.out.println("WordNet constructed successfully.");

        // Test 2: Nouns in WordNet
        System.out.println("\n[Test 2] Nouns in WordNet");
        WordNet wordnet2 = new WordNet("synsets.txt", "hypernyms.txt");
        for (String noun : wordnet2.nouns()) {
            System.out.println(noun); // Expected: print nouns in wordnet
        }

        // Test 3: Testing isNoun method
        System.out.println("\n[Test 3] Testing isNoun method:");
        WordNet wordnet3 = new WordNet("synsets.txt", "hypernyms.txt");
        String[] testWords = {"acquired_immune_deficiency_syndrome", "word2", "word3"};
        for (String word : testWords) {
            System.out.println(word + " is a WordNet noun: " + wordnet3.isNoun(word)); // Expected: true for "acquired_immune_deficiency_syndrome", false for "word2" and "word3"
        }

        // Test 4: Testing distance method
        System.out.println("\n[Test 4] Testing distance method:");
        WordNet wordnet4 = new WordNet("synsets.txt", "hypernyms.txt");
        String nounA = "ginseng";
        String nounB = "Down_syndrome";
        System.out.println("Distance between " + nounA + " and " + nounB + ": " + wordnet4.distance(nounA, nounB)); // Expected: 15

        // Test 5: Testing sap method
        System.out.println("\n[Test 5] Testing sap method:");
        WordNet wordnet5 = new WordNet("synsets.txt", "hypernyms.txt");
        System.out.println("Common Ancestor in the SAP between " + nounA + " and " + nounB + ": " + wordnet5.sap(nounA, nounB)); // Expected: entity

        System.out.println("\nAll tests completed!");
    }
}
