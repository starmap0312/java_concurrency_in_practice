// What is Thread Safety
// the core idea is about correctness: 
// 1) a class conforms to its specification
//    its specification defines invariants constraining an object's state
//    its postconditions describe the effects of its operations
// 2) the code will not break in a concurrent environment (just like in a single-threaded environment)
//
// a class is thread-safe if:
// 1) it behaves correctly when accessed from multiple threads, regardless of the interleaving of thread execution
// 2) it needs no additional synchronization or coordination on the calling code
//    i.e. thread-safe classes should encapsulate any needed synchronization so that clients need not provide their own
// 3) no operations performed sequentially (concurrently) on a thread-safe class instances will not cause it be in an invalid state
//
// Stateless objects are always thread-safe
//
// example: Stateless Servlet
//
// a servlet-based factorization service
// unpacks the number to be factored from the servlet request, factors it, and packages the results into the servlet response

@ThreadSafe
public class StatelessFactorizer implements Servlet {
    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        encodeIntoResponse(resp, factors);
    }
}
// the servlet is stateless: no shared state (field or references) between threads
// one thread's accessing of StatelessFactorizer cannot influence the result of another thread's accessing
