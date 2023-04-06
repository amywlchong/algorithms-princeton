import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

// Randomized queue.
// A randomized queue is similar to a stack or queue,
// except that the item removed is chosen
// uniformly at random among items in the data structure.

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int minSize = 2;
    private Item[] array;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.array = (Item[]) new Object[this.minSize];
        this.size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return this.size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return this.size;
    }

    private void resizeArray(int newSize) {
        Item[] newArray = (Item[]) new Object[newSize];
        for (int i = 0; i < this.size; i++) {
            newArray[i] = this.array[i];
        }
        this.array = newArray;
    }

    private void growArray() {
        resizeArray(this.array.length * 2);
    }

    private void shrinkArray() {
        resizeArray(this.array.length / 2);
    }

    // add the item
    public void enqueue(Item item) {
        checkNotNull(item);
        if (size == 0) {
            this.array[0] = item;
        } else {
            if (this.size == this.array.length) {
                growArray();
            }
            this.array[size] = item;
        }
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        checkNotEmpty();
        if (this.size < this.array.length / 4 && this.array.length > this.minSize) {
            shrinkArray();
        }
        int random = StdRandom.uniformInt(0, size);
        Item removedItem = this.array[random];
        this.array[random] = this.array[size - 1];
        this.array[size - 1] = null;
        size--;
        return removedItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        checkNotEmpty();
        int random = StdRandom.uniformInt(0, size);
        return this.array[random];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {

        private int current = 0;

        public ListIterator() {
            StdRandom.shuffle(array, 0, size);
        }

        public boolean hasNext() {
            return current < size;
        }

        public Item next() {
            if (hasNext()) {
                return array[current++];
            } else {
                throw new java.util.NoSuchElementException("No more items to return");
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private void checkNotNull(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
    }

    private void checkNotEmpty() {
        if (this.isEmpty()) {
            throw new java.util.NoSuchElementException("Randomized queue is empty");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {

        // Test 1: Construct RandomizedQueue
        System.out.println("Test 1: Construct RandomizedQueue");
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        System.out.println("Is empty? " + queue.isEmpty()); // Expected: true, as queue is initially empty
        System.out.println("Size: " + queue.size()); // Expected: 0, as queue is initially empty

        // Test 2: enqueue and check
        System.out.println("\nTest 2: enqueue and check");
        RandomizedQueue<Integer> queue2 = new RandomizedQueue<>();
        queue2.enqueue(10);
        System.out.println("Is empty? " + queue2.isEmpty()); // Expected: false, as we've added one element
        System.out.println("Size: " + queue2.size()); // Expected: 1, as we've added one element

        // Test 3: dequeue and check
        System.out.println("\nTest 3: dequeue and check");
        RandomizedQueue<Integer> queue3 = new RandomizedQueue<>();
        queue3.enqueue(10);
        queue3.enqueue(20);
        System.out.println("Removed item: " + queue3.dequeue()); // Expected: 10 or 20 (random)
        System.out.println("Size: " + queue3.size()); // Expected: 1, as we've removed one element

        // Test 4: sample and check
        System.out.println("\nTest 4: sample and check");
        RandomizedQueue<Integer> queue4 = new RandomizedQueue<>();
        queue4.enqueue(10);
        queue4.enqueue(20);
        System.out.println("Sampled item: " + queue4.sample()); // Expected: 10 or 20 (random)
        System.out.println("Size: " + queue4.size()); // Expected: 2, as sample does not remove elements

        // Test 5: Iterator and check
        System.out.println("\nTest 5: Iterator and check");
        RandomizedQueue<Integer> queue5 = new RandomizedQueue<>();
        queue5.enqueue(10);
        queue5.enqueue(20);
        System.out.println("Iterating over queue: ");
        for (int item : queue5) {
            System.out.println(item); // Expected: 10 and 20 in random order
        }

        System.out.println("\nAll tests completed!");
    }

}
