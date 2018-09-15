import java.lang.Math;

public class utils{
    public static sum(double[] arr) {
        double temp=0;
        for (double c : arr) {
            temp+=c;
        }
        return temp;
    }

    public static clamp(double x){
        return Math.min(Math.max(x, -5), 5);
    }
}