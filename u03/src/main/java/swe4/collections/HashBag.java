package swe4.collections;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class HashBag<T> implements Bag<T> {

    private static final int DEFAULT_MAX_CAPACITY = 11;
    private static final float DEFAULT_MAX_LOAD_FACTOR = 0.75f;
    public static final int REHASH_FACTOR = 2;

    private final float maxLoadFactor;
    private Object[] items;
    private int count;

    public HashBag() {
        this(DEFAULT_MAX_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }

    public HashBag(int initialCapacity, float maxLoadFactor) {
        if(initialCapacity < 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        if(maxLoadFactor > 1 || maxLoadFactor <= 0) {
            throw new IllegalArgumentException("Loadfactor must be between exclusive 0 and inclusive 1");
        }
        this.maxLoadFactor = maxLoadFactor;
        this.items = new Object[initialCapacity];
        this.count = 0;
    }

    private class HashBagIterator implements Iterator<T> {
        int idx = 0;
        Iterator<T> currentNodeItr = null;

        public HashBagIterator() {
            final Node<T> node = HashBag.this.elementAt(idx);
            if (node != null) {
                currentNodeItr = node.iterator();
            }
        }

        @Override
        public boolean hasNext() {
            return (currentNodeItr != null
                    && currentNodeItr.hasNext())
                    || findNextNotNull() >= 0;
        }

        @Override
        public T next() {
            if (currentNodeItr != null && currentNodeItr.hasNext()) {
                return currentNodeItr.next();
            }
            idx = findNextNotNull();
            if (idx < 0) {
                return null;
            }
            currentNodeItr = HashBag.this.elementAt(idx).iterator();
            return currentNodeItr.next();
        }

        private int findNextNotNull() {
            for (int i = idx + 1; i < HashBag.this.items.length; i++) {
                if (HashBag.this.items[i] != null) {
                    return i;
                }
            }
            return -1;
        }

    }

    @Override
    public Iterator<T> iterator() {
        return new HashBagIterator();
    }

    /**
     * Add an element
     *
     * @param element should be added to the HashBag
     * @return how often the element already was in the HashBag
     */
    public int add(T element) {
        return add(element, 1);
    }

    /**
     * Add an element n-times to the HashBag
     *
     * @param element     should be added to the HashBag
     * @param occurrences how often the element should be inserted
     * @return how often the element occurred in the HashBag before the add operation
     */
    public int add(T element, int occurrences) {
        if (occurrences <= 0) {
            throw new IllegalArgumentException("Occurrences must be greater than zero");
        }

        int index = hashToIndex(element);
        Node<T> node = elementAt(index);
        int prevOccurrences = 0;

        if (node == null) {
            count++;
            if ((float) count / this.items.length > this.maxLoadFactor) {
                rehash();
                index = hashToIndex(element);
            }
            node = new Node<>(element);
            this.items[index] = node;
            occurrences--;
        } else {
            prevOccurrences = node.count(element);
        }

        for (int i = 0; i < occurrences; i++) {
            node.add(element);
        }

        return prevOccurrences;
    }

    /**
     * Clear HashBag
     */
    public void clear() {
        Arrays.fill(this.items, null);
        this.count = 0;
    }

    /**
     * Checks if an element is in the HashBag
     *
     * @param element the element which is looked for
     * @return if the HashBag contains the element
     */
    public boolean contains(T element) {
        final Node<T> node = this.elementAt(this.hashToIndex(element));
        return node != null && node.contains(element);
    }

    /**
     * Count how often the element occurs in the HashBag
     *
     * @return the count of the element
     */
    public int count(T element) {
        Node<T> node = elementAt(this.hashToIndex(element));
        if (node != null) {
            return node.count(element);
        }
        return 0;
    }

    public float getLoadFactor() {
        return (float) this.count / this.items.length;
    }

    public Object[] elements() {
        int allCount = 0;
        for (T ignored :
                this) {
            allCount++;
        }
        Object[] objects = new Object[allCount];
        for (Object obj :
                this) {
            objects[--allCount] = obj;
        }
        return objects;
    }

    public boolean isEmpty() {
        return this.count == 0;
    }

    public int remove(T element) {
        return this.remove(element, 1);
    }

    public int remove(T element, int occurrences) {
        if(occurrences <= 0) {
            throw new IllegalArgumentException("Occurences must be greater than zero");
        }
        int idx = hashToIndex(element);
        Node<T> node = elementAt(idx);
        if (node == null) {
            return 0;
        }
        int length = node.count(element);
        if (node.next() == null && Objects.equals(node.value(), element)) {
            this.items[idx] = null;
            return 1;
        }

        Node<T> newList = null;
        int i = 0;
        for (Node<T> next = this.elementAt(idx); next != null; next = next.next()) {
            if (Objects.equals(next.value(), element)  && i < occurrences) {
                i++;
            } else {
                if (newList == null) {
                    newList = new Node<>(next.value());
                } else {
                    newList.add(next.value());
                }
            }
        }
        this.items[idx] = newList;
        return length;
    }

    public void rehash() {
        Object[] oldItems = this.items;
        this.items = new Object[this.items.length * REHASH_FACTOR];
        int oldCount = this.count;
        for (Object item :
                oldItems) {
            if (item != null) {
                Node<T> list = (Node<T>) item;
                for (T node :
                        list) {
                    this.add(node);
                }
            }
        }
        this.count = oldCount;
    }

    public int size() {
        int i = 0;
        for (T ignored :
                this) {
            i++;
        }
        return i;
    }

    /**
     * Calculate the index in the HashBag
     *
     * @param element the object of which the index should be calculated
     * @return index in HashBag
     */
    private int hashToIndex(T element) {
        return element == null ? 0 : Math.abs(element.hashCode()) % this.items.length;
    }

    private Node<T> elementAt(int i) {
        if (i >= this.items.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return (Node<T>) this.items[i];
    }
}
