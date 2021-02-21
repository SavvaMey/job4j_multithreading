package forkjoinpool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RolColSum {
    public static class Sums {
        private int rowSum;
        private int colSum;

        public Sums(int rowSum, int colSum) {
            this.rowSum = rowSum;
            this.colSum = colSum;
        }

        public int getRowSum() {
            return rowSum;
        }

        public int getColSum() {
            return colSum;
        }
    }

    /**
     * @param matrix - square int matrix
     * @return - matrix with Sums(rowSum, ColSum)
     */
    public static Sums[] sum(int[][] matrix) {
        Sums[] result = new Sums[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            int rowSum = 0;
            int colSum = 0;

            for (int j = 0; j < matrix.length; j++) {
                rowSum += matrix[i][j];
                colSum += matrix[j][i];
            }
            result[i] = new Sums(rowSum, colSum);
        }
        return result;
    }

    public static Sums[] asyncSum(int[][] matrix) throws ExecutionException, InterruptedException {
        Sums[] sums = new Sums[matrix.length];
        Map<Integer, CompletableFuture<Sums>> futures = new HashMap<>();
        for (int i = 0; i < sums.length; i++) {
            futures.put(i, getTask(matrix, i));
        }
        for (Integer key : futures.keySet()) {
            sums[key] = futures.get(key).get();
        }
        return sums;
    }

    private static CompletableFuture<Sums> getTask(int[][] data, int index) {
        return CompletableFuture.supplyAsync(() -> {
            int sumRow = 0;
            int sumCol = 0;
            for (int i = 0; i < data.length; i++) {
                sumRow += data[index][i];
                sumCol += data[i][index];
            }
            return new Sums(sumRow, sumCol);
        });
    }

}
