package com.snek.fancyplayershops.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PlayerShopBalance data class.
 * Tests balance functionality without Minecraft dependencies.
 */
class PlayerShopBalanceTest {

    private PlayerShopBalance balance;

    @BeforeEach
    void setUp() {
        balance = new PlayerShopBalance();
    }

    @Test
    void testDefaultConstructor_ZeroBalance() {
        assertEquals(0, balance.getValue(), "Default balance should be 0");
    }

    @Test
    void testConstructorWithValue() {
        PlayerShopBalance balance = new PlayerShopBalance(1000);
        assertEquals(1000, balance.getValue(), "Balance should be initialized with provided value");
    }

    @Test
    void testConstructorWithNegativeValue() {
        PlayerShopBalance balance = new PlayerShopBalance(-500);
        assertEquals(-500, balance.getValue(), "Balance should accept negative values");
    }

    @Test
    void testConstructorWithLargeValue() {
        long largeValue = Long.MAX_VALUE;
        PlayerShopBalance balance = new PlayerShopBalance(largeValue);
        assertEquals(largeValue, balance.getValue(), "Balance should handle Long.MAX_VALUE");
    }

    @Test
    void testScheduledForSave_DefaultFalse() {
        assertFalse(balance.isScheduledForSave(), "New balance should not be scheduled for save");
    }

    @Test
    void testScheduledForSave_SetTrue() {
        balance.setScheduledForSave(true);
        assertTrue(balance.isScheduledForSave(), "Balance should be scheduled for save after setting true");
    }

    @Test
    void testScheduledForSave_SetFalse() {
        balance.setScheduledForSave(true);
        balance.setScheduledForSave(false);
        assertFalse(balance.isScheduledForSave(), "Balance should not be scheduled after setting false");
    }

    @Test
    void testMerge_BothZero() {
        PlayerShopBalance a = new PlayerShopBalance(0);
        PlayerShopBalance b = new PlayerShopBalance(0);

        PlayerShopBalance result = PlayerShopBalance.merge(a, b);

        assertSame(a, result, "Merge should return the first balance object");
        assertEquals(0, result.getValue(), "Merged balance should be 0");
    }

    @Test
    void testMerge_PositiveValues() {
        PlayerShopBalance a = new PlayerShopBalance(1000);
        PlayerShopBalance b = new PlayerShopBalance(500);

        PlayerShopBalance result = PlayerShopBalance.merge(a, b);

        assertSame(a, result, "Merge should return the first balance object");
        assertEquals(1500, result.getValue(), "Merged balance should be sum of both values");
    }

    @Test
    void testMerge_NegativeValues() {
        PlayerShopBalance a = new PlayerShopBalance(-300);
        PlayerShopBalance b = new PlayerShopBalance(-200);

        PlayerShopBalance result = PlayerShopBalance.merge(a, b);

        assertEquals(-500, result.getValue(), "Merged balance should handle negative values");
    }

    @Test
    void testMerge_MixedValues() {
        PlayerShopBalance a = new PlayerShopBalance(1000);
        PlayerShopBalance b = new PlayerShopBalance(-300);

        PlayerShopBalance result = PlayerShopBalance.merge(a, b);

        assertEquals(700, result.getValue(), "Merged balance should correctly add positive and negative");
    }

    @Test
    void testMerge_ResultingZero() {
        PlayerShopBalance a = new PlayerShopBalance(500);
        PlayerShopBalance b = new PlayerShopBalance(-500);

        PlayerShopBalance result = PlayerShopBalance.merge(a, b);

        assertEquals(0, result.getValue(), "Merged balance should be 0 when values cancel out");
    }

    @Test
    void testMerge_LargeValues() {
        PlayerShopBalance a = new PlayerShopBalance(Long.MAX_VALUE / 2);
        PlayerShopBalance b = new PlayerShopBalance(Long.MAX_VALUE / 2);

        PlayerShopBalance result = PlayerShopBalance.merge(a, b);

        // Note: This will actually be close to MAX_VALUE but not overflow due to division
        assertTrue(result.getValue() > 0, "Large values should be added");
    }

    @Test
    void testMerge_Overflow() {
        // Testing overflow behavior - in Java, long overflow wraps around
        PlayerShopBalance a = new PlayerShopBalance(Long.MAX_VALUE);
        PlayerShopBalance b = new PlayerShopBalance(1);

        PlayerShopBalance result = PlayerShopBalance.merge(a, b);

        // This will overflow to negative
        assertTrue(result.getValue() < 0, "Overflow should wrap to negative");
    }

    @Test
    void testMerge_MultipleTimes() {
        PlayerShopBalance a = new PlayerShopBalance(100);
        PlayerShopBalance b1 = new PlayerShopBalance(50);
        PlayerShopBalance b2 = new PlayerShopBalance(75);
        PlayerShopBalance b3 = new PlayerShopBalance(25);

        PlayerShopBalance.merge(a, b1);
        assertEquals(150, a.getValue(), "First merge should add 50");

        PlayerShopBalance.merge(a, b2);
        assertEquals(225, a.getValue(), "Second merge should add 75");

        PlayerShopBalance.merge(a, b3);
        assertEquals(250, a.getValue(), "Third merge should add 25");
    }

    @Test
    void testMerge_DoesNotModifySecond() {
        PlayerShopBalance a = new PlayerShopBalance(1000);
        PlayerShopBalance b = new PlayerShopBalance(500);

        PlayerShopBalance.merge(a, b);

        assertEquals(1500, a.getValue(), "First balance should be modified");
        assertEquals(500, b.getValue(), "Second balance should remain unchanged");
    }

    @Test
    void testMerge_PreservesScheduledFlag() {
        PlayerShopBalance a = new PlayerShopBalance(100);
        PlayerShopBalance b = new PlayerShopBalance(200);

        a.setScheduledForSave(true);

        PlayerShopBalance result = PlayerShopBalance.merge(a, b);

        assertTrue(result.isScheduledForSave(), "Merge should preserve scheduled flag of first balance");
    }

    @Test
    void testMerge_WithZero() {
        PlayerShopBalance a = new PlayerShopBalance(1000);
        PlayerShopBalance b = new PlayerShopBalance(0);

        PlayerShopBalance result = PlayerShopBalance.merge(a, b);

        assertEquals(1000, result.getValue(), "Merging with zero should not change value");
    }

    @Test
    void testValueImmutability() {
        PlayerShopBalance balance = new PlayerShopBalance(1000);
        long value = balance.getValue();

        // getValue should return the same value
        assertEquals(value, balance.getValue(), "getValue should return consistent value");
        assertEquals(value, balance.getValue(), "getValue should return consistent value on multiple calls");
    }
}
