# Academic Report: Diagnostic and Remediation of Users Package and Users Test Suite

**Date:** April 13, 2026  
**Project:** Coffee Shop Backend - CSC8019  
**Author:** GitHub Copilot  
**Subject:** Users Package Integration Testing and Defect Resolution

---

## 1. Abstract

This report documents the debugging and stabilization work performed on the Users domain and its integration tests in the backend project. The work covered repository-layer defects, application context boot failures, security configuration dependencies, asynchronous test behavior, and validation through Maven Surefire outputs. The final state achieved a passing Users test suite with all Customer and Staff controller tests executing successfully.

---

## 2. Scope

The investigation and fixes focused on:

### 2.1 Users Package Production Code
- `Backend/src/main/java/org/coffeeshop/users/repositories/StaffRepository.java`
- `Backend/src/main/java/org/coffeeshop/users/services/CustomerService.java`

### 2.2 Security and JWT Configuration
- `Backend/src/main/java/org/coffeeshop/security/JwtTokenService.java`
- `Backend/src/main/java/org/coffeeshop/security/SecurityConfig.java`
- `Backend/src/main/resources/application.properties`

### 2.3 Users Test Code
- `Backend/src/test/java/org/coffeeshop/UserTests/CustomerTests/CustomerControllerTests.java`
- `Backend/src/test/java/org/coffeeshop/UserTests/StaffTests/StaffControllerTests.java`

---

## 3. Initial Failure Profile

The test execution process revealed four distinct problem classes:

### 3.1 Environment Execution Issues
- PowerShell could not locate Maven on system PATH
- Required full Maven path and corrected shell command syntax

### 3.2 Spring Context Bootstrap Failure
- Application context initialization failed during test startup
- Root cause: Unresolved JWT secret placeholder
- Error: `java.lang.IllegalArgumentException: Could not resolve placeholder 'jwt.secret' in value "${jwt.secret}"`

### 3.3 Repository Method Signature Defect
- Spring Data JPA query derivation failed for `StaffRepository.existsByUsername`
- Invalid return type caused bean creation failure
- Error: `Failed to create query for method public abstract java.util.function.BooleanSupplier org.coffeeshop.users.repositories.StaffRepository.existsByUsername(java.lang.String); No property 'asBoolean' found for type 'Staff'`

### 3.4 Behavioral Test Failures in Customer Controller Tests
- After context startup repair, Customer tests returned HTTP 404 where 200 was expected
- Affected operations: get by ID, update, delete
- Root cause: Asynchronous service method execution with test data visibility mismatch

---

## 4. Problem Analysis and Root Causes

### 4.1 JWT Property Bootstrap Dependency

**Root Cause:**  
`JwtTokenService` requires `jwt.secret` via property injection and validates it during bean construction.

**Evidence:**
- File: `Backend/src/main/java/org/coffeeshop/security/JwtTokenService.java` (lines 22-31)
- Constructor injection: `@Value("${jwt.secret}") String secret`
- Validation method enforces minimum 32-byte requirement

**Impact:**  
Entire Spring application context fails to initialize if `jwt.secret` is not provided, preventing both test and production execution.

**Resolution Applied:**  
Added JWT secret property to `Backend/src/main/resources/application.properties`:
```properties
jwt.secret=your-secret-key-for-testing-purposes-only
```

### 4.2 Staff Repository Signature Defect

**Root Cause:**  
`existsByUsername` method was typed as `BooleanSupplier` (functional interface) instead of primitive `boolean`. Spring Data JPA's query derivation mechanism cannot interpret functional types as JPA query return types.

**Evidence:**
- File: `Backend/src/main/java/org/coffeeshop/users/repositories/StaffRepository.java`
- Original: `BooleanSupplier existsByUsername(String string);`
- Corrected: `boolean existsByUsername(String string);`

**Impact:**  
JPA repository bean creation failed during component scanning, cascading into:
- `StaffService` constructor dependency injection failure
- `StaffController` bean instantiation failure
- Test context initialization failure

