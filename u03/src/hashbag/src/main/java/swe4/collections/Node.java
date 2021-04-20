package swe4.collections;

import java.util.Iterator;
import java.util.Objects;

public class Node<T> implements Iterable<T> {
    private final T value;
    private Node<T> next;

    public Node(T value) {
        this(value, null);
    }

    public Node(T value, Node<T> next) {
        this.value = value;
        this.next = next;
    }

    public T value() {
        return this.value;
    }

    public Node<T> next() {
        return this.next;
    }

    public int add(T value) {
        if (this.next == null) {
            this.next = new Node<>(value);
            return 1;
        }
        return this.next.add(value) + 1;
    }

    public boolean contains(T element) {
        if (Objects.equals(this.value, element)) {
            return true;
        }
        if (this.next == null) {
            return false;
        }
        return this.next.contains(element);
    }

    public int count(T element) {
        int count = 0;
        if (Objects.equals(this.value, element)) {
            count++;
        }
        if (this.next == null) {
            return count;
        }
        return count + this.next.count(element);
    }

    @Override
    public Iterator<T> iterator() {
        return new NodeIterator();
    }

    private Node<T> node(int idx) {
        Node<T> node = this;
        for (int i = 0; i < idx && node != null; i++, node = node.next) ;
        return node;
    }


    private class NodeIterator implements Iterator<T> {
        int idx = 0;

        @Override
        public boolean hasNext() {
            return Node.this.node(idx) != null;
        }

        @Override
        public T next() {
            return Node.this.node(idx++).value;
        }
    }
}
