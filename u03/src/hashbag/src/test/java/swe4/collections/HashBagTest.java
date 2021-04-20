package swe4.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class HashBagTest {

    /* Constructor Tests */

    @Test
    @DisplayName("Create new HashBag with zero capacity, expect Exception")
    void newHashBagCapacitySmallerZero_expectException() {
        //given
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> new HashBag<>(-1, 0.5f));
    }

    @Test
    @DisplayName("Create new HashBag with zero loadfactor, expect Exception")
    void newHashBagLoadFactorEqualZero_expectException() {
        //given
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> new HashBag<>(10, 0));
    }

    @Test
    @DisplayName("Create new HashBag with loadfactor greater than one, expect Exception")
    void newHashBagLoadFactorGreaterOne_expectException() {
        //given
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> new HashBag<>(10, 100));
    }

    /* Iterator Tests */

    @Test
    @DisplayName("Iterator returns Objects correctly, even when the underlying array has gaps with null")
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
    @DisplayName("Create new HashBag with zero loadfactor, expect Exception")
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
    @DisplayName("Iterator returns items which are multiple times in the HashBag")
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
    @DisplayName("Iterator does not have a next item when the HashBag is empty")
    void iteratorDoesNotHaveNext_whenHashBagEmpty() {
        //given
        HashBag<String> bag = new HashBag<>();

        //when
        Iterator<String> it = bag.iterator();

        //then
        assertFalse(it.hasNext());
    }

    @Test
    @DisplayName("Iterator returns null when HashBag empty")
    void iteratorReturnsNull_whenHashBagEmpty() {
        //given
        HashBag<String> bag = new HashBag<>();

        //when
        Iterator<String> it = bag.iterator();

        //then
        assertNull(it.next());
    }

    @Test
    @DisplayName("Iterator correctly returns the first element")
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
    @DisplayName("A new HashBag is empty")
    void newHashBagIsEmpty() {
        //given
        //when
        HashBag<String> bag = new HashBag<>();
        //then
        assertTrue(bag.isEmpty());
    }

    @Test
    @DisplayName("A new HashBag has no elements")
    void newHashBagContainsNoElement() {
        //given
        String test = "test";
        //when
        HashBag<String> bag = new HashBag<>();
        //then
        assertFalse(bag.contains(test));
    }

    @Test
    @DisplayName("New HashBag does not have a count for an element")
    void newHashBagCountsZeroElements() {
        //given
        String test = "test";
        //when
        HashBag<String> bag = new HashBag<>();
        //then
        assertEquals(0, bag.count(test));
    }

    @Test
    @DisplayName("New HashBag has a loadfactor of zero")
    void newHashBagHasZeroLoadFactor() {
        //given
        //when
        HashBag<String> bag = new HashBag<>();
        //then
        assertEquals(0.0f, bag.getLoadFactor());
    }

    @Test
    @DisplayName("New HashBag has a size of zero")
    void newHashBagHasZeroSize() {
        //given
        //when
        HashBag<String> bag = new HashBag<>();
        //then
        assertEquals(0, bag.size());
    }

    @Test
    @DisplayName("HashBag with multiple elements has correct size")
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
    @DisplayName("HashBag with multiple collidingeElements has correct size")
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
    @DisplayName("HashBag contains element")
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
    @DisplayName("HashBag is not empty after element was added")
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
    @DisplayName("HashBag contains exactly one element")
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
    @DisplayName("HashBag contains exactly ten elements")
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
    @DisplayName("HashBag throws exception, when zero elements are added")
    void addZeroElements_expectException() {
        //given
        String test = "test";
        HashBag<String> bag = new HashBag<>();
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> bag.add(test, 0));
    }

    @Test
    @DisplayName("HashBag rehashes, when loadfactor reached")
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
    @DisplayName("HashBag does not contain elements which have equal hashcode but are not equal")
    void hashBagWithCollisionCheckIfNonExistingElementIsNotPresent() {
        //given
        SetHashObject obj1 = new SetHashObject(1, true);
        SetHashObject obj2 = new SetHashObject(1, false);
        HashBag<SetHashObject> bag = new HashBag<>(1, 1.0f);
        //when
        bag.add(obj1);
        //then
        assertTrue(bag.contains(obj1));
        assertFalse(bag.contains(obj2));
    }

    /**
     * The problem with this implementation, is that the HashBag will never rehash
     * if the loadfactor is set to 100%.
     * This isn't bug, but behaviour which can be unexpected
     */
    @Test
    @DisplayName("HashBag contains both colliding elements")
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
    @DisplayName("HashBag was correctly cleared")
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
    @DisplayName("HashBag does not contain removed element")
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
    @DisplayName("HashBag correctly removes number of elements")
    void hashBagCorrectlyRemovesNumberOfElements() {
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
    @DisplayName("HashBag does not contain any element when all removed")
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
    @DisplayName("HashBag correctly removes non existing element")
    void removeNotExistingElement() {
        //given
        String test = "test";
        HashBag<String> bag = new HashBag<>();
        //when
        //then
        assertEquals(0, bag.remove(test));
    }

    @Test
    @DisplayName("HashBag throws exception when removing zero elements")
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
    @DisplayName("HashBag correctly removes all elements, when remove count is higher")
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
    @DisplayName("Elements with same hashcode are not removed, when not equal")
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
    @DisplayName("laodfactor is correctly calculated")
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
    @DisplayName("HashBag correctly rehashes")
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
    @DisplayName("Elements array of empty HashBag is empty")
    void newHashBagElementsIsEmpty() {
        //given
        HashBag<String> bag = new HashBag<>();

        //when
        Object[] elements = bag.elements();

        //then
        assertEquals(0, elements.length);
    }

    @Test
    @DisplayName("Elements array of HashBag is not empty")
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
    @DisplayName("Elements array contains the same element multiple times")
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
    @DisplayName("Add null to HashBag")
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
    @DisplayName("Add null multiple times")
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
    @DisplayName("Not empty HashBag does not contain null")
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
    @DisplayName("Remove null")
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
    @DisplayName("Remove some null-values")
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
    @DisplayName("Elements array contains null")
    void elementsContainsNullValues() {
        //given
        HashBag<String> bag = new HashBag<>();
        bag.add(null);

        //when
        Object[] elements = bag.elements();

        //then
        assertTrue(arrayContains(elements, null));
    }

    @Test
    @DisplayName("Add returns zero when HashBag empty")
    void addReturnsZero_whenHashbagEmpty() {
        //given
        String test = "test";
        HashBag<String> bag = new HashBag<>();

        //when
        int count = bag.add(test);

        //then
        assertEquals(0, count);
    }

    @Test
    @DisplayName("Add returns correct number of pre existing equal elements when HashBag not empty")
    void addReturnsCorrectNumber_whenHashbagNotEmpty() {
        //given
        String test = "test";
        HashBag<String> bag = new HashBag<>();

        //when
        bag.add(test);
        int count = bag.add(test);

        //then
        assertEquals(1, count);
    }

    @Test
    @DisplayName("Add returns zero when no equal element in HashBag not empty")
    void addReturnZero_whenNoEqualElementInHashBag() {
        //given
        SetHashObject obj1 = new SetHashObject(1);
        SetHashObject obj2 = new SetHashObject(2);
        HashBag<SetHashObject> bag = new HashBag<>();

        //when
        bag.add(obj1);
        int count = bag.add(obj2);

        //then
        assertEquals(0, count);
    }

    @Test
    @DisplayName("Add returns zero when elements collide in HashBag not empty")
    void addReturnZero_whenElementsCollideInHashBag() {
        //given
        SetHashObject obj1 = new SetHashObject(1, true);
        SetHashObject obj2 = new SetHashObject(1, false);
        HashBag<SetHashObject> bag = new HashBag<>();

        //when
        bag.add(obj1);
        int count = bag.add(obj2);

        //then
        assertEquals(0, count);
    }

    @Test
    @DisplayName("Remove returns zero when HashBag empty")
    void removeReturnsZero_whenHashBagEmpty() {
        //given
        SetHashObject obj1 = new SetHashObject(1);
        HashBag<SetHashObject> bag = new HashBag<>();

        //when
        int count = bag.remove(obj1);

        //then
        assertEquals(0, count);
    }

    @Test
    @DisplayName("Remove returns number of existing elements, when HashBag not empty")
    void removeReturnsNumberOfExistingElements_whenHashBagNotEmpty() {
        //given
        SetHashObject obj1 = new SetHashObject(1);
        HashBag<SetHashObject> bag = new HashBag<>();
        bag.add(obj1, 10);

        //when
        int count = bag.remove(obj1);

        //then
        assertEquals(10, count);
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
