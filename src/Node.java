public class Node<T> {
    /**
     * Invariant of the Node class:
     1. The data of the element in the Node is stored in the instance variable data.
     2. The pointer to the next element in the linked list is stored in the instance variable link.
     */
    private T data;
    private Node<T> link;

    /**
     * Creates a new Node without a pointer to the next Node in the linked list
     * @param data - the element data
     */
    public Node(T data) {
        this.data = data;
        this.link = null;
    }

    /**
     * Creates a new Node with a pointer to the next Node in the linked list
     * @param data - the element data
     * @param link - the pointer to the next Node
     */
    public Node(T data, Node<T> link) {
        this.data = data;
        this.link = link;
    }

    /**
     * @return the instance data
     */
    public T getData() {
        return data;
    }

    /**
     * Updates the element data of the Node.
     * @param data the new element data
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * @return the link to the next Node
     */
    public Node<T> getLink() {
        return link;
    }

    /**
     * Updates the pointer to the next Node in the linked list.
     * @param link the new link
     */
    public void setLink(Node<T> link) {
        this.link = link;
    }

    /**
     * Inserts a new node after an existing node in the linked list.
     * Can also be used to add to the tail of the linked list.
     * @param data data for the Node
     */
    public void addNodeAfter(T data) {
        link = new Node<T>(data, link);
    }

    /**
     * Copies a linked list and returns an array containing references to head and tail of the newly copied list.
     * @param source the original linked list
     * @return Object array containing head in 0th index and tail in 1st index
     * @param <T> can be used with Generics
     */
    public static <T> Object[] listCopyWithTail(Node<T> source) {
        Node<T> copyHead;
        Node<T> copyTail;
        Object[] answer = new Object[2];
        if (source == null)
            return answer; // The answer has two null references.

        copyHead = new Node<T>(source.data, null);
        copyTail = copyHead;

        while (source.link != null) {
            source = source.link;
            copyTail.addNodeAfter(source.data);
            copyTail = copyTail.link;
        }

        answer[0] = copyHead;
        answer[1] = copyTail;
        return answer;
    }

    /**
     * Checks whether an element exists in the linked list.
     * @param cursor the starting point or head of the linked list
     * @param element the element to be checked
     * @return true if element exists, false if element does not exist
     * @param <T> static method
     */
    public static <T> boolean contains(Node<T> cursor, T element) {
        Node<T> found = listSearch(cursor, element);
        return found != null;
    }

    /**
     * Searches the linked list for the specified element.
     * @param cursor the starting point or head of the linked list
     * @param element the element to be searched
     * @return Node of the element if found, returns null if element not found
     * @param <T> static method
     */
    public static <T> Node<T> listSearch(Node<T> cursor, T element) {
        while (cursor != null) {
            if (cursor.getData().equals(element)) { // if node exists
                return cursor; // return node
            }
            cursor = cursor.getLink();  // Move to the next node
        }
        return null;
    }
}

