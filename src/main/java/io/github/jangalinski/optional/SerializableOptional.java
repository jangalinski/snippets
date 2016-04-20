package io.github.jangalinski.optional;


import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SerializableOptional<T> implements Serializable {

  public static <T extends Serializable> SerializableOptional<T> of(final T value) {
    return copyOf(Optional.of(value));
  }

  public static <T extends Serializable> SerializableOptional<T> copyOf(final Optional<T> optional) {
    return new SerializableOptional<T>(optional.isPresent() ? optional.get() : null);
  }

  public static <T extends Serializable> SerializableOptional<T> ofNullable(T value) {
    return copyOf(Optional.ofNullable(value));
  }

  public static <T extends Serializable> SerializableOptional<T> empty() {
    return copyOf(Optional.empty());
  }


  private final T value;

  protected SerializableOptional(final T value) {
    this.value = value;
  }

  public Optional<T> toOptional() {
    return Optional.ofNullable(value);
  }

  public T get() {
    return toOptional().get();
  }

  public boolean isPresent() {
    return toOptional().isPresent();
  }

  public void ifPresent(Consumer<? super T> consumer) {
    toOptional().ifPresent(consumer);
  }

  public T orElse(T defaultValue) {
    return toOptional().orElse(defaultValue);
  }

  public T orElseGet(Supplier<? extends T> supplier) {
    return toOptional().orElseGet(supplier);
  }

  public <X extends Throwable> T orElseThrow(Supplier<? extends X> supplier) throws X {
    return toOptional().orElseThrow(supplier);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    } else if (!(other instanceof SerializableOptional)) {
      return false;
    } else {
      return Objects.equals(this.value, ((SerializableOptional) other).value);
    }
  }

  @Override
  public int hashCode() {
    return toOptional().hashCode();
  }

  @Override
  public String toString() {
    return toOptional().toString();
  }
}
