package com.korancrew.net;

/*
 * Copyright 2001-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * @author James M Snell (jasnell@us.ibm.com)
 */
public class ThreadPool {

    public static final int DEFAULT_MAX_THREADS = 100;
    protected Map threads = new Hashtable();
    protected long threadcount;
    public boolean _shutdown;
    private int maxPoolSize = DEFAULT_MAX_THREADS;

    public ThreadPool() {
    }

    public ThreadPool(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void cleanup() throws InterruptedException {
        System.out.println("Enter: ThreadPool::cleanup");
        if (!isShutdown()) {
            safeShutdown();
            awaitShutdown();
        }
        synchronized (this) {
            threads.clear();
            _shutdown = false;
        }
        System.out.println("Exit: ThreadPool::cleanup");
    }

    /**
     * Returns true if all workers have been shutdown
     */
    public boolean isShutdown() {
        synchronized (this) {
            return _shutdown && threadcount == 0;
        }
    }

    /**
     * Returns true if all workers are in the process of shutting down
     */
    public boolean isShuttingDown() {
        synchronized (this) {
            return _shutdown;
        }
    }

    /**
     * Returns the total number of currently active workers
     */
    public long getWorkerCount() {
        synchronized (this) {
            return threadcount;
        }
    }

    /**
     * Adds a new worker to the pool
     */
    public void addWorker(Runnable worker) {
        System.out.println("Enter: ThreadPool::addWorker");
        if (_shutdown || threadcount == maxPoolSize) {
            throw new IllegalStateException("ThreadPool is shutdown or already full.");
        }
        Thread thread = new Thread(worker);
        threads.put(worker, thread);
        threadcount++;
        thread.start();
        System.out.println("Exit: ThreadPool::addWorker");
    }

    /**
     * Forcefully interrupt all workers
     */
    public void interruptAll() {
        System.out.println("Enter: ThreadPool::interruptAll");
        synchronized (threads) {
            for (Iterator i = threads.values().iterator(); i.hasNext();) {
                Thread t = (Thread) i.next();
                t.interrupt();
            }
        }
        System.out.println("Exit: ThreadPool::interruptAll");
    }

    /**
     * Forcefully shutdown the pool
     */
    public void shutdown() {
        System.out.println("Enter: ThreadPool::shutdown");
        synchronized (this) {
            _shutdown = true;
        }
        interruptAll();
        System.out.println("Exit: ThreadPool::shutdown");
    }

    /**
     * Forcefully shutdown the pool
     */
    public void safeShutdown() {
        System.out.println("Enter: ThreadPool::safeShutdown");
        synchronized (this) {
            _shutdown = true;
        }
        System.out.println("Exit: ThreadPool::safeShutdown");
    }

    /**
     * Await shutdown of the worker
     */
    public synchronized void awaitShutdown() throws InterruptedException {
        System.out.println("Enter: ThreadPool::awaitShutdown");
        if (!_shutdown) {
            throw new IllegalStateException("Not shutting down.");
        }
        while (threadcount > 0) {
            wait();
        }
        System.out.println("Exit: ThreadPool::awaitShutdown");
    }

    /**
     * Await shutdown of the worker
     */
    public synchronized boolean awaitShutdown(long timeout) throws InterruptedException {
        System.out.println("Enter: ThreadPool::awaitShutdown");
        if (!_shutdown) {
            throw new IllegalStateException("Not shutting down.");
        }
        if (threadcount == 0) {
            System.out.println("Exit: ThreadPool::awaitShutdown");
            return true;
        }
        long waittime = timeout;
        if (waittime <= 0) {
            System.out.println("Exit: ThreadPool::awaitShutdown");
            return false;
        }
        for (;;) {
            wait(waittime);
            if (threadcount == 0) {
                System.out.println("Exit: ThreadPool::awaitShutdown");
                return true;
            }
            waittime = timeout - System.currentTimeMillis();
            if (waittime <= 0) {
                System.out.println("Exit: ThreadPool::awaitShutdown");
                return false;
            }
        }
    }

    /**
     * Used by MessageWorkers to notify the pool that it is done
     */
    public void workerDone(
            Runnable worker,
            boolean restart) {
        System.out.println("Enter: ThreadPool::workerDone");
        synchronized (this) {
            threads.remove(worker);
            if (--threadcount == 0 && _shutdown) {
                notifyAll();
            }
            if (!_shutdown && restart) {
                addWorker(worker);
            }
        }
        System.out.println("Exit: ThreadPool::workerDone");
    }
}
