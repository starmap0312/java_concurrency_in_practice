// Guarding State with Locks
// 1) locks enable serialized access to the code paths they guard (threads take turns accessing object exclusively)
//    can use them to construct protocols for guaranteeing exclusive access to shared state
// 2) atomic compound action: holding a lock for the entire duration of a compound action
//    compound actions on shared state must be made atomic to avoid race conditions
//    ex. incrementing a counter (read-modify-write), lazy initialization (check-then-act)
// 3) wrapping the compound action with a synchronized block is not sufficient
//    locking is not just about mutual exclusion: it is also about memory visibility
//    i.e. if a mutable state variable is accessed by multiple threads,
//         all accesses to that variable must be performed with the same lock held (i.e. variable is guarded by the lock)
//         otherwise, threads may not see the most up-to-date values
//         therefore, we should ensure that only one thread at a time can access that variable
//    i.e. acquiring a lock prevents other thread from acquiring that same lock (mutex block), not from accessing that object
//
// Rule of thumb
// 1) every shared, mutable variable should be guarded by exactly one lock
//    make it clear to maintainers which lock that is
// 2) for every invariant that involves more than one variable, all the variables involved in that invariant must be guarded by the same lock
//
// A common locking convention
// 1) encapsulate all mutable state within an object 
// 2) protect it from concurrent access by synchronizing any code path that accesses mutable state using the object's intrinsic lock
//    this is used by many thread-safe classes, ex. Vector and other synchronized collection classes
//
// synchronization is NOT the only cure for race conditions (i.e. declare every method synchronized)
// ex. put-if-absent operation (a compound action) has a race condition, even though both contains and add are atomic
if (!vector.contains(element)) {
    vector.add(element);
}
// synchronizing every method can lead to liveness or performance problems, ex. SynchronizedFactorizer
