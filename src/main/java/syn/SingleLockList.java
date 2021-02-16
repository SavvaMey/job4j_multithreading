package syn;

import java.lang.reflect.Array;
import java.util.Iterator;

public class SingleLockList <T> implements Iterable<T> {
    private SimpleArray<T> array = new SimpleArray<>(10);

    public synchronized void add(T value) {
        array.add(value);
    }

    public synchronized T get(int index) {
        return array.get(index);
    }

    public synchronized SimpleArray<T> copy(SimpleArray<T> array) {
        SimpleArray<T> arrayTmp = new SimpleArray<>();
        array.iterator().forEachRemaining(arrayTmp::add);
        return arrayTmp;
    }

    @Override
    public synchronized Iterator<T> iterator() {
        return copy(this.array).iterator();
    }
}
