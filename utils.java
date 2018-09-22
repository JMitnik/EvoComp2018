import java.lang.Math;

import sun.security.util.Length;

public class utils {
    public static double sum(double[] arr) {
        double temp = 0;
        for (double c : arr) {
            temp += c;
        }
        return temp;
    }

    public static double clamp(double x) {
        return Math.min(Math.max(x, -5), 5);
    }

    public static void swap(double[][] arr, int i1, int j1, int i2, int j2) {
        double temp = arr[i1][j1];
        arr[i1][j1] = arr[i2][j2];
        arr[i2][j2] = temp;
        return;
    }

    public static void swappingMix(double[][] arr, int i1, int j1, int i2, int j2, double mixRate) {
        assert mixRate >= 0 && mixRate <= 1;
        double temp = arr[i1][j1];
        arr[i1][j1] = mixRate * temp + (1 - mixRate) * arr[i2][j2];
        arr[i2][j2] = temp + arr[i2][j2] - arr[i1][j1];
        return;
    }

    public static void meanVector(double[][] arr, double[] mean) {
        int length = arr.length;
        assert length > 0;
        assert arr[0].length == mean.length;

        for (double[] a : arr) {
            addVector(a, mean, mean);
        }

//        TODO: Type Error, assertion is broken
//        mean /= length;
        return;
    }

    public static void addVector(double[] a, double[] b, double[] res) {
        assert a.length == b.length;
        for (int i = 0; i < a.length; i++) {
            res[i] = a[i] + b[i];
        }
        return;
    }
    // from a to b, wrap of System.copy
    public static void copyVector(double[] a, double[] b) {
        assert a.length== b.length;
        int l = a.length;
        System.arraycopy(a, 0, b, 0, l);
        return;
    }
}