package ru.spbu.apcyb.svp.tasks;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A simple array-backed linear list.
 *
 * <p>Only the operations required by the task are fully supported:
 * add-to-end, add-at-index, remove-by-index, contains, isEmpty, get-by-index.</p>
 *
 * @param <E> element type
 */
public final class ArrayLinearList<E> extends AbstractList<E> implements List<E> {

  private static final int DEFAULT_CAPACITY = 10;

  private Object[] elements;
  private int size;

  /**
   * Creates an empty list with default capacity.
   */
  public ArrayLinearList() {
    this.elements = new Object[DEFAULT_CAPACITY];
    this.size = 0;
  }

  /**
   * Creates an empty list with provided initial capacity.
   *
   * @param initialCapacity initial capacity (must be >= 0)
   */
  public ArrayLinearList(int initialCapacity) {
    if (initialCapacity < 0) {
      throw new IllegalArgumentException("initialCapacity must be >= 0");
    }
    this.elements = new Object[Math.max(initialCapacity, DEFAULT_CAPACITY)];
    this.size = 0;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public boolean add(E element) {
    ensureCapacity(size + 1);
    elements[size] = element;
    size++;
    modCount++;
    return true;
  }

  @Override
  public void add(int index, E element) {
    checkPositionIndex(index); // allow index == size
    ensureCapacity(size + 1);
    int numMoved = size - index;
    if (numMoved > 0) {
      System.arraycopy(elements, index, elements, index + 1, numMoved);
    }
    elements[index] = element;
    size++;
    modCount++;
  }

  @Override
  public E get(int index) {
    checkElementIndex(index);
    @SuppressWarnings("unchecked")
    E value = (E) elements[index];
    return value;
  }

  @Override
  public E remove(int index) {
    checkElementIndex(index);

    @SuppressWarnings("unchecked")
    final E removed = (E) elements[index];

    int numMoved = size - index - 1;
    if (numMoved > 0) {
      System.arraycopy(elements, index + 1, elements, index, numMoved);
    }
    elements[size - 1] = null;
    size--;
    modCount++;
    return removed;
  }

  @Override
  public boolean contains(Object o) {
    return indexOf(o) >= 0;
  }

  @Override
  public int indexOf(Object o) {
    if (o == null) {
      for (int i = 0; i < size; i++) {
        if (elements[i] == null) {
          return i;
        }
      }
      return -1;
    }
    for (int i = 0; i < size; i++) {
      if (o.equals(elements[i])) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public int lastIndexOf(Object o) {
    if (o == null) {
      for (int i = size - 1; i >= 0; i--) {
        if (elements[i] == null) {
          return i;
        }
      }
      return -1;
    }
    for (int i = size - 1; i >= 0; i--) {
      if (o.equals(elements[i])) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public void clear() {
    Arrays.fill(elements, 0, size, null);
    size = 0;
    modCount++;
  }

  @Override
  public Object[] toArray() {
    return Arrays.copyOf(elements, size);
  }

  @Override
  public <T> T[] toArray(T[] a) {
    Objects.requireNonNull(a, "a");

    if (a.length < size) {
      @SuppressWarnings("unchecked")
      T[] copy = (T[]) java.lang.reflect.Array
          .newInstance(a.getClass().getComponentType(), size);
      for (int i = 0; i < size; i++) {
        @SuppressWarnings("unchecked")
        T value = (T) elements[i];
        copy[i] = value;
      }
      return copy;
    }

    for (int i = 0; i < size; i++) {
      @SuppressWarnings("unchecked")
      T value = (T) elements[i];
      a[i] = value;
    }
    if (a.length > size) {
      a[size] = null;
    }
    return a;
  }

  private void ensureCapacity(int minCapacity) {
    if (minCapacity <= elements.length) {
      return;
    }
    int newCapacity = elements.length + (elements.length >> 1) + 1; // ~1.5x + 1
    if (newCapacity < minCapacity) {
      newCapacity = minCapacity;
    }
    elements = Arrays.copyOf(elements, newCapacity);
  }

  private void checkElementIndex(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }
  }

  private void checkPositionIndex(int index) {
    if (index < 0 || index > size) {
      throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }
  }

  private String outOfBoundsMsg(int index) {
    return "Index: " + index + ", Size: " + size;
  }
}