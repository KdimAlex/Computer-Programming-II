package prog03;

public class ConstantFib extends PowerFib{
    /** The order O() of the implementation.
     @param n index
     @return the function of n inside the O()
     */
    public double O (int n) {
        return Math.log(n);
    }

    /** Raise x to the n'th power
     @param x x
     @param n n
     @return x to the n'th power
     */
    protected double pow (double x, int n) {
        double constantRunTime = Math.pow(x , n);
       return constantRunTime;
    }
}
