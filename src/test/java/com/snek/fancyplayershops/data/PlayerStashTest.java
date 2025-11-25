package com.snek.fancyplayershops.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PlayerStash data class.
 * Tests the stash functionality without Minecraft dependencies.
 */
class PlayerStashTest {

    private PlayerStash stash;
    private UUID itemUuid;

    @BeforeEach
    void setUp() {
        stash = new PlayerStash();
        itemUuid = UUID.randomUUID();
    }

    @Test
    void testScheduledForSave_DefaultFalse() {
        assertFalse(stash.isScheduledForSave(), "New stash should not be scheduled for save");
    }

    @Test
    void testScheduledForSave_SetTrue() {
        stash.setScheduledForSave(true);
        assertTrue(stash.isScheduledForSave(), "Stash should be scheduled for save after setting true");
    }

    @Test
    void testScheduledForSave_SetFalse() {
        stash.setScheduledForSave(true);
        stash.setScheduledForSave(false);
        assertFalse(stash.isScheduledForSave(), "Stash should not be scheduled after setting false");
    }

    @Test
    void testScheduledForSave_Toggle() {
        stash.setScheduledForSave(true);
        assertTrue(stash.isScheduledForSave());
        stash.setScheduledForSave(false);
        assertFalse(stash.isScheduledForSave());
        stash.setScheduledForSave(true);
        assertTrue(stash.isScheduledForSave());
    }

    @Test
    void testEmptyStash() {
        assertTrue(stash.isEmpty(), "New stash should be empty");
        assertEquals(0, stash.size(), "New stash should have size 0");
    }

    @Test
    void testPutAndGet() {
        StashEntry entry = new StashEntry(null); // Will test with null ItemStack
        stash.put(itemUuid, entry);

        assertEquals(1, stash.size(), "Stash should contain one entry");
        assertSame(entry, stash.get(itemUuid), "Should retrieve the same entry");
    }

    @Test
    void testPutMultiple() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        UUID uuid3 = UUID.randomUUID();

        StashEntry entry1 = new StashEntry(null);
        StashEntry entry2 = new StashEntry(null);
        StashEntry entry3 = new StashEntry(null);

        stash.put(uuid1, entry1);
        stash.put(uuid2, entry2);
        stash.put(uuid3, entry3);

        assertEquals(3, stash.size(), "Stash should contain three entries");
        assertSame(entry1, stash.get(uuid1));
        assertSame(entry2, stash.get(uuid2));
        assertSame(entry3, stash.get(uuid3));
    }

    @Test
    void testRemove() {
        StashEntry entry = new StashEntry(null);
        stash.put(itemUuid, entry);

        StashEntry removed = stash.remove(itemUuid);

        assertSame(entry, removed, "Should return the removed entry");
        assertTrue(stash.isEmpty(), "Stash should be empty after removal");
        assertNull(stash.get(itemUuid), "Removed entry should not be retrievable");
    }

    @Test
    void testContainsKey() {
        assertFalse(stash.containsKey(itemUuid), "Empty stash should not contain key");

        stash.put(itemUuid, new StashEntry(null));
        assertTrue(stash.containsKey(itemUuid), "Stash should contain added key");
    }

    @Test
    void testClear() {
        stash.put(UUID.randomUUID(), new StashEntry(null));
        stash.put(UUID.randomUUID(), new StashEntry(null));
        stash.put(UUID.randomUUID(), new StashEntry(null));

        assertEquals(3, stash.size());
        stash.clear();
        assertTrue(stash.isEmpty(), "Stash should be empty after clear");
    }

    @Test
    void testComputeIfAbsent() {
        UUID uuid = UUID.randomUUID();

        StashEntry entry = stash.computeIfAbsent(uuid, k -> new StashEntry(null));
        assertNotNull(entry, "ComputeIfAbsent should return an entry");
        assertEquals(1, stash.size(), "Stash should contain the computed entry");

        // Second call should return same entry without recomputing
        StashEntry sameEntry = stash.computeIfAbsent(uuid, k -> new StashEntry(null));
        assertSame(entry, sameEntry, "Should return existing entry on second call");
        assertEquals(1, stash.size(), "Size should remain 1");
    }

    @Test
    void testNullKey() {
        // HashMap allows null keys
        StashEntry entry = new StashEntry(null);
        stash.put(null, entry);

        assertTrue(stash.containsKey(null), "Should handle null keys");
        assertSame(entry, stash.get(null), "Should retrieve entry with null key");
    }

    @Test
    void testEqualsAndHashCode() {
        PlayerStash stash1 = new PlayerStash();
        PlayerStash stash2 = new PlayerStash();

        assertEquals(stash1, stash2, "Empty stashes should be equal");
        assertEquals(stash1.hashCode(), stash2.hashCode(), "Empty stashes should have same hash code");

        UUID uuid = UUID.randomUUID();
        StashEntry entry = new StashEntry(null);

        stash1.put(uuid, entry);
        stash2.put(uuid, entry);

        assertEquals(stash1, stash2, "Stashes with same content should be equal");
        assertEquals(stash1.hashCode(), stash2.hashCode(), "Stashes with same content should have same hash code");
    }

    @Test
    void testNotEquals() {
        PlayerStash stash1 = new PlayerStash();
        PlayerStash stash2 = new PlayerStash();

        stash1.put(UUID.randomUUID(), new StashEntry(null));

        assertNotEquals(stash1, stash2, "Stashes with different content should not be equal");
    }

    @Test
    void testScheduledFlagIndependentOfContent() {
        PlayerStash stash1 = new PlayerStash();
        PlayerStash stash2 = new PlayerStash();

        stash1.setScheduledForSave(true);
        stash2.setScheduledForSave(false);

        // The scheduled flag should not affect equality (it's transient-like)
        assertEquals(stash1, stash2, "Empty stashes should be equal regardless of scheduled flag");
    }
}
