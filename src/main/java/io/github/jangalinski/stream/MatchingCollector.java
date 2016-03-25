package io.github.jangalinski.stream;


import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class MatchingCollector<T> implements Collector<T, AtomicReference<T>, Optional<T>>, BiPredicate<T, T> {

  public static <T> MatchingCollector<T> match(BiPredicate<T,T> predicate) {
    return new MatchingCollector<>(predicate);
  }

  protected final BiPredicate<T, T> itemIsNotNull = (o, i) -> i != null;
  protected final BiPredicate<T, T> containerIsNull = (o, i) -> o == null;
  protected final BiPredicate<T, T> predicate;

  protected MatchingCollector(BiPredicate<T, T> predicate) {
    this.predicate = predicate;
  }

  @Override
  public Supplier<AtomicReference<T>> supplier() {
    return () -> new AtomicReference<T>();
  }

  @Override
  public BiConsumer<AtomicReference<T>, T> accumulator() {
    return (container, item) -> {
      if (test(container.get(), item)) {
        container.set(item);
      }
    };
  }

  @Override
  public BinaryOperator<AtomicReference<T>> combiner() {
    return (a, b) -> a;
  }

  @Override
  public Function<AtomicReference<T>, Optional<T>> finisher() {
    return reference -> Optional.ofNullable(reference.get());
  }

  @Override
  public Set<Characteristics> characteristics() {
    return EnumSet.of(Characteristics.UNORDERED);
  }

  @Override
  public boolean test(T containerValue, T streamItem) {
    return itemIsNotNull.and(containerIsNull.or(predicate)).test(containerValue, streamItem);
  }
}
