package swe4.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class HashBagTest {

    @Test
    void newHashBagIsEmpty() {
        //given
        //when
        HashBag<String> bag = new HashBag<>();
        //then
        assertTrue(bag.isEmpty());
    }

    @Test
    void newHashBagContainsNoElement() {
        //given
        String test = "test";
        //when
        HashBag<String> bag = new HashBag<>();
        //then
        assertFalse(bag.contains(test));
    }

    @Test
    void newHashBagCountsZeroElements() {
        //given
        String test = "test";
        //when
        HashBag<String> bag = new HashBag<>();
        //then
        assertEquals(0, bag.count(test));
    }

    @Test
    void newHashBagHasZeroLoadFactor() {
        //given
        //when
        HashBag<String> bag = new HashBag<>();
        //then
        assertEquals(0.0f, bag.getLoadFactor());
    }

    @Test
    void newHashBagCapacitySmallerZero_expectException() {
        //given
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> new HashBag<>(-1, 0.5f));
    }

    @Test
    void newHashBagLoadFactorEqualZero_expectException() {
        //given
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> new HashBag<>(10, 0));
    }

    @Test
    void newHashBagLoadFactorGreaterOne_expectException() {
        //given
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> new HashBag<>(10, 100));
    }

    @Test
    void hashBagContainsElement() {
        //given
        String test = "test";
        HashBag<String> bag = new HashBag<>();
        //when
        bag.add(test);
        //then
        assertTrue(bag.contains(test));
    }

    @Test
    void hashBagContainsExactlyOneElement() {
        //given
        String test = "test";
        HashBag<String> bag = new HashBag<>();
        //when
        bag.add(test);
        //then
        assertEquals(1, bag.count(test));
    }

    @Test
    void hashBagContainsExactlyTenElement() {
        //given
        String test = "test";
        HashBag<String> bag = new HashBag<>();
        //when
        bag.add(test, 10);
        //then
        assertEquals(10, bag.count(test));
    }

    @Test
    void hashBagWithCollisionCheckIfNonExistingElementIsNotPresent() {
        //given
        String test = "test";
        String test1 = "asdfjklö";
        HashBag<String> bag = new HashBag<>(1, 1.0f);
        //when
        bag.add(test);
        //then
        assertTrue(bag.contains(test));
        assertFalse(bag.contains(test1));
    }

    /**
     * The problem with this implementation, is that the HashBag will never rehash
     * if the loadfactor is set to 100%.
     * This isn't bug, but behaviour which can be unexpected
     */
    @Test
    void hashBagWithCollisionCheckIfExistingElementIsPresent() {
        //given
        String test = "test";
        String test1 = "asdfjklö";
        HashBag<String> bag = new HashBag<>(1, 1.0f);
        //when
        bag.add(test);
        bag.add(test1);
        //then
        assertTrue(bag.contains(test));
        assertTrue(bag.contains(test1));
    }

    @Test
    void hashBagDoesNotContainElementWhenRemoved() {
        //given
        String test = "test";
        HashBag<String> bag = new HashBag<>();
        //when
        bag.add(test);
        bag.remove(test);
        //then
        assertFalse(bag.contains(test));
    }

    @Test
    void hashBagContainsOnlyNumberOfElementsWhichWereAddedMinusTheRemovedOnes() {
        //given
        String test = "test";
        HashBag<String> bag = new HashBag<>();
        //when
        bag.add(test, 10);
        bag.remove(test, 5);
        //then
        assertEquals(5, bag.count(test));
    }

    @Test
    void hashBagContainsNoElementWhenAllRemove() {
        //given
        String test = "test";
        HashBag<String> bag = new HashBag<>();
        //when
        bag.add(test, 10);
        bag.remove(test, 10);
        //then
        assertEquals(0, bag.count(test));
    }

    @Test
    void addZeroElements_expectException() {
        //given
        String test = "test";
        HashBag<String> bag = new HashBag<>();
        //when
        //then
        assertThrows(IllegalArgumentException.class,  () -> bag.add(test, 0));
    }

    @Test
    void removeNotExistingElement() {
        //given
        String test = "test";
        HashBag<String> bag = new HashBag<>();
        //when
        //then
        assertEquals(0, bag.remove(test));
    }

    @Test
    void removeZeroElements_expectException() {
        //given
        String test = "test";
        HashBag<String> bag = new HashBag<>();
        //when
        //then
        assertThrows(IllegalArgumentException.class,  () -> bag.remove(test, 0));
    }

    @Test
    void loadFactorIsCorrectIfHalfIsFilled() {
        //given
        int i = 1;
        SetHashObject obj1 = new SetHashObject(i++);
        SetHashObject obj2 = new SetHashObject(i++);
        SetHashObject obj3 = new SetHashObject(i++);
        SetHashObject obj4 = new SetHashObject(i++);
        SetHashObject obj5 = new SetHashObject(i);
        HashBag<SetHashObject> bag = new HashBag<>(10, 0.75f);

        //when
        bag.add(obj1);
        bag.add(obj2);
        bag.add(obj3);
        bag.add(obj4);
        bag.add(obj5);

        //then
        assertEquals(0.5f, bag.getLoadFactor());
    }

    @Test
    void iteratorReturnsNull_whenHashBagEmpty() {
        //given
        HashBag<String> bag = new HashBag<>();

        //when
        Iterator<String> it = bag.iterator();

        //then
        assertNull(it.next());
    }

    @Test
    void iteratorReturnsFirstElement() {
        //given
        String test = "test";
        HashBag<String> bag = new HashBag<>();
        bag.add(test);

        //when
        Iterator<String> it = bag.iterator();

        //then
        assertEquals(test, it.next());
    }

    @Test
    void iteratorReturnsElements_whenHashBagHasGaps() {
        //Given
        HashBag<SetHashObject> bag = new HashBag<>(10, 0.75f);
        SetHashObject obj1 = new SetHashObject(1);
        SetHashObject obj2 = new SetHashObject(3);
        bag.add(obj1);
        bag.add(obj2);

        //when
        Iterator<SetHashObject> it = bag.iterator();
        SetHashObject test1 = it.next();
        SetHashObject test2 = it.next();

        //then
        assertEquals(obj1, test1);
        assertEquals(obj2, test2);
    }

    @Test
    void iteratorHasNotNext_whenHashBagEmpty() {
        //Given
        HashBag<SetHashObject> bag = new HashBag<>();

        //when
        Iterator<SetHashObject> it = bag.iterator();

        //then
        assertFalse(it.hasNext());
        assertTrue(bag.isEmpty());
    }

    @Test
    void iteratorReturnsMultipleEntries() {
        //Given
        HashBag<SetHashObject> bag = new HashBag<>();
        SetHashObject obj1 = new SetHashObject(1);
        bag.add(obj1, 3);

        //when
        Iterator<SetHashObject> it = bag.iterator();

        //then
        assertTrue(it.hasNext());
        assertEquals(obj1, it.next());
        assertTrue(it.hasNext());
        assertEquals(obj1, it.next());
        assertTrue(it.hasNext());
        assertEquals(obj1, it.next());
        assertFalse(it.hasNext());
    }

}
