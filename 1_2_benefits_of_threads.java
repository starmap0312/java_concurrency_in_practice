// Benefits of Threads
//   threads are useful in GUI applications for improving the responsiveness of the user interface, and
//     in server applications for improving resource utilization and throughput
//   the basic unit of scheduling is the thread, a program with only one thread can run on at most one processor at a time
// 1) Exploiting Multiple Processors
//    when properly designed, multithreaded programs can improve throughput by utilizing available processor resources more effectively
//    ex. utilize all available CPU resources on a 100-processor system
//    ex. a multithreaded program can make progress during the slow, blocking I/O
// 2) Simplicity of Modeling
//    a program that processes one type of task sequentially is simpler to write
//    a complicated, asynchronous workflow can be decomposed into a number of simpler, synchronous workflows each running in a separate thread,
//      interacting only with each other at specific synchronization points
//    ex. RMI (Remote Method Invocation) includes tasks such as thread creation, load balancing, and dispatching requests, etc.
// 3) Simplified Handling of Asynchronous Events
//    ex. a server application accepts socket connections from multiple remote clients
//          each connection is allocated its own thread and uses synchronous I/O
//        if each request has its own thread, then blocking does not affect the processing of other requests
// 4) More Responsive User Interfaces
//    long-running task is instead executed in a separate thread, the event thread remains free to process UI events, making the UI more responsive
//    we don't need to frequently poll throughout the code for input events if using multi-threading
