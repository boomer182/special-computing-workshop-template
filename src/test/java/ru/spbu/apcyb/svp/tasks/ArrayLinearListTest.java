package ru.spbu.apcyb.svp.tasks;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrayLinearListTest {

  @Test
  void new_list_is_empty() {
    ArrayLinearList<Object> list = new ArrayLinearList<>();
    assertTrue(list.isEmpty());
    assertEquals(0, list.size());

    list.add(new Object());
    assertFalse(list.isEmpty());
    assertEquals(1, list.size());
  }

  @Test
  void add_to_end_and_get_by_index() {
    ArrayLinearList<String> list = new ArrayLinearList<>();
    list.add("a");
    list.add("b");

    assertEquals(2, list.size());
    assertEquals("a", list.get(0));
    assertEquals("b", list.get(1));
  }

  @Test
  void add_at_index_shifts_elements() {
    ArrayLinearList<Integer> list = new ArrayLinearList<>();
    list.add(1);
    list.add(3);

    list.add(1, 2);

    assertEquals(List.of(1, 2, 3), list);
  }

  @Test
  void remove_by_index_shifts_left() {
    ArrayLinearList<String> list = new ArrayLinearList<>();
    list.add("a");
    list.add("b");
    list.add("c");

    assertEquals("b", list.remove(1));
    assertEquals(List.of("a", "c"), list);
  }

  @Test
  void contains_works_for_null_and_values() {
    ArrayLinearList<String> list = new ArrayLinearList<>();
    list.add(null);
    list.add("x");

    assertTrue(list.contains(null));
    assertTrue(list.contains("x"));
    assertFalse(list.contains("y"));
  }

  @Test
  void index_checks_throw() {
    ArrayLinearList<Integer> list = new ArrayLinearList<>();
    assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
    assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
    assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, 10));

    list.add(1);
    assertEquals(1, list.get(0)); // <-- “query”, предупреждение пропадёт

    assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, 10));
  }

  @Test
  void grows_capacity_on_many_adds() {
    ArrayLinearList<Integer> list = new ArrayLinearList<>(0);
    for (int i = 0; i < 200; i++) {
      list.add(i);
    }
    assertEquals(200, list.size());
    assertEquals(0, list.get(0));
    assertEquals(199, list.get(199));
  }
}