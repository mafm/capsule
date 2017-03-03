/**
 * Copyright (c) Michael Steindorfer <Centrum Wiskunde & Informatica> and Contributors.
 * All rights reserved.
 *
 * This file is licensed under the BSD 2-Clause License, which accompanies this project
 * and is available under https://opensource.org/licenses/BSD-2-Clause.
 */
package io.usethesource.capsule.generators.multimap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.pholser.junit.quickcheck.generator.ComponentizedGenerator;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import io.usethesource.capsule.SetMultimap;

import static com.pholser.junit.quickcheck.internal.Ranges.Type.INTEGRAL;
import static com.pholser.junit.quickcheck.internal.Ranges.checkRange;

public abstract class AbstractSetMultimapGenerator<T extends SetMultimap.Immutable>
    extends ComponentizedGenerator<T> {

  private Class<T> target;
  private Size sizeRange;

  public AbstractSetMultimapGenerator(Class<T> target) {
    super(target);
    this.target = target;
  }

  public void configure(Size size) {
    this.sizeRange = size;
    checkRange(INTEGRAL, size.min(), size.max());
  }

  protected final int size(SourceOfRandomness random, GenerationStatus status) {
    return sizeRange != null ? random.nextInt(sizeRange.min(), sizeRange.max()) : status.size();
  }

  protected T empty() {
    try {
      final Method persistentSetOfEmpty = target.getMethod("of");
      return (T) persistentSetOfEmpty.invoke(null);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException e) {
      throw new RuntimeException();
    }
  }

  @Override
  public int numberOfNeededComponents() {
    return 2;
  }

  @Override
  public T generate(SourceOfRandomness random, GenerationStatus status) {
    int size = size(random, status);

    T items = empty();

    final int oneToOneStepSize = 2;
    final int oneToManySetSize = 2;

    int i = size - 1;
    while (i >= 0) {
      final Object item0 = componentGenerators().get(0).generate(random, status);

      final int generatedTuplesCount;
      if (i % oneToOneStepSize == 0) {
        final Object item1 = componentGenerators().get(1).generate(random, status);
        items = (T) items.__insert(item0, item1);

        generatedTuplesCount = 1;
      } else {
        for (int j = oneToManySetSize - 1; j >= 0 && i >= 0; j--) {
          final Object item1 = componentGenerators().get(1).generate(random, status);
          items = (T) items.__insert(item0, item1);
        }

        generatedTuplesCount = oneToManySetSize;
      }

      i = i - generatedTuplesCount;
    }

    return items;
  }

  @Override
  public boolean canShrink(Object larger) {
    return false;
  }

  // @Override public List<T> doShrink(SourceOfRandomness random, T larger) {
  // @SuppressWarnings("unchecked")
  // List<Object> asList = new ArrayList<>(larger.entrySet());
  //
  // List<T> shrinks = new ArrayList<>();
  // shrinks.addAll(removals(asList));
  //
  // @SuppressWarnings("unchecked")
  // Shrink<Object> generator = (Shrink<Object>) componentGenerators().get(0);
  //
  // List<List<Object>> oneItemShrinks = shrinksOfOneItem(random, asList, generator);
  // shrinks.addAll(oneItemShrinks.stream()
  // .map(this::convert)
  // .filter(this::inSizeRange)
  // .collect(Collectors.toList()));
  //
  // return shrinks;
  // }

  private boolean inSizeRange(T items) {
    return sizeRange == null
        || (items.size() >= sizeRange.min() && items.size() <= sizeRange.max());
  }

  // private List<T> removals(List<?> items) {
  // return stream(halving(items.size()).spliterator(), false)
  // .map(i -> removeFrom(items, i))
  // .flatMap(Collection::stream)
  // .map(this::convert)
  // .filter(this::inSizeRange)
  // .collect(Collectors.toList());
  // }
  //
  // @SuppressWarnings("unchecked")
  // private T convert(List<?> items) {
  // T converted = empty();
  // for (Object item : items) {
  // converted = (T) converted.__insert(item);
  // }
  // return converted;
  // }

}
