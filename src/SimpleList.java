import java.util.Iterator;
import java.util.ListIterator;
/**
 * A class that allows us to access the iterator.
 * @author Samuel A. Rebelsky??
 */
/**
 * Very simple lists.
 */
public interface SimpleList<T> extends Iterable<T> {
  public Iterator<T> iterator();

  public ListIterator<T> listIterator();
} // interface SimpleList<T>
