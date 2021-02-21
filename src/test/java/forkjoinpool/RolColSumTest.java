package forkjoinpool;

import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static forkjoinpool.RolColSum.sum;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class RolColSumTest {
    private final int[][] matrix =
            {
                    {5, 7, 3, 17},
                    {7, 0, 1, 12},
                    {8, 1, 2, 3},
                    {10, 0, 2, 1},
            };

    @Test
    public void sumSync() {
        assertThat(RolColSum.sum(matrix)[0].getColSum(), is(30));
        assertThat(RolColSum.sum(matrix)[1].getRowSum(), is(20));
    }

    @Test
    public void sumAsync() throws ExecutionException, InterruptedException {
        assertThat(RolColSum.asyncSum(matrix)[0].getColSum(), is(30));
        assertThat(RolColSum.asyncSum(matrix)[1].getRowSum(), is(20));
    }
}