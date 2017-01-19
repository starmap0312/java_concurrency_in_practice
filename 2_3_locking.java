// Locking
// 
// improve performance of by caching
//   AtomicLong: a thread-safe holder class for a long integer
//   AtomicReference: a thread-safe holder class for an object reference

// (bad example: inadequate atomicity)
@NotThreadSafe
public class UnsafeCachingFactorizer implements Servlet {
    private final AtomicReference<BigInteger> lastNumber = new AtomicReference<BigInteger>();
    private final AtomicReference<BigInteger[]> lastFactors = new AtomicReference<BigInteger[]>();

    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        if (i.equals(lastNumber.get()) {
            encodeIntoResponse(resp, lastFactors.get());
        } else {
            BigInteger[] factors = factor(i);
            lastNumber.set(i);
            lastFactors.set(factors);
            encodeIntoResponse(resp, factors);
        }
    }
}
// even though the atomic references are individually thread-safe
// but there are still race conditions that could make it produce the wrong answer

// 1) Intrinsic Locks (monitor locks)
//    Java provides a built-in locking mechanism for enforcing atomicity: synchronized block
//    I)  lock is automatically acquired by thread before entering a synchronized block
//    II) lock is automatically released when control exits the synchronized block
//    intrinsic locks act as mutexes (mutual exclusion locks): at most one thread may own the lock
//      when thread A attempts to acquire a lock held by thread B, A must wait, or block, until B releases it
//      if B never releases the lock, A waits forever
//
// (bad example: Poor Concurrency)
@ThreadSafe
public class UnsafeCachingFactorizer implements Servlet {
    @GuardedBy("this") private BigInteger lastNumber;    // guarded by servlet object's intrinsic lock (documented by @GuardedBy)
    @GuardedBy("this") private BigInteger[] lastFactors; // guarded by servlet object's intrinsic lock (documented by @GuardedBy)

    public synchronized void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        if (i.equals(lastNumber.get()) {
            encodeIntoResponse(resp, lastFactors);
        } else {
            BigInteger[] factors = factor(i);
            lastNumber = i;
            lastFactors = factors;
            encodeIntoResponse(resp, factors);
        }
    }
}
// thread-safe, synchronized makes only one thread enters service at a time
// poor responsiveness: it inhibits multiple clients from using the factoring servlet simultaneously at all

// 2) Reentrancy (re-entered)
//    locks are acquired on a per-thread rather than per-invocation basis
//      note: pthreads (POSIX threads) mutexes are per-invocation basis
//    it is implemented by associating with each lock an acquisition count and an owning thread
//      therefore, when a thread requests a lock that is already held by another thread, the requesting thread blocks
//    this simplifies the development of object-oriented concurrent code

// (bad example: if Intrinsic Locks were Not Reentrant)
public class Widget {
    public synchronized void doSomething() {
        //...
    }
}

public class LoggingWidget extends Widget {
    public synchronized void doSomething() {
        System.out.println(toString() + ": calling doSomething";
        super.doSomething();
    }
}
// this will cause deadlock if synchronized is NOT reentrant
//   you cannot acquire the lock held by yourself: lock already held by LoggingWidget, so call to superclass Widget has to wait
