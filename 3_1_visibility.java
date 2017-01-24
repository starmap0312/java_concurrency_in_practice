// Visibility
// Synchronization:
//   1) mutex, critical section, atomicity: synchronized blocks and methods
//      prevent one thread from modifying the state of an object when another is using it
//   2) memory visibility
//      ensure that when a thread modifies the state of an object, other threads can actually see the changes made
//
// visibility problem:
//   when you write a value to a variable in a thread and later read that variable in another
//   there is no guarantee that the reading thread will see a value written by the thread on a timely basis
//
// (bad exmaple: Sharing variables without synchronization)
public class NoVisibility {
    private static boolean ready;             // default: false
    private static int number;                // default: 0

    private static class ReaderThread extends Thread {
        public void run() {
            while (!ready) {                  // when ready == false, as we expect ReaderThread to block if number not yet written (continues forever)
                Thread.yield();
            }
            System.out.println(number);       // we expect that number == 42 will be printed
        }
    }

    public static void main(String[] args) {
        new ReaderThread().start();          // start a reader thread
        number = 42;                         // the main thread writes to the shared variable
        ready = true;                        // the main thread sets ready == true, so that reader thread can print out the written number 
    }
}
// problems:
// 1) reader thread could loop forever because the value of ready might never become visible to the reader thread
// 2) reader thread could print zero because the write to ready might be made visible to reader thread before the write to number, i.e. reordering 
//    when main thread writes first to number and then to ready without synchronization, reader thread could see those writes happen in the opposite order

// JVM reordering:
// JVMs can take full advantage of the performance of modern multiprocessor hardware
// Java Memory Model permits:
// 1) compiler to reorder operations and cache values in registers
// 2) CPUs to reorder operations and cache values in processor-specific caches

// Rule of thumb
//   always use the proper synchronization whenever data is shared across threads

// Stale Data: i.e. out-of-date value
//   can cause serious and confusing failures such as unexpected exceptions, corrupted data structures, inaccurate computations, and infinite loops
//
// (bad example:  Non-thread-safe Mutable Integer Holder)
@NotThreadSafe
public class MutableInteger {
    private int value;

    public int get() { return value; }
    public void set(int value) { this.value = value; }
}

// (good example: Thread-safe Mutable Integer Holder)
@ThreadSafe
public class SyncrhonizedInteger {
    @GuardedBy("this") private int value;

    public synchronized int get() { return value; }
    public synchronized void set(int value) { this.value = value; }
}
// note: synchronizing only the setter would not be sufficient: threads calling get would still be able to see stale values

// Rule:
//   requiring all threads to synchronize on the same lock when accessing a shared mutable variable
//   to guarantee that values written by one thread are made visible to other threads (otherwise, it might see a stale value)

// Volatile Variables
// 1) an alternative, weaker form of synchronization
// 2) ensure that updates to a variable are propagated predictably to other threads
// 3) tell compiler that this variable is shared and that operations on it should not be reordered with other memory operations
//    i.e. volatile variables are not cached in registers or in caches where they are hidden from other processors (threads), so
//         a read of a volatile variable always returns the most recent write by any thread

// synchronized getter & setter vs. volatile variables
//   accessing a volatile variable performs no locking and so cannot cause the executing thread to block
//     declared as volatile prevents the reordering problem
//   volatile variables is a lighter-weight synchronization mechanism than synchronized

// Rule of thumb
// 1) use volatile variables only when they simplify implementing and verifying your synchronization policy
// 2) good uses of volatile variables include:
//    ensuring the visibility of their own state or
//    indicating that an important lifecycle event (such as initialization or shutdown) has occurred
//    ex. indicating a completion, interruption, or used as a status flag

// example: a typical use of volatile variables, i.e. checking a status flag to determine when to exit a loop
volatile boolean asleep;

while (!asleep) {
    countSomeSheep();
}
// without volatile, the thread might not notice when asleep has been set by another thread
//
// limitations of volatile variables
//    the semantics of volatile are not strong enough to make the increment operation (count++) atomic
//    whereas, atomic variables do provide atomic read-modify-write support and can often be used as better volatile variables

// use volatile variables only when all the following criteria are met
// 1) writes to the variable do not depend on its current value, or you can ensure that only a single thread ever updates the value
// 2) the variable does not participate in invariants with other state variables
// 3) locking is not required for any other reason while the variable is being accessed

// volatile variables vs. AtomicInteger
// 1) volatile: approximately that each individual read or write operation on that variable is atomic
// 2) provide a wider variety of operations atomically, specifically including increment, i.e. i++

