// Thread Confinement
//   accessing shared, mutable data requires using synchronization
//   if data is only accessed from a single thread, no synchronization is needed
//
// example 1: Swing
//   Swing visual components and data model objects are not thread safe
//   safety is achieved by confining them to the Swing event dispatch thread
//   to use Swing properly, code running in threads other than the event thread should not access these objects
//     i.e. Swing provides the invokeLater mechanism to schedule a Runnable for execution in the event thread
//
// example 2: JDBC (Java Database Connectivity) Connection objects
//   a thread acquires a connection from the pool, uses it for processing a single request, and returns it
//   most requests, ex. servlet requests or EJB (Enterprise JavaBeans) calls, are processed synchronously by a single thread
//   the pool will not dispense the same connection to another thread until it has been returned
//   i.e. this connection management confines the Connection to a specific thread for the duration of the request
//
// thread confinement is an element of your program's design that must be enforced by its implementation
//   no means of confining an object to a thread (just like no means of enforcing a variable to be guarded by a lock)
//
// 1) Ad-hoc Thread Confinement
//    when the responsibility for maintaining thread confinement falls entirely on the implementation
//    ex. volatile variable is only written from a single thread, which includes:
//        confining the modification to a single thread to prevent race conditions
//        ensure that other threads see the most up-to-date value
//    try to use stack confinement or ThreadLocal first
// 2) Stack Confinement
//    when an object can only be reached through local variables, also called within-thread or thread-local usage
//    simpler to maintain and less fragile than ad-hoc thread confinement
// 3) ThreadLocal
//    associate a per-thread value with a value-holding object
//    used to prevent sharing in designs based on mutable Singletons or global variables
//    ex. each thread has its own JDBC connection
//    ex. frequent operation requires temporary object, ex. buffer, and don't want to reallocate the temporary object on each invocation


// example: Thread Confinement of Local Primitive and Reference Variables (Stack Confinement)
public int loadTheArk(Collection<Animal> candidates) {
    SortedSet<Animal> animals;
    int numPairs = 0;
    Animal candidate = null;
    // animals confined to method, don't let them escape
    animals = new TreeSet<Animal>(new SpeciesGenderComparator());
    animals.addAll(candidates);
    for (Animal a : animals) {
        if (candidate == null || !candidate.isPotentialMate(a)) {
            candidate = a;
        } else {
            ark.load(new AnimalPair(candidate, a));
            ++numPairs;
            candidate = null;
        }
    }
    return numPairs;
}
// there is exactly one reference, i.e. animals, to the TreeSet, held in a local variable and therefore confined to the executing thread
// don't let animals escape

// ThreadLocal
//   Thread-Local provides getter and setter methods that maintain a separate copy of the value for each thread that uses it
//   so a get method should return the most recent value passed to set method from the "currently executing thread"

// example: Using ThreadLocal to Ensure thread Confinement
private static ThreadLocal<Connection> connectionHolder
    = new ThreadLocal<Connection>() {
        public Connection initialValue() {
            return DriverManager.getConnection(DB_URL);
        }
        // when a thread calls ThreadLocal.get for the first time, initialValue is to provide the initial value for that thread
    };

public static Connection getConnection() {
    return connectionHolder.get();
}
// each thread will have its own connection

// Conceptually, ThreadLocal<T> holds a Map<Thread,T> that stores the thread-specific values
//   thread-specific values are stored in the Thread object itself
//   when the thread terminates, thread-specific values can be garbage collected
