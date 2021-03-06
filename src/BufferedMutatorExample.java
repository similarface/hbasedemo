import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.BufferedMutator;
import org.apache.hadoop.hbase.client.BufferedMutatorParams;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * An example of using the {@link BufferedMutator} interface.
 */
public class BufferedMutatorExample extends Configured implements Tool {

    private static final Log LOG = LogFactory.getLog(BufferedMutatorExample.class);

    private static final int POOL_SIZE = 10;
    private static final int TASK_COUNT = 100;
    private static final TableName TABLE = TableName.valueOf("testtable");
    private static final byte[] FAMILY = Bytes.toBytes("colfam1");

    @Override
    public int run(String[] args) throws InterruptedException, ExecutionException, TimeoutException {

        /** a callback invoked when an asynchronous write fails. */
        //创建一个用户监听
        final BufferedMutator.ExceptionListener listener = new BufferedMutator.ExceptionListener() {
            @Override
            public void onException(RetriesExhaustedWithDetailsException e, BufferedMutator mutator) {
                for (int i = 0; i < e.getNumExceptions(); i++) {
                    LOG.info("Failed to sent put " + e.getRow(i) + ".");
                }
            }
        };
        BufferedMutatorParams params = new BufferedMutatorParams(TABLE)
                .listener(listener);

        //
        // step 1: create a single Connection and a BufferedMutator, shared by all worker threads.
        //
        try (final Connection conn = ConnectionFactory.createConnection(HBaseConfiguration.create());
             final BufferedMutator mutator = conn.getBufferedMutator(params)) {

            /** worker pool that operates on BufferedTable instances */
            final ExecutorService workerPool = Executors.newFixedThreadPool(POOL_SIZE);
            List<Future<Void>> futures = new ArrayList<>(TASK_COUNT);

            for (int i = 0; i < TASK_COUNT; i++) {
                futures.add(workerPool.submit(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        //
                        // step 2: each worker sends edits to the shared BufferedMutator instance. They all use
                        // the same backing buffer, call-back "listener", and RPC executor pool.
                        //
                        Put p = new Put(Bytes.toBytes("row7"));
                        p.addColumn(FAMILY, Bytes.toBytes("qual1"), Bytes.toBytes("some value"));
                        mutator.mutate(p);
                        // do work... maybe you want to call mutator.flush() after many edits to ensure any of
                        // this worker's edits are sent before exiting the Callable
                        return null;
                    }
                }));
            }

            //
            // step 3: clean up the worker pool, shut down.
            //
            for (Future<Void> f : futures) {
                f.get(5, TimeUnit.MINUTES);
            }
            workerPool.shutdown();
        } catch (IOException e) {
            // exception while creating/destroying Connection or BufferedMutator
            LOG.info("exception while creating/destroying Connection or BufferedMutator", e);
        } // BufferedMutator.close() ensures all work is flushed. Could be the custom listener is
        // invoked from here.
        return 0;
    }

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new BufferedMutatorExample(), args);
    }
}