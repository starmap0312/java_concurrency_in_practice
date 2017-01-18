// What is Thread Safety
// i.e. correctness: 
// 1) a class conforms to its specification
//    specification defines invariants constraining an object's state
//    postconditions describing the effects of its operations
// 2) the code works, i.e. code confidence: we know it when we see it
// 3) no broken in a concurrent environment and in a single-threaded environment
//
// a class is thread-safe if
// 1) it behaves correctly when accessed from multiple threads, regardless of interleaving of threads execution
// 2) it has no additional synchronization or other coordination on the calling code
//    i.e. thread-safe classes encapsulate any needed synchronization so that clients need not provide their own
// 3) no operations performed sequentially or concurrently on a thread-safe class instances can cause it be in an invalid state
