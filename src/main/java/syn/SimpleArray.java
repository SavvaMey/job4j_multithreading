package syn;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class SimpleArray <T> implements Iterable<T> {
    private T[] container;
    private int size = 0;
    private int pos = 0;
    private int modCount = 0;

    public SimpleArray(int size) {
        this.container = (T[]) new Object[size];
    }

    public SimpleArray() {
        this.container = (T[]) new Object[10];
    }

    public T get(int index) {
        Objects.checkIndex(index, pos);
        return container[index];
    }

    public void expand() {
        size *= 2;
        T[] subContainer = (T[]) new Object[size];
        System.arraycopy(container, 0, subContainer, 0, pos - 1);
        container = subContainer;
    }

    public void add(T model) {
        modCount++;
        if (pos == container.length) {
            expand();
        }
        container[pos++] = model;
    }

    public boolean checkUnique(T model) {
        for (int i = 0; i < container.length; i++) {
            if (Objects.equals(container[i], model)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int cursor = 0;
            private int expectedModCount = modCount;

            @Override
            public boolean hasNext() {
                return cursor < pos;
            }

            @Override
            public T next() {
                if (expectedModCount != modCount) {
                    throw new ConcurrentModificationException();
                }
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return container[cursor++];
            }
        };
    }
}
