package com.aaronhible.datastructures.set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class HashTableTest {

    @Test
    public void add() {
        final HashTable<String, String> hashTable = new HashTable<>();
        final String first = RandomStringUtils.random(5);
        final String second = RandomStringUtils.random(5);

        hashTable.add(first, RandomStringUtils.random(5));
        assertEquals(1, hashTable.size());
        hashTable.add(second, RandomStringUtils.random(5));
        assertEquals(2, hashTable.size());
        // add first again
        hashTable.add(first, RandomStringUtils.random(5));
        // size should still be 2
        assertEquals(2, hashTable.size());

    }

    @Test
    public void add_bigRandom() {
        final int size = 10000;
        final HashTable<String, String> hashTable = new HashTable<>(size);

        // we expect collisions
        for (int loop = 0; loop < size; loop++) {
            final String key = RandomStringUtils.random(5);
            final String value = RandomStringUtils.random(5);
            hashTable.add(key, value);
        }

        assertEquals(size, hashTable.size());
    }

    @Test
    public void get() {

        final HashTable<String, String> hashTable = new HashTable<>();

        final String first = RandomStringUtils.random(5);
        final String firstValue = RandomStringUtils.random(5);
        final String second = RandomStringUtils.random(5);
        final String secondValue = RandomStringUtils.random(5);

        hashTable.add(first, firstValue);
        hashTable.add(second, secondValue);

        final String expectFirst = hashTable.get(first);
        assertEquals(firstValue, expectFirst);

        final String expectSecond = hashTable.get(second);
        assertEquals(secondValue, expectSecond);

        final String anotherValue = RandomStringUtils.random(5);
        hashTable.add(first, anotherValue);

        final String expectAnother = hashTable.get(first);
        assertEquals(anotherValue, expectAnother);

    }

    @Test
    public void remove_bigRandom() {
        // add a bunch
        final int size = 10000;
        final HashTable<String, String> hashTable = new HashTable<>(size);
        final Set<String> stateSet = new HashSet<>();

        // we expect collisions
        for (int loop = 0; loop < size; loop++) {
            final String key = RandomStringUtils.random(5);
            stateSet.add(key);
            final String value = RandomStringUtils.random(5);
            hashTable.add(key, value);
        }

        assertEquals(size, hashTable.size());

        int expectedSize = size;
        for (final String key : stateSet) {
            final Object removed = hashTable.remove(key);
            assertNotNull(removed);
            assertEquals(--expectedSize, hashTable.size());
        }
    }

    @Test
    public void remove() {

        final HashTable<String, String> hashTable = new HashTable<>();
        final String first = RandomStringUtils.random(5);
        final String firstValue = RandomStringUtils.random(5);
        final String second = RandomStringUtils.random(5);
        final String secondValue = RandomStringUtils.random(5);
        hashTable.add(first, firstValue);
        hashTable.add(second, secondValue);

        final String removed = (String) hashTable.remove(second);

        assertEquals(secondValue, removed);
        assertEquals(1, hashTable.size());
        assertFalse(hashTable.contains(second));
        assertTrue(hashTable.contains(first));
    }

    @Test
    public void contains() {
        final HashTable<String, String> hashTable = new HashTable<>();
        final String first = RandomStringUtils.random(5);

        assertFalse(hashTable.contains(first));
        hashTable.add(first, RandomStringUtils.random(5));
        assertTrue(hashTable.contains(first));

    }

    @Test
    public void isValuesEqual() {
        final HashTable<Integer, Integer> hashTable = new HashTable<>();

        // same reference
        Integer one = new Integer(1);
        Integer two = one;
        assertTrue(hashTable.isValuesEqual(one, two));

        final HashTable<String, String> stringHashTable = new HashTable<>();
        // same value
        final String random = RandomStringUtils.random(3);
        // must new up string to not have the same reference but same value
        final String first = new String(random);
        final String second = new String(random);
        assertTrue(stringHashTable.isValuesEqual(first, second));

        // both null
        assertTrue(stringHashTable.isValuesEqual(null, null));

        // lhs is null
        assertFalse(stringHashTable.isValuesEqual(null, random));

        // rhs is null
        assertFalse(stringHashTable.isValuesEqual(random, null));

        // unequal value
        one = new Integer(1);
        two = new Integer(2);
        assertFalse(hashTable.isValuesEqual(one, two));

    }

    @Test
    public void size() {
        final int size = 1000;
        final HashTable<Integer, String> hashTable = new HashTable<>();
        for (int integer = 0; integer < size; integer++) {
            hashTable.add(integer, RandomStringUtils.random(5));
        }
        assertEquals(size, hashTable.size());
    }

    @Test
    public void hashAndIndex() {
        final int capacity = 100;
        final int expectedHash = 33;
        final int expectedIndex = 33;
        final String keyToHash = "34";

        final HashTable<String, String> hashTable = new HashTable<>();
        final int hash = hashTable.hash(keyToHash, capacity);

        assertEquals(expectedHash, hash);

        final int index = hashTable.index(hash, capacity);
        assertTrue((0 <= index) && (index < (capacity - 1)));
        assertEquals(expectedIndex, index);

    }

    // @Test
    // public void javasHashCodeImplementation() {
    // final int size = 10000;
    // final Object[] array = new Object[size];
    // int collisionCount = 0;
    // for (int value = 0; value < size; value++) {
    // final String elementValue = RandomStringUtils.random(5);
    // int index = hashExpanded(elementValue);
    // index = index & size - 1;
    // if (array[index] != null) {
    // collisionCount++;
    // System.out.println("collision");
    // }
    // array[index] = elementValue;
    // }
    // System.out.println(Arrays.toString(array));
    // System.out.println("Collisions:" + collisionCount);
    //
    // }

    // private static int indexFromHashMap() {
    // // h = hash
    // // length = bucket array length (minus one to get to the last index)
    // // (h & length - 1)
    // }
    // private static int hashFromHashMap(final String k) {
    // int h = 0;
    // h ^= k.hashCode();
    //
    // // This function ensures that hashCodes that differ only by
    // // constant multiples at each bit position have a bounded
    // // number of collisions (approximately 8 at default load factor).
    // h ^= (h >>> 20) ^ (h >>> 12);
    // return h ^ (h >>> 7) ^ (h >>> 4);
    // }

    /***
     * This works great for integers, but can cause significant collisions for other types of keys. Since in
     * practicality keys are more often strings, collisions would occur more frequently. There are techniques such as
     * separate chaining that can help with collisions, but they come at the cost of performance. The best thing to do
     * is to accept that collisions will happen, but try to provide an algorithm where they don't happen very
     * frequently.
     */
    @Test
    public void simpleModHash_String() {
        final HashTable<String, String> hashTable = new HashTable<>();
        final int size = 10000;
        final Object[] array = new Object[size];
        int collisionCount = 0;
        for (int value = 0; value < size; value++) {
            final String elementValue = RandomStringUtils.random(5);
            final int hash = hashTable.hash(elementValue, size);
            final int index = hashTable.index(hash, size);
            if (array[index] != null) {
                collisionCount++;
            }
            array[index] = elementValue;
        }
        System.out.println(getNotNullElements(array) + " out of " + size + " buckets with " + collisionCount
                        + " collisions");
    }

    /**
     * @param array
     * @return
     */
    private static int getNotNullElements(final Object[] array) {
        int notNull = 0;
        for (final Object object : array) {
            if (object != null) {
                notNull++;
            }
        }
        return notNull;
    }

}
