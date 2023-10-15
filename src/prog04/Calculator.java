package prog04;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import prog02.UserInterface;
import prog02.GUI;
import prog02.ConsoleUI;

public class Calculator {
  static final String OPERATORS = "()+-*/u^";
  static final int[] PRECEDENCE = { -1, -1, 1, 1, 2, 2, 3, 4 };
  Stack<Character> operatorStack = new Stack<Character>();
  Stack<Double> numberStack = new Stack<Double>();
  UserInterface ui = new GUI("Calculator");
  static boolean previousTokenWasNumberOrRightParenthesis = false;

  Calculator (UserInterface ui) { this.ui = ui; }

  void emptyStacks () {
    while (!numberStack.empty())
      numberStack.pop();
    while (!operatorStack.empty())
      operatorStack.pop();
  }

  String numberStackToString () {
    String s = "numberStack: ";
    Stack<Double> helperStack = new Stack<Double>();

    while (!numberStack.empty()){
      helperStack.push(numberStack.pop());
    }

    while (!helperStack.empty()){
      s = s + " " + numberStack.push(helperStack.pop());
    }
    // EXERCISE
    // Put every element of numberStack into helperStack
    // You will need to use a loop.  What kind?
    // What condition? When can you stop moving elements out of numberStack?
    // What method do you use to take an element out of numberStack?
    // What method do you use to put that element into helperStack?



    // Now put everything back, but also add each one to s:
    // s = s + " " + number;



    return s;
  }

  String operatorStackToString () {
    String s = "operatorStack: ";
    Stack<Character> helperStack = new Stack<Character>();
    // EXERCISE
    while (!operatorStack.empty()){
      helperStack.push(operatorStack.pop());
    }

    while (!helperStack.empty()){
      s = s + " " + operatorStack.push(helperStack.pop());
    }
    return s;
  }

  void displayStacks () {
    ui.sendMessage(numberStackToString() + "\n" +
                   operatorStackToString());
  }

  void doNumber (double x) {
    previousTokenWasNumberOrRightParenthesis = true;
//    if (operatorStack.peek() == 'u'){
//      numberStack.push(evaluateOperator(0,operatorStack.pop(),x));
//      displayStacks();
//      return;
//    }
    numberStack.push(x);
    displayStacks();
  }

  void doOperator (char op) {
    if (op == '-' && previousTokenWasNumberOrRightParenthesis == false){
      processOperator('u');
    } else {
      previousTokenWasNumberOrRightParenthesis = false;
      processOperator(op);
    }
    displayStacks();
  }

  double doEquals () {
    while (!operatorStack.empty())
      evaluateTopOperator();

    return numberStack.pop();
  }
    
  double evaluateOperator (double a, char op, double b) {
    switch (op) {
    case '+':
      return a + b;
    case '-':
      return a - b;
    case '*':
      return a * b;
    case '/':
      return a / b;
    case '^':
      return Math.pow(a, b);
    case 'u':
      return -b;
      // EXERCISE
    }
    System.out.println("Unknown operator " + op);
    return 0;
  }

  void evaluateTopOperator () {
    if (operatorStack.peek() == 'u'){
      numberStack.push(evaluateOperator(0,operatorStack.pop(),numberStack.pop()));
      displayStacks();
      return;
    }
    char op = operatorStack.pop();
    double b = numberStack.pop();
    double a = numberStack.pop();

    numberStack.push(evaluateOperator(a,op,b));
    // EXERCISE
    displayStacks();
  }

  private int precedence(char op){
    int operatorPrecedence = PRECEDENCE[OPERATORS.indexOf(op)];
    return operatorPrecedence;
  }

  void processOperator (char op) {
//    if(operatorStack.empty()){
//      operatorStack.push(op);
//      return;
//    }
    try{
      if (op == '('){
        operatorStack.push(op);
        return;
      }
      if (op == ')'){
        previousTokenWasNumberOrRightParenthesis = true;
        while (operatorStack.peek() != '('){
          evaluateTopOperator();
        }
        operatorStack.pop();
        return;
      }
      if (op == 'u'){
        operatorStack.push('u');
        return;
      }

      while(precedence(operatorStack.peek()) >= precedence(op)){
        evaluateTopOperator();
      }
      operatorStack.push(op);
    } catch (EmptyStackException e){
      operatorStack.push(op);
    }
  }
  
  static boolean checkTokens (UserInterface ui, Object[] tokens) {
      for (Object token : tokens)
        if (token instanceof Character &&
            OPERATORS.indexOf((Character) token) == -1) {
          ui.sendMessage(token + " is not a valid operator.");
          return false;
        }
      return true;
  }

  static void processExpressions (UserInterface ui, Calculator calculator) {
    while (true) {
      String line = ui.getInfo("Enter arithmetic expression or cancel.");
      if (line == null)
        return;
      Object[] tokens = Tokenizer.tokenize(line);
      if (!checkTokens(ui, tokens))
        continue;
      try {
        for (Object token : tokens)
          if (token instanceof Double)
            calculator.doNumber((Double) token);
          else          
            calculator.doOperator((Character) token);
        double result = calculator.doEquals();
        ui.sendMessage(line + " = " + result);
      } catch (Exception e) {
        ui.sendMessage("Bad expression.");
        calculator.emptyStacks();
      }
      previousTokenWasNumberOrRightParenthesis = false;
    }
  }

  public static void main (String[] args) {
    UserInterface ui = new ConsoleUI();
    Calculator calculator = new Calculator(ui);
    processExpressions(ui, calculator);
  }
}
