Wait and notify methods

1.wait and notify methods are inherited from object class

2.wait is used to put current threat to wait and release intrinsic lock

3.notify is used to notify outside threads that is about to release intrinsic lock

Note: wait and notify must be called inside synchronized block

Important note:

wait()=> sets thread to waiting and hands over the intrinsic lock

notify()=> notify other threads,doesn't hand over intrinsic lock.(Nothing to do with the intrinsic
lock hand over)