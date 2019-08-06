import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

class ExecutorsTest {

    @Test
    void simpleSingleExecutorTest() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Current Thread: " + threadName);
        });
    }

    @Test
    void simpleCallableTest() throws ExecutionException, InterruptedException {
        Callable<Long> task = () -> {
            System.out.println("Callable will return UNIX time in Long");
            return Instant.now().getLong(ChronoField.INSTANT_SECONDS);
        };

        ExecutorService executor = Executors.newFixedThreadPool(3);
        Future<Long> future = executor.submit(task);

        System.out.println("future done? " + future.isDone());

        Long result = future.get();

        System.out.println("future done? " + future.isDone());
        Assertions.assertTrue(future.isDone());
        Assertions.assertNotNull(result);
        System.out.print("result: " + result);
    }

    @Test
    void invokeAllTest() throws InterruptedException {
        ExecutorService executor = Executors.newWorkStealingPool();

        List<Callable<String>> listCallable = Arrays.asList(
                () -> "task 1",
                () -> "task 2",
                () -> "task 3");

        executor.invokeAll(listCallable)
                .stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .forEach(System.out::println);
    }

    @Test
    void scheduledExecutorTest() throws InterruptedException {

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable task = () -> System.out.println("Scheduling: " + System.nanoTime());
        ScheduledFuture<?> future = executor.schedule(task, 5, TimeUnit.SECONDS);

        TimeUnit.MILLISECONDS.sleep(1337);

        long remainingDelay = future.getDelay(TimeUnit.MILLISECONDS);
        System.out.printf("Remaining Delay: %sms", remainingDelay);
    }


}
