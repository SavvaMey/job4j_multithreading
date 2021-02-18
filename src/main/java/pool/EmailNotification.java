package pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EmailNotification {
    ExecutorService pool = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );

    public void emailTo(User user) {
        String subject = String.format("Notification %s to email %s", user.getUsername(),
                user.getEmail());
        String body = String.format("Add a new event to %s", user.getUsername());
        send(subject, body, user.getEmail());
        pool.submit(new Runnable() {
            @Override
            public void run() {
                send(subject, body, user.getEmail());
            }
        });
    }

    public void send(String subject, String body, String email) {
    }

    public void close() throws InterruptedException {
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);
    }

    public static void main(String[] args) throws InterruptedException {
        EmailNotification notification = new EmailNotification();
        User user = new User("sav", "i@email.com");
        notification.emailTo(user);
        notification.close();
    }
}
