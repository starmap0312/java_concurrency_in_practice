// Immutability
//   immutable objects are always thread-safe
//
// An object is immutable if:
// 1) its state cannot be modifled after construction
// 2) all its flelds are final
// 3) it is properly constructed (the this reference does not escape during construction)
//
// immutable objects can still represent mutable entities or use mutable objects internally
//
// rule of thumb:
// 1) make all fields private unless they need greater visibility
// 2) make all fields final unless they need to be mutable
//
// example: Immutable Class Built Out of Mutable Underlying Objects
@Immutable
public final class ThreeStooges {
    private final Set<String> stooges = new HashSet<String>();

    public ThreeStooges() {
        stooges.add("Moe");
        stooges.add("Larry");
        stooges.add("Curly");
    }

    public boolean isStooge(String name) {
        return stooges.contains(name);
    }
}

// Final Fields
//
// final keyword: a more limited version of the const mechanism from C++

// ex1. final local variables
//      final local variables can be assigned, once, later in Java
public class Foo {

    void bar() {
        final int a;
        a = 10;   // Legal 
        //a = 11; // Not legal
    }
}

// ex2. final class fields 
//      final class fields need to be given a value by the time its instance is constructed
public class Foo {
    private final int a;
    private final int b = 11;

    public Foo() {
        a = 10;
    }
}

// ex3. final class methods
//      final class methods cannot be overriden
public class Bar {

    public final void foo() {

    }
}

public class Error extends Bar {

    public void foo() { // Error in java, can't override
        
    }
} 

// example: Immutable Holder for Caching a Number and its Factors
@Immutable
public class OneValueCache {
    private final BigInteger lastNumber;
    private final BigInteger[] lastFactors;

    public OneValueCache(BigInteger, i, BigInteger[] factors) { 
        lastNumber = i;
        lastFactors = Arrays.copyOf(factors, factors.length);
    }

    public BigInteger[] getFactors(BigInteger i) {
        if (lastNumber == null || !lastNumber.equals(i)) {
            return null;
        } else {
            return Arrays.copyOf(lastFactors, lastFactors.length);
        }
    }
}
// why is it good?
//   once a thread acquires a reference to an immutable object, it need never worry about another thread modifying its state

// a volatile object can use a OneValueCache (immutable holder object) to store the cached number and factors
// 1) when a thread sets the volatile cache field to reference a new OneValueCache, the new cached data becomes immediately visible to other threads
// 2) volatile reference is used to ensure its timely visibility, allows the volatile object to be thread-safe even though it does no explicit locking

