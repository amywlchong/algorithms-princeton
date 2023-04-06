import edu.princeton.cs.algs4.StdIn;

// Client.
// Write a client program Permutation.java that
// takes an integer k as a command-line argument;
// reads a sequence of strings from standard input
// using StdIn.readString(); and prints exactly k of them,
// uniformly at random.
// Print each item from the sequence at most once.

public class Permutation {
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Please provide an integer command-line argument");
        }

        int k;
        try {
            k = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Command line argument should be an integer", e);
        }

        RandomizedQueue<String> randomizedInput = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            String input = StdIn.readString();
            randomizedInput.enqueue(input);
        }

        for (int i = 0; i < k; i++) {
            if (randomizedInput.isEmpty()) {
                throw new IllegalArgumentException("Fewer strings in input than requested k");
            }
            System.out.println(randomizedInput.dequeue());
        }
    }
}
