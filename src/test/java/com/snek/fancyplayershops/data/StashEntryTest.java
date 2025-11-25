package com.snek.fancyplayershops.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StashEntry data class.
 * Tests the entry functionality without requiring ItemStack.
 */
class StashEntryTest {

    private StashEntry entry;

    @BeforeEach
    void setUp() {
        // Create entry with null ItemStack for testing without Minecraft
        entry = new StashEntry(null);
    }

    @Test
    void testInitialCount_Zero() {
        assertEquals(0, entry.getCount(), "New StashEntry should have count of 0");
    }

    @Test
    void testAdd_PositiveValue() {
        entry.add(10);
        assertEquals(10, entry.getCount(), "Count should be 10 after adding 10");
    }

    @Test
    void testAdd_MultiplePositiveValues() {
        entry.add(5);
        entry.add(10);
        entry.add(3);

        assertEquals(18, entry.getCount(), "Count should accumulate: 5 + 10 + 3 = 18");
    }

    @Test
    void testAdd_Zero() {
        entry.add(10);
        entry.add(0);

        assertEquals(10, entry.getCount(), "Adding zero should not change count");
    }

    @Test
    void testAdd_NegativeValue() {
        entry.add(20);
        entry.add(-5);

        assertEquals(15, entry.getCount(), "Negative values should decrease count");
    }

    @Test
    void testAdd_ResultingInNegative() {
        entry.add(10);
        entry.add(-15);

        assertEquals(-5, entry.getCount(), "Count can go negative");
    }

    @Test
    void testAdd_LargeValue() {
        entry.add(Integer.MAX_VALUE / 2);
        entry.add(Integer.MAX_VALUE / 2);

        assertTrue(entry.getCount() > 0, "Large values should be added");
    }

    @Test
    void testAdd_Overflow() {
        // Testing integer overflow behavior
        entry.add(Integer.MAX_VALUE);
        entry.add(1);

        // In Java, int overflow wraps to negative
        assertTrue(entry.getCount() < 0, "Integer overflow should wrap to negative");
    }

    @Test
    void testAdd_SequentialOperations() {
        entry.add(100);
        assertEquals(100, entry.getCount());

        entry.add(50);
        assertEquals(150, entry.getCount());

        entry.add(-30);
        assertEquals(120, entry.getCount());

        entry.add(-120);
        assertEquals(0, entry.getCount());
    }

    @Test
    void testItemField_Null() {
        // Testing with null item
        assertNull(entry.item, "Item field should be null when initialized with null");
    }

    @Test
    void testItemField_Final() {
        // The item field is final, so we verify it remains the same reference
        Object initialItem = entry.item;

        entry.add(100);
        entry.add(-50);

        assertSame(initialItem, entry.item, "Item reference should not change after add operations");
    }

    @Test
    void testMultipleEntriesIndependence() {
        StashEntry entry1 = new StashEntry(null);
        StashEntry entry2 = new StashEntry(null);

        entry1.add(100);
        entry2.add(200);

        assertEquals(100, entry1.getCount(), "Entry1 should have count 100");
        assertEquals(200, entry2.getCount(), "Entry2 should have count 200");

        entry1.add(50);
        assertEquals(150, entry1.getCount(), "Entry1 should be modified");
        assertEquals(200, entry2.getCount(), "Entry2 should remain unchanged");
    }

    @Test
    void testGetCount_ReadOnly() {
        entry.add(42);

        // Call getCount multiple times
        int count1 = entry.getCount();
        int count2 = entry.getCount();
        int count3 = entry.getCount();

        assertEquals(42, count1, "First getCount should return 42");
        assertEquals(42, count2, "Second getCount should return 42");
        assertEquals(42, count3, "Third getCount should return 42");
        assertEquals(count1, count2, "Multiple calls should return same value");
        assertEquals(count2, count3, "Multiple calls should return same value");
    }

    @Test
    void testConcurrentModification() {
        // Simulate rapid additions as might happen in concurrent scenarios
        for (int i = 0; i < 1000; i++) {
            entry.add(1);
        }

        assertEquals(1000, entry.getCount(), "Count should be 1000 after 1000 additions of 1");
    }

    @Test
    void testAddition_AlternatingValues() {
        entry.add(10);
        entry.add(-5);
        entry.add(10);
        entry.add(-5);
        entry.add(10);
        entry.add(-5);

        assertEquals(15, entry.getCount(), "Alternating additions: (10-5)*3 = 15");
    }

    @Test
    void testMinimumValue() {
        entry.add(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, entry.getCount(), "Should handle minimum integer value");
    }

    @Test
    void testMaximumValue() {
        entry.add(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, entry.getCount(), "Should handle maximum integer value");
    }
}
