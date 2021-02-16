package commonresources;

public class DCLSingleton {
    private static volatile DCLSingleton inst;

    public static DCLSingleton instOf() {
        if (inst == null) {
            synchronized (DCLSingleton.class) {
                if (inst == null) {
                    inst = new DCLSingleton();
                }
            }
        }
        return inst;
    }

    private DCLSingleton() {
    }

    public static void main(String[] args) {
        Thread threadOne = new Thread(DCLSingleton::instOf);
        Thread threadTwo = new Thread(DCLSingleton::instOf);
        threadOne.start();
        threadTwo.start();
    }
}
