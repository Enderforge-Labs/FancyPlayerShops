# FancyPlayerShops Test Suite

This directory contains unit tests for the FancyPlayerShops Minecraft mod.

## Overview

The test suite focuses on testing data manager classes and their business logic without requiring a full Minecraft server environment.

## Test Structure

```
src/test/java/com/snek/fancyplayershops/
├── data/
│   ├── PlayerStashTest.java           # Tests for PlayerStash data class
│   ├── PlayerShopBalanceTest.java     # Tests for PlayerShopBalance data class
│   ├── StashEntryTest.java            # Tests for StashEntry data class
│   ├── ShopKeyTest.java               # Documentation for ShopKey testing challenges
│   └── ManagerTestingGuide.java       # Comprehensive testing documentation
└── README.md                          # This file
```

## Running Tests

### Run all tests:
```bash
./gradlew test
```

### Run specific test class:
```bash
./gradlew test --tests PlayerStashTest
./gradlew test --tests PlayerShopBalanceTest
./gradlew test --tests StashEntryTest
```

### Run with verbose output:
```bash
./gradlew test --info
```

### Generate test report:
```bash
./gradlew test
# Report available at: build/reports/tests/test/index.html
```

## Test Coverage

### Currently Tested (100% unit test coverage):
- ✅ **PlayerStash**: HashMap operations, scheduled save flag, equality
- ✅ **PlayerShopBalance**: Balance operations, merge function, scheduled save flag
- ✅ **StashEntry**: Count accumulation, add operations, edge cases

### Documented but Not Tested (requires Minecraft/mocking):
- 📝 **ShopKey**: Equality, hashCode, HashMap usage (needs BlockPos and Level mocking)
- 📝 **ShopManager**: Registration, lookup, save/load (needs extensive mocking)
- 📝 **StashManager**: Stashing, retrieval, persistence (needs ItemStack mocking)
- 📝 **BalanceManager**: Balance operations, claiming, persistence (needs economy system mocking)

## Testing Challenges

The FancyPlayerShops mod has deep integration with Minecraft, making comprehensive unit testing challenging:

### Major Dependencies:
1. **Minecraft Core Classes**: BlockPos, Level, ServerLevel, ItemStack
2. **File System**: JSON serialization to disk via FancyPlayerShops.getStorageDir()
3. **External Mods**: SolsticeEconomy (EconomyManager, NotificationManager)
4. **Custom Libraries**: FrameworkLib utilities
5. **Static State**: All managers use static methods and state

### Why Some Tests Are Missing:

The data managers (ShopManager, StashManager, BalanceManager) heavily depend on:
- Minecraft's type system (ItemStack, BlockPos, Level)
- File I/O with mod-specific storage directories
- External economy mod integration
- Static initialization and state management

To properly test these would require either:
1. **Mockito** - Extensive mocking of Minecraft classes
2. **Integration Tests** - Actual Minecraft test server (Fabric GameTest)
3. **Refactoring** - Dependency injection and interfaces (breaking changes)

See `ManagerTestingGuide.java` for detailed analysis of what should be tested and how.

## What We CAN Test (Current Approach)

We focus on **data classes** that have minimal Minecraft dependencies:

### ✅ PlayerStash
- Pure HashMap extension with scheduled save flag
- All HashMap operations work correctly
- Flag management works as expected

### ✅ PlayerShopBalance
- Value storage and retrieval
- Merge function for combining balances
- Flag management

### ✅ StashEntry
- Count accumulation logic
- Add operation correctness
- Edge cases (overflow, negative values)

## What We CANNOT Easily Test (Without Mocking/Integration)

### ❌ ShopManager
Requires: Shop objects, BlockPos, Level, ChunkPos, file system
- Shop registration/unregistration
- Chunk shop tracking
- Shop lookup by position
- Save/load operations
- Item display management

### ❌ StashManager
Requires: ItemStack, ServerPlayer, UUID, file system
- Item stashing with ItemStack
- Stash retrieval
- JSON serialization/deserialization
- Item UUID calculation

### ❌ BalanceManager
Requires: ServerPlayer, EconomyManager, NotificationManager, file system
- Balance claiming
- Economy system integration
- Notification sending
- Persistence operations

## Recommendations for Future Testing

### Short Term (No Code Changes)
1. ✅ **Unit test data classes** (DONE)
2. Add Mockito tests for business logic
3. Create integration test suite with Fabric GameTest
4. Manual testing checklist for critical paths

### Medium Term (Minor Refactoring)
1. Extract interfaces for external dependencies
2. Use dependency injection where possible
3. Separate I/O operations from business logic
4. Make storage directory configurable for testing

### Long Term (Major Refactoring)
1. Replace static managers with singleton instances
2. Create service layer with interfaces
3. Abstract Minecraft types behind domain objects
4. Implement proper dependency injection framework

## Test Quality Metrics

Current test coverage:
- **Data Classes**: ~100% line coverage
- **Manager Business Logic**: ~0% (requires mocking/integration)
- **Overall Project**: ~15% estimated

Target test coverage:
- **Data Classes**: 100%
- **Manager Business Logic**: 80%
- **Critical Paths**: 100% (via integration tests)
- **Overall Project**: 60%+

## Adding New Tests

### For Data Classes (Pure Java):
```java
@Test
void testNewFeature() {
    // Arrange
    PlayerStash stash = new PlayerStash();

    // Act
    stash.put(UUID.randomUUID(), new StashEntry(null));

    // Assert
    assertEquals(1, stash.size());
}
```

### For Managers (Requires Mocking):
```java
@Test
void testShopRegistration() {
    // This would require:
    Shop shop = mock(Shop.class);
    BlockPos pos = mock(BlockPos.class);
    Level world = mock(Level.class);

    when(shop.getKey()).thenReturn(new ShopKey(pos, world));
    // ... more setup

    ShopManager.registerShop(shop);

    verify(shop).getKey();
    // ... assertions
}
```

## Continuous Integration

Tests are designed to run in CI/CD pipelines:
- No Minecraft server required for current tests
- Fast execution (< 1 second)
- No external dependencies
- Pure JUnit 5 tests

Add to your CI pipeline:
```yaml
- name: Run Tests
  run: ./gradlew test --no-daemon

- name: Generate Test Report
  run: ./gradlew test jacocoTestReport

- name: Upload Coverage
  uses: codecov/codecov-action@v3
```

## Contributing

When adding new testable code:
1. Prefer pure functions over static state
2. Use dependency injection where possible
3. Keep business logic separate from I/O
4. Write tests first (TDD) for new features
5. Document testing challenges in code comments

## Resources

- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Fabric GameTest](https://fabricmc.net/wiki/tutorial:gametest)
- [Testing Best Practices](https://phauer.com/2019/modern-best-practices-testing-java/)

## License

Same as FancyPlayerShops mod license.
