package ru.spbu.apcyb.svp.tasks;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Stack implementation backed by {@link ArrayLinearList}.
 *
 * @param <E> element type
 */
public final class ArrayListStack<E> extends Stack<E> {

  private final ArrayLinearList<E> list;

  /**
   * Creates an empty stack.
   */
  public ArrayListStack() {
    this.list = new ArrayLinearList<>();
  }

  @Override
  public boolean empty() {
    return list.isEmpty();
  }

  @Override
  public E push(E item) {
    list.add(item);
    return item;
  }

  @Override
  public synchronized E pop() {
    if (list.isEmpty()) {
      throw new EmptyStackException();
    }
    return list.remove(list.size() - 1);
  }

  @Override
  public synchronized E peek() {
    if (list.isEmpty()) {
      throw new EmptyStackException();
    }
    return list.get(list.size() - 1);
  }

  @Override
  public synchronized int size() {
    return list.size();
  }
}