// Visibility
// Synchronization:
//   1) mutex, critical section, atomicity: synchronized blocks and methods
//      prevent one thread from modifying the state of an object when another is using it
//   2) memory visibility
//      ensure that when a thread modifies the state of an object, other threads can actually see the changes made
