/**
 * Copyright (c) Michael Steindorfer <Centrum Wiskunde & Informatica> and Contributors.
 * All rights reserved.
 *
 * This file is licensed under the BSD 2-Clause License, which accompanies this project
 * and is available under https://opensource.org/licenses/BSD-2-Clause.
 */
package io.usethesource.capsule;

import java.util.Iterator;

import io.usethesource.capsule.experimental.ordered.OrderedTrieMap;
import org.junit.Assert;
import org.junit.Test;

import static java.lang.System.out;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OrderedTrieMapTest {

  static OrderedTrieMap<String, Integer> createBasicMap() {
    return OrderedTrieMap.<String, Integer>of().insert("5", 5).insert("7", 7).insert("-1", -1)
        .insert("32", 32).insert("1500", 1500);
  }

  static OrderedTrieMap<String, Integer> createBiggerMap() {
    return OrderedTrieMap.<String, Integer>of().insert("5", 5).insert("7", 7).insert("-1", -1)
        .insert("32", 32).insert("1500", 1500).insert("34934502", 34934502).insert("3344", 3344)
        .insert("0", 0).insert("13", 13).insert("345", 345).insert("-15", -15).insert("33", 33)
        .insert("32", 32);
  }

  @Test
  public void basicContainsKeyValue() {
    OrderedTrieMap<String, Integer> map = createBasicMap();

    assertTrue(map.contains("5"));
    assertTrue(map.containsValue(5));

    assertTrue(map.contains("7"));
    assertTrue(map.containsValue(7));

    assertTrue(map.contains("-1"));
    assertTrue(map.containsValue(-1));

    assertTrue(map.contains("32"));
    assertTrue(map.containsValue(32));

    assertTrue(map.contains("1500"));
    assertTrue(map.containsValue(1500));
  }

  @Test
  public void basicOrderedKeyIterator() {
    OrderedTrieMap<String, Integer> map = createBasicMap();

    Iterator<?> it = map.orderedKeyIterator();

    assertEquals("5", it.next());
    assertEquals("7", it.next());
    assertEquals("-1", it.next());
    assertEquals("32", it.next());
    assertEquals("1500", it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void basicReverseOrderedKeyIterator() {
    OrderedTrieMap<String, Integer> map = createBasicMap();

    Iterator<?> it = map.reverseOrderedKeyIterator();

    assertEquals("1500", it.next());
    assertEquals("32", it.next());
    assertEquals("-1", it.next());
    assertEquals("7", it.next());
    assertEquals("5", it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void basicOrderedKeyIteratorAfterDeleteAndInsert() {
    OrderedTrieMap<String, Integer> map = createBasicMap().remove("-1").insert("-1", -1);

    Iterator<?> it = map.orderedKeyIterator();

    assertEquals("5", it.next());
    assertEquals("7", it.next());
    assertEquals("32", it.next());
    assertEquals("1500", it.next());
    assertEquals("-1", it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void basicOrderedKeyIteratorAfterReplace() {
    OrderedTrieMap<String, Integer> map = createBasicMap().insert("-1", -1);

    Iterator<?> it = map.orderedKeyIterator();

    assertEquals("5", it.next());
    assertEquals("7", it.next());
    assertEquals("-1", it.next());
    assertEquals("32", it.next());
    assertEquals("1500", it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void basicReplaceRetainsSize() {
    Assert.assertEquals(createBasicMap().size(), createBasicMap().insert("-1", -1).size());
  }

  @Test
  public void basicToString() {
    OrderedTrieMap<String, Integer> map = createBasicMap();
    out.println(map);
  }

}
