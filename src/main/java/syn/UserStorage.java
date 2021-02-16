package syn;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;

@ThreadSafe
public class UserStorage {

    @GuardedBy("this")
    private final Map<Integer, User> storage = new HashMap<>();

    public synchronized boolean add(User user) {
        return storage.putIfAbsent(user.getId(), user) != null;
    }

    public synchronized boolean update(User user) {
        return storage.replace(user.getId(), user) != null;
    }

    public synchronized boolean delete(User user) {
        return storage.remove(user.getId(), user);
    }

    public synchronized void transfer(int fromId, int toId, int amount) {
        if (!storage.containsKey(fromId) || !storage.containsKey(toId)) {
            System.out.println("неправильный id");
            return;
        }
        User userOne = storage.get(fromId);
        User userTwo = storage.get(toId);
        if (userOne.getAmount() < userTwo.getAmount()) {
            System.out.println("недостаточно средств");
            return;
        }
        userOne.setAmount(userOne.getAmount() - amount);
        userTwo.setAmount(userTwo.getAmount() + amount);
//        this.update(userOne);
//        this.update(userTwo);
    }
}
