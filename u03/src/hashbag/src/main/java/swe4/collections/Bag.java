package swe4.collections;

public interface Bag<T> extends Iterable<T>{

    int add(T element);

    int add(T element, int occurrences);

    void clear();

    int count(T element);

    float getLoadFactor();

    Object[] elements();

    boolean isEmpty();

    int remove(T element);

    int remove(T element, int occurrences);

    void rehash();

    int size();
}
