package prog03;
import prog02.UserInterface;
import prog02.GUI;

/**
 *
 * @author vjm
 */
public class Main {
  /** Use this variable to store the result of each call to fib. */
  public static double fibn;

  /** Determine the time in microseconds it takes to calculate the
   n'th Fibonacci number.
   @param fib an object that implements the Fib interface
   @param n the index of the Fibonacci number to calculate
   @return the time for the call to fib(n)
   */
  public static double time (Fib fib, int n) {
    // Get the current time in nanoseconds.
    long start = System.nanoTime();

    // Calculate the n'th Fibonacci number.  Store the
    // result in fibn.
    fibn = fib.fib(n);

    // Get the current time in nanoseconds.
    long end = System.nanoTime();
    double runTime = (end - start) / 1000.0; // fix this

    // Return the difference between the end time and the
    // start time divided by 1000.0 to convert to microseconds.
    return runTime; // fix this
  }

  /** Determine the average time in microseconds it takes to calculate
   the n'th Fibonacci number.
   @param fib an object that implements the Fib interface
   @param n the index of the Fibonacci number to calculate
   @param ncalls the number of calls to average over
   @return the average time per call
   */
  public static double averageTime (Fib fib, int n, int ncalls) {
    // Get the current time in nanoseconds.
    long start = System.nanoTime();

    // Calculate the n'th Fibonacci number.  Store the
    // result in fibn.
    for (int i = 0; i < ncalls; i++) {
      fibn = fib.fib(n);
    }
    // Get the current time in nanoseconds.
    long end = System.nanoTime();
    long averageRunTime = (end - start) / ncalls; // fix this

    // Return the difference between the end time and the
    // start time divided by 1000.0 to convert to microseconds.
    return (averageRunTime / 1000.0);

//    // Copy the contents of Main.time here.
//
//    // Add a "for loop" line before the line with the call to fib.fib
//    // to make that line run ncalls times.
//
//    // Modify the return value.
//
//    return 0; // remove this line
  }

  /** Determine the time in microseconds it takes to to calculate the
   n'th Fibonacci number.  Average over enough calls for a total
   time of at least one second.
   @param fib an object that implements the Fib interface
   @param n the index of the Fibonacci number to calculate
   @return the time it it takes to compute the n'th Fibonacci number
   */
  public static double accurateTime (Fib fib, int n) {
    // Get the time in microseconds for one call.
    double t = time(fib, n);

    // If the time is (equivalent to) more than a second, return it.
    if (t >= 1000000) {
      return t;
    }


    // Estimate the number of calls that would add up to one second.
    // Use   (int)(YOUR EXPESSION)   so you can save it into an int variable.
    int numcalls = (int)(1000000 / t); // fix this


    // Get the average time using averageTime above and that many
    // calls and return it.
    return averageTime(fib, n, numcalls);
  }

 // private static UserInterface ui = new TestUI("Fibonacci experiments");
  private static UserInterface ui = new GUI("Fibonacci experiments");

  /** Get a non-negative integer from the using using ui.
   If the user enters a negative integer, like -2, say
   "-2 is negative...invalid"
   If the user enters a non-integer, like abc, say
   "abc is not an integer"
   If the user clicks cancel, return -1.
   @return the non-negative integer entered by the user or -1 for cancel.
   */
  static int getInteger () {
    String s = ui.getInfo("Enter n");
    try {
      if (s == null) {
        return -1;
      }
      int integerCandidate = Integer.parseInt(s);

      if (integerCandidate < 0){
        ui.sendMessage(integerCandidate + " is negative. Retry.");
        return -2;
      }

      return integerCandidate;

    } catch (NumberFormatException nfe) {
      ui.sendMessage("This is not an integer");
      return -2;
    }

//    String s = ui.getInfo("Enter n");
//    if (s == null)
//      return -1;
//    return Integer.parseInt(s);
  }

