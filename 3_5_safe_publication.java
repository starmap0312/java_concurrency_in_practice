// Safe Publication
//   sometimes we want to share objects across threads
//
// (bad example: Caching the Last Result Using a Volatile Reference to an Immutable Holder Object)
@ThreadSafe
public class VolatileCachedFactorizer implements Servlet {
    private volatile OneValueCache cache = new OneValueCache(null, null);

    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = cache.getFactors(i);
        if (factors = null) {
            factors = factor(i);
            cache = new OneValueCache(i, factors);
        }
        encodeIntoResponse(resp, factors);
    }
}

// Unsafe publication: Publishing an Object without Adequate Synchronization
public Holder holder;

public void initialize() {
    holder = new Holder(42);
}
// why it fails?
//   Because of visibility problems, the Holder could appear to another thread to be in an inconsistent state,
//     even though its invariants were properly established by its constructor
//   This improper publication could allow another thread to observe a partially constructed object
