package me.ningsk.common.utils;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ThreadUtil
{
    public static void join(Thread thread)
    {
        try
        {
            thread.join();
        } catch (InterruptedException e) {
            Assert.fail();
        }
    }

    public static boolean join(Thread thread, int millisec) {
        try {
            thread.join(millisec);
        } catch (InterruptedException e) {
            Assert.fail();
        }
        return !thread.isAlive();
    }

    public static void wait(Object object)
    {
        try {
            object.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void wait(Object object, long millis) {
        try {
            object.wait(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static ExecutionException waitForCompletion(Future<?> task) {
        try {
            task.get();
        } catch (InterruptedException e) {
            Assert.fail();
        } catch (ExecutionException e) {
            return e;
        }
        return null;
    }

    public static <T> T get(Future<T> task) {
        try {
            return task.get();
        } catch (InterruptedException e) {
            return Assert.fail(e);
        } catch (ExecutionException e) {
            return Assert.fail(e);
        }
    }

    public static <T> T take(BlockingQueue<T> queue) {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            return Assert.fail(e);
        }
    }

    public static <T> T exchange(Exchanger<T> exchanger, T object) {
        try {
            return exchanger.exchange(object);
        } catch (InterruptedException e) {
            return Assert.fail(e);
        }
    }

    public static <T> T exchange(Exchanger<T> exchanger) {
        return exchange(exchanger, null);
    }
}