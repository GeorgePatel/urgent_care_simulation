import java.util.NoSuchElementException;

public class Queue<E> implements Cloneable {

    /**
     * Invariant of the LinkedQueue class:
     1. Elements can only be inserted into the rear of the Queue and removed from the head of the Queue.
     2. Instance variable front holds reference to head of the Queue
     3. Instance variable rear holds reference to rear of the Queue
     4. Instance variable numOfElements holds count of number of elements currently in Queue
     */
    private Node<E> front;
    private Node<E> rear;
    int numOfElements;

    /**
     * Creates a new LinkedQueue with null references for both front and rear.
     */
    public Queue() {
        front = null;
        rear = null;
        numOfElements = 0;
    }

    public void add(E data) {
        if (data == null) {
            return;
        }
        if (isEmpty()) {
            front = new Node<E>(data, null);
            rear = front;
        } else {
            rear.setLink(new Node<>(data, null));
            rear = rear.getLink();
        }
        numOfElements++;
    }

    public E remove() {
        E answer;
        if (numOfElements == 0)
            throw new NoSuchElementException("Queue underflow.");
        answer = front.getData();
        front = front.getLink();
        numOfElements--;
        if (numOfElements == 0)
            rear = null;
        return answer;
    }

    public boolean isEmpty() {
        return (numOfElements == 0);
    }

    public int size() {
        return numOfElements;
    }

    public Queue<E> clone() {
        Queue<E> answer;
        try {
            answer = (Queue<E>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException
                    ("This class does not implement Cloneable.");
        }
        Object[] cloneInfo = Node.listCopyWithTail(front);
        answer.front = (Node<E>) cloneInfo[0];
        answer.rear = (Node<E>) cloneInfo[1];
        return answer;
    }

    public E peekFront() {
        return front.getData();
    }

    public Node<E> getFront() {
        return front;
    }

    public E peekRear() {
        return rear.getData();
    }

    /**
     * From a clone of this queue, count the number of elements removed until target is found.
     * Otherwise, return 0 if target does not exist in queue.
     * @param target the element to search for
     * @return the count of elements removed before reaching target or 0 if target doesn't exist
     */
    public int removeUntil(E target) {
        Queue<E> clone = this.clone();
        int removed = 0;
        while (!clone.isEmpty() && !target.equals(clone.peekFront())) {
            E removedElement = clone.remove();
            removed++;
        }
        if (clone.isEmpty()) {
            System.out.println(target + " not found.");
            return 0;
        }
        return removed;
    }

}

