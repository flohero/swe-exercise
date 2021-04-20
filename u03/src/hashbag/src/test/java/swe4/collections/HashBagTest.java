package swe4.collections;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class HashBagTest {

    /* Constructor Tests */

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

    /* Iterator Tests */

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

    /* Other Tests */

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
    void newHashBagHasZeroSize() {
        //given
        //when
        HashBag<String> bag = new HashBag<>();
        //then
        assertEquals(0, bag.size());
    }

    @Test
    void hashBagWithMultipleElementsHasCorrectSize() {
        //given
        HashBag<SetHashObject> bag = new HashBag<>(10, 0.75f);
        SetHashObject obj1 = new SetHashObject(1);
        SetHashObject obj2 = new SetHashObject(2);
        SetHashObject obj3 = new SetHashObject(3);
        //when
        bag.add(obj1);
        bag.add(obj2);
        bag.add(obj3);
        //then
        assertEquals(3, bag.size());
    }

    @Test
    void hashBagWithCollisionAndMultipleElementsHasCorrectSize() {
        //given
        HashBag<SetHashObject> bag = new HashBag<>(10, 0.75f);
        SetHashObject obj1 = new SetHashObject(1);
        SetHashObject obj1Copy1 = new SetHashObject(1);
        SetHashObject obj1Copy2 = new SetHashObject(1);
        SetHashObject obj2 = new SetHashObject(2);
        SetHashObject obj3 = new SetHashObject(3);
        //when
        bag.add(obj1);
        bag.add(obj1Copy1);
        bag.add(obj1Copy2);
        bag.add(obj2);
        bag.add(obj3);
        //then
        assertEquals(5, bag.size());
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
    void hashBagIsNotEmpty() {
        //given
        String test = "test";
        HashBag<String> bag = new HashBag<>();
        //when
        bag.add(test);
        //then
        assertFalse(bag.isEmpty());
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
    void hashBagContainsExactlyTenElements() {
        //given
        String test = "test";
        HashBag<String> bag = new HashBag<>();
        //when
        bag.add(test, 10);
        //then
        assertEquals(10, bag.count(test));
    }

    @Test
    void addZeroElements_expectException() {
        //given
        String test = "test";
        HashBag<String> bag = new HashBag<>();
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> bag.add(test, 0));
    }

    @Test
    void hashBagRehashesWhenLoadfactorReached() {
        //given
        HashBag<SetHashObject> bag = new HashBag<>(10, 0.8f);
        int i = 0;
        SetHashObject obj1 = new SetHashObject(i++);
        SetHashObject obj2 = new SetHashObject(i++);
        SetHashObject obj3 = new SetHashObject(i++);
        SetHashObject obj4 = new SetHashObject(i++);
        SetHashObject obj5 = new SetHashObject(i++);
        SetHashObject obj6 = new SetHashObject(i++);
        SetHashObject obj7 = new SetHashObject(i++);
        SetHashObject obj8 = new SetHashObject(i++);
        SetHashObject obj9 = new SetHashObject(i);

        //when
        bag.add(obj1);
        bag.add(obj2);
        bag.add(obj3);
        bag.add(obj4);
        bag.add(obj5);
        bag.add(obj6);
        bag.add(obj7);
        bag.add(obj8);
        float oldLoadfactor = bag.getLoadFactor();
        bag.add(obj9);
        //then
        assertTrue(oldLoadfactor > bag.getLoadFactor());
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
    void clearedHashBagIsCompletelyEmpty() {
        //given
        HashBag<SetHashObject> bag = new HashBag<>(10, 0.75f);
        SetHashObject obj1 = new SetHashObject(1);
        SetHashObject obj2 = new SetHashObject(2);
        bag.add(obj1, 3);
        bag.add(obj2, 2);

        //when
        bag.clear();

        //then
        assertTrue(bag.isEmpty());
        assertEquals(0, bag.getLoadFactor());
        assertEquals(0, bag.size());
        assertEquals(0, bag.count(obj1));
        assertEquals(0, bag.count(obj2));
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
        bag.add(test);
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> bag.remove(test, 0));
    }

    @Test
    void removeMoreElementsThanInHashBag() {
        //given
        String test = "test";
        HashBag<String> bag = new HashBag<>();
        bag.add(test, 10);
        //when
        bag.remove(test, 20);
        //then
        assertFalse(bag.contains(test));
        assertEquals(0, bag.count(test));
    }

    @Test
    void elementWhichHasSameHashCodeIsNotRemoved() {
        //given
        HashBag<SetHashObject> bag = new HashBag<>();
        SetHashObject obj1 = new SetHashObject(1, true);
        SetHashObject obj2 = new SetHashObject(1, false);
        bag.add(obj1);
        bag.add(obj2);
        //when
        bag.remove(obj2);
        //then
        assertTrue(bag.contains(obj1));
        assertEquals(1, bag.count(obj1));
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
    void hashBagRehash() {
        //given
        SetHashObject obj1 = new SetHashObject(1);
        SetHashObject obj2 = new SetHashObject(2);
        HashBag<SetHashObject> bag = new HashBag<>(10, 0.75f);
        bag.add(obj1);
        bag.add(obj2);
        float oldLoadfactor = bag.getLoadFactor();

        // when
        bag.rehash();

        //then
        assertTrue(oldLoadfactor > bag.getLoadFactor());

    }

    @Test
    void newHashBagElementsIsEmpty() {
        //given
        HashBag<String> bag = new HashBag<>();

        //when
        Object[] elements = bag.elements();

        //then
        assertEquals(0, elements.length);
    }

    @Test
    void hashBagElementsContainsElements() {
        //given
        HashBag<SetHashObject> bag = new HashBag<>(10, 0.75f);
        SetHashObject obj1 = new SetHashObject(1);
        SetHashObject obj2 = new SetHashObject(2);
        bag.add(obj1);
        bag.add(obj2);

        //when
        Object[] elements = bag.elements();

        //then
        assertEquals(2, elements.length);
        assertTrue(arrayContains(elements, obj1));
        assertTrue(arrayContains(elements, obj2));
    }

    @Test
    void checkIfElementsContainsSameElementMultipleTimes() {
        //given
        HashBag<SetHashObject> bag = new HashBag<>(10, 0.75f);
        SetHashObject obj1 = new SetHashObject(1);
        bag.add(obj1, 5);

        //when
        Object[] elements = bag.elements();

        //then
        assertEquals(5, elements.length);
        assertTrue(arrayContains(elements, obj1));
    }

    /* Null Tests */
    @Test
    void addNullToHashBag() {
        //given
        HashBag<String> bag = new HashBag<>();

        //when
        bag.add(null);

        //then
        assertTrue(bag.contains(null));
        assertEquals(1, bag.count(null));
    }

    @Test
    void addMultipleNullValuesToHashBag() {
        //given
        HashBag<String> bag = new HashBag<>();

        //when
        bag.add(null, 10);

        //then
        assertTrue(bag.contains(null));
        assertEquals(10, bag.count(null));
    }

    @Test
    void hashBagDoesNotContainNullWhenNotEmpty() {
        //given
        HashBag<String> bag = new HashBag<>();
        String test = "test";
        bag.add(test);
        //when
        //then
        assertFalse(bag.contains(null));
    }

    @Test
    void removeNullValueFromHashBag() {
        //given
        HashBag<String> bag = new HashBag<>();
        bag.add(null);

        //when
        bag.remove(null);

        //then
        assertFalse(bag.contains(null));
        assertEquals(0, bag.count(null));
    }

    @Test
    void removePartOfTheNullValueFromHashBag() {
        //given
        HashBag<String> bag = new HashBag<>();
        bag.add(null, 10);

        //when
        bag.remove(null, 5);

        //then
        assertTrue(bag.contains(null));
        assertEquals(5, bag.count(null));
    }

    @Test
    void elementsContainsNullValues() {
        //given
        HashBag<String> bag = new HashBag<>();
        bag.add(null);

        //when
        Object[] elements = bag.elements();

        //then
        assertTrue(arrayContains(elements, null));
    }


    /* Helper Methods */

    /**
     * Method to check if an element is in an array
     * @param objects the array which should be checked
     * @param element the element which should be in the array
     * @return if the element was in the array
     */
    private boolean arrayContains(Object[] objects, Object element) {
        for (Object obj :
                objects) {
            if(Objects.equals(obj, element)) {
                return true;
            }
        }
        return false;
    }

}
