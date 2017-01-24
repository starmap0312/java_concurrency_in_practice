// Liveness and Performance
// deciding how big or small of synchronized blocks:
//   tradeoffs among safety, simplicity, and performance
// rule of thumbs:
// 1) when implementing a synchronization policy, resist the temptation to prematurely sacriflce simplicity for the sake of performance
//    safety cannot be compromised for performance
//    simplicity: synchronizing the entire method
//    performance: concurrency, synchronizing the shortest possible code paths
//      ex. break down synchronized blocks: factoring ++hits into its own synchronized block
//      ex. CachedFactorizer holds the lock when accessing state variables for the duration of compound actions
//          but releases it before executing the potentially long-running factorization operation
// 2) avoid holding locks during lengthy computations or operations (ex. network or console I/O)
//
// example: Servlet that Caches its Last Request and Result.

// (bad example: Poor Concurrency)
@ThreadSafe
public class UnsafeCachingFactorizer implements Servlet {
    @GuardedBy("this") private BigInteger lastNumber;    // guarded by servlet object's intrinsic lock (documented by @GuardedBy)
    @GuardedBy("this") private BigInteger[] lastFactors; // guarded by servlet object's intrinsic lock (documented by @GuardedBy)

    public synchronized void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        if (i.equals(lastNumber.get()) {                 // if the request number equals the cached lastNumber
            encodeIntoResponse(resp, lastFactors);       // then return the cached lastFactors
        } else {
            BigInteger[] factors = factor(i);
            lastNumber = i;
            lastFactors = factors;
            encodeIntoResponse(resp, factors);
        }
    }
}
// cocurrent executions of three such tasks: A, B, C (poor concurrency)
// ex.
//   A -> factoring
//   B              -> factoring
//   C                           -> factoring

// (good example: CachedFactorizer holds the lock when accessing state variables for the duration of compound actions
//                but releases it before executing the potentially long-running factorization operation)
@ThreadSafe
public class CachedFactorizer implements Servlet {
    @GuardedBy("this") private BigInteger lastNumber;    // guarded by servlet object's intrinsic lock (documented by @GuardedBy)
    @GuardedBy("this") private BigInteger[] lastFactors; // guarded by servlet object's intrinsic lock (documented by @GuardedBy)
    @GuardedBy("this") private long hits;                // guarded by servlet object's intrinsic lock (documented by @GuardedBy)
    @GuardedBy("this") private long cachHits;            // guarded by servlet object's intrinsic lock (documented by @GuardedBy)

    public synchronized long getHits()  {
        return hits;
    }
    public synchronized long getCachHitRatio() {i
        return (double) cacheHits / (double) hits;
    }

    public synchronized void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = null;
        synchronized (this)  {                           // this check-then-act compund action on lastNumber & lastFactors needs to
            ++hits;                                      //   be in a mutex block
            if (i.equals(lastNumber)) {
                ++cacheHits;
                factors = lastFactors.clone();
            }
        }
        if (factors == null) {
            factors = factor(i);                         // factoring (CPU-intensive) does not need to be in a mutex block
            synchronized (this)  {                       // access to lastNumber & lastFactors needs to be in a mutex block
                lastNumber = i;
                lastFactors = factors.clone();
            }
        }
        encodeIntoResponse(resp, factors);
    }
}
// better concurrency:
//   when one thread is doing factoring, other threads are not blocked and may return the cached factors
// the above restructuring provides a balance between:
// 1) simplicity: synchronizing the entire method
// 2) concurrency: synchronizing the shortest possible code paths
