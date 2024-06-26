import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;



/**
 * circular doubly-linked lists that supportsFail Fast policy.
 *
 * @author Samuel A. Rebelsky
 * @author Nye Tenerelli
 */

public class SimpleCDLL<T> implements SimpleList<T> {
  // +--------+------------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The front of the list
   */
  Node2<T> dummy;

  /**
   * The number of values in the list.
   */
  int size;
  int numchanges = 0;

  // +--------------+------------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create an empty list.
   */
  public SimpleCDLL() {
    this.dummy = new Node2<T>(null);
    this.dummy.next = this.dummy;
    this.dummy.prev = this.dummy;

    this.size = 0;
    numchanges = 0;
  } // SimpleDLL

  // +-----------+---------------------------------------------------------
  // | Iterators |
  // +-----------+

  public Iterator<T> iterator() {
    return listIterator();
  } // iterator()

  public ListIterator<T> listIterator() {
    return new ListIterator<T>() {
      // +--------+--------------------------------------------------------
      // | Fields |
      // +--------+

      /**
       * The position in the list of the next value to be returned.
       * Included because ListIterators must provide nextIndex and
       * prevIndex.
       */
      int pos = 0;
      int changes = SimpleCDLL.this.numchanges;
      /**
       * The cursor is between neighboring values, so we start links
       * to the previous and next value..
       */
      Node2<T> prev = dummy;
      Node2<T> next = SimpleCDLL.this.dummy.next;

      /**
       * The node to be updated by remove or set.  Has a value of
       * null when there is no such value.
       */

      Node2<T> update = null;

      // +---------+-------------------------------------------------------
      // | Methods |
      // +---------+

      public void add(T val) throws UnsupportedOperationException {
        failFast();

        // Normal case
        this.prev = this.prev.insertAfter(val);

        // Note that we cannot update
        this.update = null;

        // Increase the size
        ++SimpleCDLL.this.size;

        // Update the position.  (See SimpleArrayList.java for more of
        // an explanation.)
        ++this.pos;
        SimpleCDLL.this.numchanges = ++this.changes;
      } // add(T)

      public boolean hasNext() {
        failFast();
        return (this.pos < SimpleCDLL.this.size);
        // return (this.next != SimpleCDLL.this.dummy);

      } // hasNext()

      public boolean hasPrevious() {
        failFast();
        return (this.pos > 0);
        //return (this.next != SimpleCDLL.this.dummy);

      } // hasPrevious()

      public T next() {
        failFast();
        if (!this.hasNext()) {
         throw new NoSuchElementException();
        } // if
        // Identify the node to update
        this.update = this.next;
        // Advance the cursor
        this.prev = this.next;
        this.next = this.next.next;
        // Note the movement
        ++this.pos;
        // And return the value
        return this.update.value;
      } // next()

      public int nextIndex() {
        failFast();
        return this.pos;
      } // nextIndex()

      public int previousIndex() {
        failFast();
        return this.pos - 1;
      } // prevIndex

      public T previous() throws NoSuchElementException {
        failFast();
        if (!this.hasPrevious())
          throw new NoSuchElementException();
          //Identify the node to update
          this.update = this.prev;
          // Advance the cursor
          this.next = this.prev;
          this.prev = this.prev.prev;
          // Note the movement
          --this.pos;
          // And return the value
          return this.update.value;
      } // prev ious()

      public void remove() {
        failFast();
        // Sanity check
        if (this.update == null) {
          throw new IllegalStateException();
        } // if

        // Update the cursor
        if (this.next == this.update) {
          this.next = this.update.next;
        } // if
        if (this.prev == this.update) {
          this.prev = this.update.prev;
          --this.pos;
        } // if


        // Do the real work
        this.update.remove();
        --SimpleCDLL.this.size;

        SimpleCDLL.this.numchanges = ++this.changes;

        // Note that no more updates are possible
        this.update = null;
      } // remove()

      public void set(T val) {
        failFast();
        // Sanity check
        if (this.update == null) {
          throw new IllegalStateException();
        } // if
        // Do the real work
        this.update.value = val;
      } // set(T)

      
      void failFast()  {
        if (SimpleCDLL.this.numchanges != this.changes)  {
          throw new ConcurrentModificationException();
        }
      }
    };
  } // listIterator()



  //understand that cdl...
  //SimpleCDLL.this.iterate

} // class SimpleDLL<T>
