Reentrant Lock

1.Implementation of interface Lock

2.Methods lock(),unlock();

3.Alternative to synchronized keyword,I use methods lock() unlock() to lock/unlock intrinsic lock

Object            :     Condition
--------------------------------------
wait()            :       await();
notify()          :       signal();
notifyAll()       :       signalAll();