  public static void doExperiments (Fib fib) {
    System.out.println("doExperiments " + fib);

    boolean firstLoop = true;

    while(true) {
      int integerReceived = getInteger();
      switch (integerReceived) {
        case -1:
          return;
        case -2:
          break;
        default:
          double estimatedTime = fib.estimateTime(integerReceived);

          if (firstLoop){
            ui.sendMessage("Fib (" + integerReceived + ") is " + fib.fib(integerReceived) + " in " + accurateTime(fib, integerReceived) + " microseconds.");
          }

          if (estimatedTime > 3600000000.0) {
            ui.sendMessage("This operation will take more than an hour (" + estimatedTime + " microseconds). Are you sure you wish to proceed?");
            String[] options = {"Yes", "No"};
            int d = ui.getCommand(options);
            if(d == 1){
              continue;
            }
          }

          double actualTimeThisTook = accurateTime(fib, integerReceived);
          fib.recordConstant(integerReceived, actualTimeThisTook);
          if (!firstLoop) {
            ui.sendMessage("Estimated time on input " + integerReceived + " is " + estimatedTime + " microseconds.");
            ui.sendMessage("Fib("+ integerReceived +") is " + fib.fib(integerReceived) + ". It actually took " + actualTimeThisTook + " microseconds. The percentage error is " + ((estimatedTime - actualTimeThisTook) / actualTimeThisTook) * 100.0 + "%" );
            break;
          }
      }
      firstLoop = false;
    }
  }

  public static void doExperiments () {
    // Give the user a choice instead, in a loop, with the option to exit.
    //doExperiments(new ExponentialFib());

      String[] options = {"ExponentialFib","LinearFib","LogFib", "ConstantFib","MysteryFib", "Exit"};
      while(true) {
        int d = ui.getCommand(options);
        switch (d) {
          case -1:
          case 5:
            return;
          case 0:
            doExperiments(new ExponentialFib());
            break;
          case 1:
            doExperiments(new LinearFib());
            break;
          case 2:
            doExperiments(new LogFib());
            break;
          case 3:
            doExperiments(new ConstantFib());
            break;
          case 4:
            doExperiments(new MysteryFib());
            break;
        }
      }
  }

  static void labExperiments () {
    // Create (Exponential time) Fib object and test it.
//    Fib efib = new ExponentialFib();
//    Fib efib = new LinearFib();
//    Fib efib = new LogFib();
//    Fib efib = new ConstantFib();
      Fib efib = new MysteryFib();

    System.out.println(efib);
    for (int i = 0; i < 11; i++)
      System.out.println(i + " " + efib.fib(i));

    // Determine running time for n1 = 20 and print it out.
    int n1 = 20;
    double time1 = accurateTime(efib, n1);
    System.out.println("n1 " + n1 + " time1 " + time1 + " microseconds");

    // Calculate constant:  time = constant times O(n).
    double c = time1 / efib.O(n1);
    System.out.println("c " + c);

    // Estimate running time for n2=30.
    int n2 = 30;
    double time2est = c * efib.O(n2);
    System.out.println("n2 " + n2 + " estimated time " + time2est + " microseconds");

    // Calculate actual running time for n2=30.
    double time2 = accurateTime(efib, n2);
    System.out.println("n2 " + n2 + " actual time " + time2 + " microseconds");

    // Estimate how long ExponentialFib.fib(100) would take.
    int n3 = 100;
    double time3est = c * efib.O(n3);
    double years = time3est * (1 / 1000000.0) * (1 / 60.0) * (1 / 60.0) * (1 / 24.0) * (1 / 365.25);
    System.out.println("n3 " + n3 + " estimated time " + years + " years");
    System.out.println("n2 " + n3 + " estimated time " + time3est + " microseconds");
  }

  /**
   * @param args the command line arguments
   */
  public static void main (String[] args) {
    labExperiments();
    doExperiments();
  }
}