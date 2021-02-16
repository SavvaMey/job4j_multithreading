package thread;

public final class Cache {
    private static Cache cache;

    public synchronized static Cache instOf() {
        if (cache == null) {
            cache = new Cache();
        }
        return cache;
    }

    public static void main(String[] args) {
        Thread threadOne = new Thread(Cache::instOf);
        Thread threadTwo = new Thread(Cache::instOf);
        threadOne.start();
        threadTwo.start();
    }
}

