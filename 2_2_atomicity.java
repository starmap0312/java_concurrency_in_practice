// Atomicity
//
// example: add a long field to the servlet and increment it on each request 
//          Servlet that Counts Requests without Synchronization 

@NotThreadSafe
public class UnsafeCountingFactorizer implements Servlet {
    private long count = 0;                 // a mutable state

    public long getCount() { return count; }

    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        ++count;                            // not atomic operation 
        encodeIntoResponse(resp, factors);
    }
}
// i.e. Read-modify-write operation
//   it works just fine in a single-threaded environment
//   but ++count is not atomic operation, i.e. fetch value, add one, and write value back
//   two threads may increment the object's count field at the same time
// solution: atomic operation


@ThreadSafe
public class CountingFactorizer implements Servlet {
    private final AtomicLong count = new AtomicLong(0); // java.util.concurrent.atomic package contains atomic variable class

    public long getCount() { return count.get(); }

    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        count.incrementAndGet();            // atomic operation: thread-safe
        encodeIntoResponse(resp, factors);
    }
}
// solution: atomic operation


// Race Conditions
//   race condition occurs when correctness of a computation depends on relative timing or interleaving of multiple threads at runtime
//   it does not always result in failure (i.e. some unlucky timing is required)
//   but it could cause serious problems:
//     ex. registrations are lost or multiple activities have inconsistent views of the set of registered objects
//     ex. two distinct objects could end up with the same ID
// examples:
// 1) check-then-act or Read-modify-write operations
//    i.e. compound actions: sequences of operations that must be executed atomically in order to remain thread-safe
//    check-then-act: a potentially stale observation is used to make a decision on what to do next
//                    however, the state of the system may have changed when you act
//    solution: locking
//
// 2) Lazy Initialization:
//    lazy initialization: defer initializing an object until it is actually needed
//    solution: locking

@NotThreadSafe
public class LazyInitRace {
    private ExpensiveObject instance = null;

    public ExpensiveObject getInstance() {
        if (instance == null) {
            instance = new ExpensiveObject();
        }
        return instance;
    }
}
// ex. threads A and B execute getInstance at the same time
//     then both see that instance is null and instantiate a new ExpensiveObject
// solution: locking


