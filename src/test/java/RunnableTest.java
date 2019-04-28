import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RunnableTest {

    @Test
    public void runnableOneThread() {
        Runnable task = () -> {
            String name = Thread.currentThread().getName();
            System.out.println("Thread name: " + name);
        };

        task.run();

        Thread thread = new Thread(task);
        thread.setName("My-Thread");
        thread.start();

        System.out.println("End of declaration block");

        Assertions.assertEquals("My-Thread", thread.getName());
    }
}
