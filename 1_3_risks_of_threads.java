// Risks of Threads
// 1) Safety Hazards
//    when data is shared across threads, simultaneous access of the data accross threads could cause problem
//
//    ex. two threads could call getNext() and receive the same value

@NotThreadSafe
public class UnsafeSequence {
    private int value;

    public int getNext() {
        return value++;
    }
}
// issue: two threads to read the value at the same time, both see the same value, and then both add one to it
//        then the same value is returned different threads
//
//   A: value = 9              -> 9 + 1          -> value = 10
//   B:              value = 9          -> 9 + 1               -> value = 10

@ThreadSafe
public class UnsafeSequence {
    private int value;

    public syncrhonized int getNext() {
        return value++;
    }
}
// solution: make getNext a synchronized method

// 2) Liveness Hazards
//    liveness failure occurs when an activity gets into a state such that it is permanently unable to make forward progress
//
//    ex. thread A is waiting for a resource that thread B holds exclusively, and B never releases it, A will wait forever
//    ex. deadlock, starvation, and livelock 
//
// 3) Performance Hazards
//    liveness: something good eventually happens
//    performance: something good happens quickly
//
//    ex. problems of poor service time, responsiveness, throughput, resource consumption, or scalability
//
//    threads runtime overhead:
//    1) context switches
//       i.e. scheduler suspends the active thread temporarily so another thread can run) 
//       overhead: saving and restoring execution context, loss of locality, and CPU time spent scheduling threads
//    2) when threads share data, they must use synchronization mechanisms that can inhibit compiler optimizations, 
//       flush or invalidate memory caches, and create synchronization traffic on the shared memory bus