**Resolution Applied:**  
Corrected method signature from functional type to primitive boolean type. This allows Spring Data JPA's derived query mechanism to correctly generate the query.

### 4.3 Customer 404 Failures After Context Startup

**Observed Symptom:**  
Customer integration tests for "get by ID", "update", and "delete" operations expected HTTP 200 status but received 404, indicating resource not found.

**Root Cause Analysis:**  
Test data visibility mismatch between test setup execution and asynchronous service method execution:

1. Test setup saves customer entity within test thread's transaction context
2. Service method marked with `@Async` executes in thread pool thread
3. Different transaction contexts resulted in uncommitted data not visible to async execution
4. Repository queries in async thread returned empty results

**Technical Context - Asynchronous Service Methods:**

Customer service implements asynchronous operations:
- `findCustomerById(Long id)`: Line 90, decorated with `@Async`
- `updateCustomer(Long id, CustomerDto dto)`: Line 109, decorated with `@Async`
- `deleteCustomer(Long id)`: Line 135, decorated with `@Async`

Each method returns `CompletableFuture<T>`, allowing non-blocking execution in REST controller:

```java
@GetMapping("/{id}")
public CompletableFuture<ResponseEntity<CustomerDto>> getCustomerById(@PathVariable("id") Long id) {
    return customerService.findCustomerById(id).thenApply(ResponseEntity::ok);
}
```

**Initial Mitigation Attempt (Rejected):**  
Adding `@Transactional` annotations at service method level was considered to enforce transaction visibility across threads. However, this approach was rejected because:
- Introduces semantic coupling between production and test concerns
- Unnecessary in production (async handlers operate on already-committed data from REST clients)
- Violates separation of concerns principle

**Final Resolution - Test Layer Adjustment:**

Modified test class to ensure data visibility for asynchronous execution without affecting production semantics:

1. **Removed class-level transactional wrapping:**
   - Deleted: `@Transactional` annotation from test class
   - Impact: Each test manages its own transaction lifecycle

2. **Added explicit flush and persistence context clear:**
   - Pattern applied in all test data setup: `customerRepository.flush()` followed by `entityManager.clear()`
   - Effect: Forces immediate database write and clears Hibernate session cache
   - Result: Async methods in different threads can see committed data

**Evidence of Test-Level Visibility Controls:**

In `CustomerControllerTests`:
```java
Customer savedCustomer = customerRepository.save(new Customer(...));
customerRepository.flush();        // Force immediate database write
entityManager.clear();             // Clear persistence context
```

Applied to all four test methods:
- `getAllCustomers_returnsList()` (lines 86-87)
- `getCustomerById_returnsCustomer()` (lines 102-103)
- `updateCustomer_returnsUpdatedCustomer()` (lines 127-128)
- `deleteCustomer_returnsStringMessage()` (lines 169-170)

---

## 5. Security and Test Environment Clarification

### 5.1 JWT Configuration Role Clarification

During analysis, two distinct JWT roles were identified:

1. **Bootstrap/Context Initialization (Required):**
   - JWT secret is required for Spring context startup
   - `JwtTokenService` bean cannot be constructed without valid `jwt.secret`
   - Affects both test and production environments equally

2. **Request Authentication (Not Required in Current Tests):**
   - Bearer token authentication is not enforced in current tests
   - Security policy configured with `permitAll()` for all requests

Evidence from `SecurityConfig`:
```java
.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
```

**Implication:**  
JWT secret is a startup configuration dependency, not a request-authentication requirement for these specific test endpoints under current security policy.

---

## 6. Validation Outcomes

### 6.1 Final Test Execution Summary

**Command Executed:**
```
mvn clean test
```

**Duration:** 15.341 seconds total

### 6.2 Surefire Test Results

