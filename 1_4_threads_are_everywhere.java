// Threads are Everywhere
// the following JAVA frameworks create threads on your behalf
//   they make calls (callbacks) from the framework threads to your application components
//   i.e. your application component are called from threads not managed by the application
// 1) when the JVM starts, it creates threads for:
//    JVM housekeeping tasks (garbage collection, finalization) and a main thread for running the main method
// 2) AWT (Abstract Window Toolkit) and Swing user interface frameworks create threads for managing user interface events
//    GUI applications are inherently asynchronous
//    Swing:
//      achieve their thread safety by confining all access to GUI components to the event thread
//      if application wants to manipulate GUI from outside the event thread, the code that manipulates GUI must run in event thread
//      the event handler needs to access application state in a thread-safe manner
// 3) Timer creates threads for executing deferred tasks
//    usage: a convenience mechanism for scheduling tasks to run at a later time (once/periodically)
//    thread-safety: ensure that shared objects accessed by TimerTask are themselves thread-safe
// 4) Component frameworks, such as servlets and RMI create pools of threads and invoke component methods in these threads
//    I) servlets
//    usage: handle all the infrastructure of deploying a web application and dispatching requests from remote HTTP clients 
//           requests are dispatched to the appropriate servlet or JSP (each servlet represents a component of application logic)
//    thread-safety: ensure that a servlet can be called simultaneously from multiple threads
//                  servlets often access state information shared with other servlets
//                  ex. application-scoped objects (stored in ServletContext) or session-scoped objects (stored in per-client HttpSession)
//    II) RMI
//    usage: invoke methods on objects running in another JVM
//           the method arguments are packaged (marshaled) into a byte stream and shipped over the network to the remote JVM
//           then they are unpacked (unmarshaled) and passed to the remote method
//    thread-safety: the thread that calls remote method is not the thread you created 
//                  remote object must guard against two thread safety hazards
//                  coordinating access to state shared with other objects and access to the state of the remote object itself 
