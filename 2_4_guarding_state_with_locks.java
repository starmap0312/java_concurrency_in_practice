// Guarding State with Locks
// 1) locks enable serialized access to the code paths they guard (threads take turns accessing object exclusively)
//    so we can use them to construct protocols for guaranteeing exclusive access to shared state
// 2) holding a lock for the entire duration of a compound action
//    compound actions on shared state must be made atomic to avoid race conditions
//    ex. incrementing a counter (read-modify-write), lazy initialization (check-then-act)
// 3) for each mutable state variable that may be accessed by more than one thread, all accesses to that variable must be
//    performed with the same lock held (i.e. the variable is guarded by that lock)
// 4) note that wrapping the compound action with a synchronized block is not sufficient
//    it is needed everywhere that variable is accessed (read/write)
//    i.e. locking is not just about mutual exclusion: it is also about memory visibility
//    I)  acquiring the lock associated with an object does not prevent other threads from accessing that object
//        acquiring a lock prevents other thread from acquiring that same lock (mutex block)
//    II) so all threads may not the most up-to-date values of shared mutable variables
//    soluiton:
//      to ensure that all threads see the most up-to-date values of shared mutable variables, reading and writing threads
//      must synchronize on a common lock
