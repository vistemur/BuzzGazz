package BuzzGazz;

import java.io.IOException;

abstract class MutexedThread extends Thread {
    Object mutex;

    public MutexedThread(Object mutex) {
        this.mutex = mutex;
    }

    public void run() {
        synchronized (mutex) {
            make();
            mutex.notify();
        }
    }

    protected abstract void make();
}