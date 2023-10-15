package prog04;

import java.util.EmptyStackException;

/** Class to implement interface StackInterface<E> as a linked list.
*   @author vjm
* */

public class LinkedStack<E> implements StackInterface<E> {
  int size = 0;
  /** This Node class is the building block for a singly-linked list.

      We call it a Node instead of an Entry because it has only one
      data field, instead of a name (key) and number (value) field.

      Each Node has a next pointer to the next Node in the list.

      Since we declare it inside LinkedStack, we can just call it
      Node instead of StackNode and we do not need a second file.
   */
  private class Node {
    // Data Fields
    /** The reference to the data. */
    private E data;

    /** The reference to the next node. */
    private Node next;

    // Constructors
    /** Creates a new node with a null next field.
        @param data The data stored
     */
    private Node (E data) {
      this.data = data;
      next = null; // Necessary in C++ but not in Java.
    }
  } //end class Node

  // Data Fields
  /** The reference to the top stack node. */
  private Node top = null;

  /** Pushes an item onto the top of the stack and returns the item
      pushed.
      @param obj The object to be inserted.
      @return The object inserted.
   */
  public E push (E obj) {
    Node newNode = new Node(obj);
    newNode.next = top;
    top = newNode;
    size++;
    return obj;
  }

  @Override
  public E peek() {
    return top.data;
  }

  @Override
  public E pop() {
    if (empty()){
      throw new EmptyStackException();
    }
    E whatWasThereBefore = top.data;
    top = top.next;
    size--;
    return whatWasThereBefore;
  }

  @Override
  public boolean empty() {
    return size == 0;
  }

  /**** EXERCISE ****/
}
