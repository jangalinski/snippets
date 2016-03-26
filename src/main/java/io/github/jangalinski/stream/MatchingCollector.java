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
    // create a new empty value container (when the first element of a stream is reached)
    return AtomicReference::new;
  }

  @Override
  public BiConsumer<AtomicReference<T>, T> accumulator() {
    // for each item on stream, call test() with container value and item and set best match in container.
    return (container, item) -> container.accumulateAndGet(item, (o,n) -> test(o,n) ? n : o);
  }

  @Override
  public BinaryOperator<AtomicReference<T>> combiner() {
    // when joining parallel stream containers, use the test() function to check which one contains the best matching value.
    return (a, b) -> test(a.get(), b.get()) ? b : a;
  }

  @Override
  public Function<AtomicReference<T>, Optional<T>> finisher() {
    // when the stream is ended, return an Optional of the container value
    return reference -> Optional.ofNullable(reference.get());
  }

  @Override
  public Set<Characteristics> characteristics() {
    // we only return one value, no order required
    return EnumSet.of(Characteristics.UNORDERED);
  }

  @Override
  public boolean test(T oldValue, T newValue) {
    // the new value is better then the old one if:
    // - it is not null
    // - there is no container value
    // - or if there is a container value, evaluating the given predicate is true
    return itemIsNotNull.and(containerIsNull.or(predicate)).test(oldValue, newValue);
  }
}
