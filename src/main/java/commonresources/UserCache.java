package commonresources;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UserCache {
    private final ConcurrentHashMap<Integer, User> users = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger();

    public void add(User user) {
        users.put(id.incrementAndGet(), User.of(user.getName()));
    }

    public User findById(int id) {
        return User.of(users.get(id).getName());
    }

    public List<User> findAll() {
        return users.values().stream()
                .map(value -> User.of(value.getName()))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws InterruptedException {
        UserCache cache = new UserCache();
        User user = User.of("name");
        User userOne = User.of("nameOne");
        cache.add(userOne);
        cache.add(user);
        List<User> users = cache.findAll();
        System.out.println(users.get(0) == user);
        System.out.println(users.get(1) == user);
    }
}


//@NotThreadSafe
//public class UserCache {
//    private final ConcurrentHashMap<Integer, User> users = new ConcurrentHashMap<>();
//    private final AtomicInteger id = new AtomicInteger();
//
//    public void add(User user) {
//        users.put(id.incrementAndGet(), user);
//    }
//
//    public User findById(int id) {
//        return users.get(id);
//    }
//}
