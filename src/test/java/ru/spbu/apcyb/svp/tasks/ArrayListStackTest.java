package ru.spbu.apcyb.svp.tasks;

import org.junit.jupiter.api.Test;

import java.util.EmptyStackException;

import static org.junit.jupiter.api.Assertions.*;

class ArrayListStackTest {

  @Test
  void empty_stack_behaviour() {
    ArrayListStack<Integer> s = new ArrayListStack<>();
    assertTrue(s.empty());
    assertEquals(0, s.size());

    assertThrows(EmptyStackException.class, s::peek);
    assertThrows(EmptyStackException.class, s::pop);
  }

  @Test
  void push_pop_peek_work_lifo() {
    ArrayListStack<String> s = new ArrayListStack<>();

    s.push("a");
    s.push("b");
    s.push("c");

    assertFalse(s.empty());
    assertEquals(3, s.size());
    assertEquals("c", s.peek());
    assertEquals("c", s.pop());
    assertEquals("b", s.pop());
    assertEquals("a", s.pop());
    assertTrue(s.empty());
  }
}