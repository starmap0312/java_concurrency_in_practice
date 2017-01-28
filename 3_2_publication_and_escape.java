// Publication and Escape
// object escape problem in concurrent programming
//   once an object escapes, another class or thread may, maliciously or carelessly, misuse it
//   this could cause serious problem, ex. security, so use encapsulation as much as possible
// 1) publication of an object
//    pusblishing an object: make an object available to code outside of its current scope
//    ex. by storing a reference to it where other code can find it (this couples different parts of code that use it)
//        by returning it from a nonprivate method: so this may indirectly publish other objects
//        by passing it to a method in another class (this couples callee and caller)
//    in many situations, ensure that objects and their internals are not published (encapsulation)
//    in some situations, we want to publish an object for general use (but in a thread-safe manner)
// 2) escape of an object
//    an object that is published when it should not have been is said to have escaped
//
// (example: publishing an object by storing a reference in a public static field)
public static Set<Secret> knownSecrets;

public void initialize() {
    knownSecrets = new HashSet<Secret>();
}

// publishing one object may indirectly publish others
//   ex. returning a reference from a nonprivate method also publishes the returned object
//   ex. adding an element to a published/nonprivate iterable aslo publishes the element
//   ex. publishing an object also publishes any objects referred to by its nonprivate fields
//   ex. passing an object to an alien method of a class may also publish that object, since you can't know what its code does 
// i.e. any object reachable from a published object by following some chain of nonprivate field references and method calls are also published

// (bad example: Allowing Internal Mutable State to Escape)
class UnsafeStates {
    private String[] states = new String[] { "AK", "AL" };
    public String[] getStates() { return states; }
}
// why is it bad?
// states array has escaped its intended scope, it is intended to be private, but has been made public through the public getter method
// so any caller can modify its states/contents

// (bad example: Implicitly Allowing the this Reference to Escape)
public class ThisEscape {
    public ThisEscape(EventSource source) {
        source.registerListener(
            new EventListener() {
                public void onEvent(Event e) {
                    doSomething(e);
                }
            }
        );
    }
}
// why is it bad?
// when ThisEscape publishes the EventListener, it implicitly publishes the enclosing ThisEscape instance as well
//   because inner class instances contain a hidden reference to the enclosing instance
//
// Safe Construction Practices
//   do not allow the this reference to escape during construction
//   there's nothing wrong with creating a thread in a constructor, but it is best not to start the thread immediately
//   avoid the improper construction by using a private constructor and a public factory method

// example: using a Factory Method to Prevent the this Reference from Escaping During Construction
public class SafeListener {
    private final EventListener listerner;

    private SafeListener() {
        listener = new EventListener() {        // creating a thread in a constructor, but do not to start the thread immediately
            public void onEvent(Event e) {
                doSomething(e);
            }
        };
    }

    public static SafeListener newInstance(EventSource source) {
        SafeListener safe = new SafeListener();
        source.registerListener(safe.listener); // implicitly publishes the SafeListener instance (start the tread here)
        return safe;
    }
}
