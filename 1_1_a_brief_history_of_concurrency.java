// motivating factors of concurrency
// 1) Resource utilization:
//    programs have to wait for external operations, ex. input or output
//    concurrency makes programs do other useful work while waiting
// 2) Fairness:
//    multiple programs may have equal claims on the machine's resources 
//    concurrency enables the sharing of the computer via finer-grained time slicing
// 3) Convenience:
//    some concurrent tasks require several programs performing a single task and coordinating with each other
//    concurrency makes the coding of these concurrent tasks more easily
