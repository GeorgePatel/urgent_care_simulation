import java.util.EmptyStackException;

public class Stack<E> implements Cloneable {
    /**
     * Invariant of the Stack class:
     1. The element at the top of the stack is stored in the instance variable top which is a linked list.
     2. The length or number of elements in the stack is stored in the instance variable length.
     */
    private Node<E> top;
    private int length;

    /**
     * Initializes an empty stack.
     */
    public Stack() {
        top = null;
        length = 0;
    }

    public boolean isEmpty() {
        return top == null;
    }

    /**
     * View the element at top of the stack without removing it.
     * @return the element's data
     */
    public E peek() {
        if (top == null || length == 0) {
            throw new EmptyStackException();
        }
        return top.getData();
    }

    /**
     * Creates a Node with element data. Add new element to the top of stack.
     * Increments stack size.
     * @param data the data of the element
     */
    public void push(E data) {
        if (data == null) {
            return;
        }
        top = new Node<>(data, top);
        length++;
    }

    /**
     * Removes the element at the top of stack. Throws EmptyStackException if stack is empty.
     * Update top to following element in stack. Decrement stack size.
     * @return the removed element data
     */
    public E pop() {
        E removed;
        if (top == null) {
            throw new EmptyStackException();
        }
        removed = top.getData();
        top = top.getLink();
        length--;
        return removed;
    }

    public int getLength() {
        return length;
    }

    public Node<E> getTop() {
        return top;
    }

    public void setTop(Node<E> top) {
        this.top = top;
    }

    public Stack<E> clone() {
        Stack<E> answer;
        try {
            answer = (Stack<E>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("This class does not implement Cloneable.");
        }
        return answer;
    }
}