**Customer Controller Tests:**
- File: `Backend/target/surefire-reports/org.coffeeshop.UserTests.CustomerTests.CustomerControllerTests.txt`
- Tests run: 5
- Failures: 0
- Errors: 0
- Skipped: 0
- Time elapsed: 8.862 seconds
- Status: **PASSED ✓**

**Staff Controller Tests:**
- File: `Backend/target/surefire-reports/org.coffeeshop.UserTests.StaffTests.StaffControllerTests.txt`
- Tests run: 6
- Failures: 0
- Errors: 0
- Skipped: 0
- Time elapsed: 0.457 seconds
- Status: **PASSED ✓**

**Combined Users Test Suite:**
- Total tests run: 11
- Total failures: 0
- Total errors: 0
- Total skipped: 0
- **Overall Status: BUILD SUCCESS ✓**

---

## 7. Final Change Set Summary

### 7.1 Production Code Changes

**File 1: StaffRepository.java**
```java
// Changed from:
BooleanSupplier existsByUsername(String string);

// Changed to:
boolean existsByUsername(String string);
```
**Rationale:** Enable Spring Data JPA query derivation for repository method.

### 7.2 Configuration Changes

**File 2: application.properties**
```properties
# Added line:
jwt.secret=your-secret-key-for-testing-purposes-only
```
**Rationale:** Provide required JWT secret for Spring context initialization.

### 7.3 Test Code Changes

**File 3: CustomerControllerTests.java**

**Removed:**
- Class-level `@Transactional` annotation
- Import: `jakarta.transaction.Transactional`

**Added:**
- Import: `jakarta.persistence.EntityManager`
- Field: `private EntityManager entityManager;`
- Flush/clear pattern in each test method after data setup

**Example Pattern:**
```java
Customer savedCustomer = customerRepository.save(new Customer(...));
customerRepository.flush();    // Force write to database
entityManager.clear();         // Clear session cache
```

---

## 8. Discussion

### 8.1 Design Considerations

The solution prioritizes **production-side semantic clarity** by:
1. Keeping service methods free of test-specific annotations
2. Isolating transaction management concerns to test setup
3. Using standard JPA EntityManager APIs (not framework-specific) for visibility control

### 8.2 Data Lifecycle in Test Environment

With H2 in-memory database:
- Test data persists until JVM shutdown or repository.deleteAll() call
- Each `@BeforeEach` cleans test data before test execution
- No persistent side effects between test runs
- In-memory database is ephemeral and discarded after test suite completion

### 8.3 Production Implications

Service layer changes are **production-safe** because:
1. No `@Transactional` annotations added to service methods
2. Async execution operates on already-committed REST request data
3. Repository method signature correction aligns with Spring Data JPA specifications
4. JWT secret property requirement is standard Spring Security practice

---

## 9. Conclusions

The debugging effort successfully resolved a multi-layer failure chain:

1. **Missing security configuration** (jwt.secret) prevented context initialization
2. **Invalid repository method signature** (BooleanSupplier) broke bean creation
3. **Asynchronous test data visibility** mismatch caused false 404 failures

The final approach correctly:
- Isolated test-layer data visibility fixes to the test code
- Preserved production semantic clarity and non-transactional service behavior
- Applied standard Spring Data JPA and JPA patterns
- Achieved 100% test pass rate (11/11 tests)

**The Users package and Users test suite are now functionally stable under the current security policy and H2 in-memory test configuration.**

---

## Appendix A: Files Modified

| File | Change Type | Description |
|------|-------------|-------------|
| `Backend/src/main/java/org/coffeeshop/users/repositories/StaffRepository.java` | Code Fix | Corrected method signature: `BooleanSupplier` → `boolean` |
| `Backend/src/main/resources/application.properties` | Configuration | Added `jwt.secret` property |
| `Backend/src/test/java/org/coffeeshop/UserTests/CustomerTests/CustomerControllerTests.java` | Test Refactor | Removed `@Transactional`, added `flush()` and `entityManager.clear()` patterns |

---

**End of Report**