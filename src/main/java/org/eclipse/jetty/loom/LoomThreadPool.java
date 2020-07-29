package org.eclipse.jetty.loom;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.thread.ThreadPool;

public class LoomThreadPool extends AbstractLifeCycle implements ThreadPool
{
    private final ExecutorService executorService;

    public LoomThreadPool()
    {
        ThreadFactory factory = Thread.builder().virtual().name("LoomThreadPool-").factory();
        executorService = Executors.newThreadExecutor(factory);
    }

    @Override
    public void execute(Runnable command)
    {
        executorService.submit(command);
    }

    @Override
    public void join()
    {
        while (!executorService.isTerminated())
        {
            Thread.onSpinWait();
        }
    }

    @Override
    protected void doStop() throws Exception
    {
        super.doStop();
        executorService.shutdown();
    }

    @Override
    public int getThreads()
    {
        // TODO: always report a value?
        return Integer.MAX_VALUE;
    }

    @Override
    public int getIdleThreads()
    {
        // TODO: always report available?
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isLowOnThreads()
    {
        return false;
    }
}
