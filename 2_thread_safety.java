// Thread Safety
//   writing thread-safe code is about managing access to an object's shared, mutable state
//     an object's state is its data, ex. instance or static fields
//     shared: accessed by multiple threads
//     mutable: could change during its lifetime
//   tread-safety: protect shared data from uncontrolled concurrent access of multiple threads
//     i.e. coordinate their access to it using synchronization
// thread-safety solution:
//   1) don't share the state variable across threads             (avoid shared state)
//   2) make the state variable immutable                         (avoid mutable state)
//   3) use synchronization whenever accessing the state variable (use lock/transaction if shared mutable, state is needed)
//   design well-organized, maintainable classes, ex. encapsulation and data hiding, can help create thread-safe classes

