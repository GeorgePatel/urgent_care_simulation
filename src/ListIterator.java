import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListIterator<E> implements Iterator<E> {
    private Node<E> list;

    public ListIterator(Node<E> head) {
        this.list = head;
    }

    public boolean hasNext(){
        return list != null;
    }
    public E next() {
        E answer;
        if (!hasNext())
            throw new NoSuchElementException("The Lister is empty");

        answer = list.getData();
        list = list.getLink();

        return answer;
    }
}
