package nonblockingcache;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class CacheTest {

    @Test
    public void whenUpdateThenOk() {
        Cache cash = new Cache();
        Base one = new Base(1, 0);
        one.setName("one");
        cash.add(one);
        Base two = new Base(1, 0);
        two.setName("oneOk");
        cash.update(two);
        assertThat(cash.get(1).getVersion(), is(1));
        assertThat(cash.get(1).getName(), is("oneOk"));
    }

    @Test (expected = OptimisticException.class)
    public void whenEx() {
        Cache cash = new Cache();
        Base one = new Base(1, 0);
        one.setName("one");
        cash.add(one);
        Base two = new Base(1, 1);
        two.setName("oneOk");
        cash.update(two);
    }

}