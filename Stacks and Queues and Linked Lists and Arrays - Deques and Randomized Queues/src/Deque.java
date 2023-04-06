import java.util.Iterator;

// Dequeue.
// A double-ended queue or deque (pronounced “deck”)
// is a generalization of a stack and a queue that
// supports adding and removing items from
// either the front or the back of the data structure.

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int size;

    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }

    // construct an empty deque
    public Deque() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        checkNotNull(item);
        size++;
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        if (oldFirst != null) {
            oldFirst.previous = first;
        } else {
            last = first;
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        checkNotNull(item);
        size++;
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.previous = oldLast;
        if (oldLast != null) {
            oldLast.next = last;
        } else {
            first = last;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        checkNotEmpty();
        size--;
        Item removedItem = first.item;
        first = first.next;
        if (first != null) {
            first.previous = null;
        } else {
            last = null;
        }
        return removedItem;
    }

    // remove and return the item from the back
    public Item removeLast() {
        checkNotEmpty();
        size--;
        Item removedItem = last.item;
        last = last.previous;
        if (last != null) {
            last.next = null;
        } else {
            first = null;
        }
        return removedItem;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {

        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (hasNext()) {
                Item item = current.item;
                current = current.next;
                return item;
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
            throw new java.util.NoSuchElementException("Deque is empty");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {

        // Test 1: Construct Deque
        System.out.println("Test 1: Construct Deque");
        Deque<Integer> deque = new Deque<>();
        System.out.println("Is empty? " + deque.isEmpty()); // Expected: true, as deque is initially empty
        System.out.println("Size: " + deque.size()); // Expected: 0, as deque is initially empty

        // Test 2: addFirst and check
        System.out.println("\nTest 2: addFirst and check");
        Deque<Integer> deque2 = new Deque<>();
        deque2.addFirst(10);
        System.out.println("Is empty? " + deque2.isEmpty()); // Expected: false, as we've added one element
        System.out.println("Size: " + deque2.size()); // Expected: 1, as we've added one element
        System.out.println("Iterating over deque: ");
        for (int item : deque2) {
            System.out.println(item); // Expected: 10, as we've added 10 as the first element
        }

        // Test 3: addLast and check
        System.out.println("\nTest 3: addLast and check");
        Deque<Integer> deque3 = new Deque<>();
        deque3.addFirst(10);
        deque3.addLast(20);
        System.out.println("Is empty? " + deque3.isEmpty()); // Expected: false, as we've added two elements
        System.out.println("Size: " + deque3.size()); // Expected: 2, as we've added two elements
        System.out.println("Iterating over deque: ");
        for (int item : deque3) {
            System.out.println(item); // Expected: 10 and then 20, as these are the elements we've added
        }

        // Test 4: removeFirst and check
        System.out.println("\nTest 4: removeFirst and check");
        Deque<Integer> deque4 = new Deque<>();
        deque4.addFirst(10);
        deque4.addLast(20);
        System.out.println("Removed item: " + deque4.removeFirst()); // Expected: 10, as 10 is the first element
        System.out.println("Size: " + deque4.size()); // Expected: 1, as we've removed one element
        System.out.println("Iterating over deque: ");
        for (int item : deque4) {
            System.out.println(item); // Expected: 20, as 20 is the remaining element after removal
        }

        // Test 5: removeLast and check
        System.out.println("\nTest 5: removeLast and check");
        Deque<Integer> deque5 = new Deque<>();
        deque5.addFirst(10);
        deque5.addLast(20);
        System.out.println("Removed item: " + deque5.removeLast()); // Expected: 20, as 20 is the last element
        System.out.println("Removed item: " + deque5.removeFirst()); // Expected: 10, as 10 is the remaining element
        System.out.println("Is empty? " + deque5.isEmpty()); // Expected: true, as we've removed all elements
        System.out.println("Size: " + deque5.size()); // Expected: 0, as we've removed all elements

        System.out.println("\nAll tests completed!");
    }

}
