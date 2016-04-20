package io.github.jangalinski.optional;

import org.junit.Test;
import org.junit.internal.ArrayComparisonFailure;
import org.springframework.util.SerializationUtils;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class SerializableOptionalTest {

  public static class A extends SerializableOptional<String> {
    protected A(String value) {
      super(value);
    }
  }

  @Test
  public void of_with_value() throws Exception {
    assertThat(A.of("foo").get()).isEqualTo("foo");
  }

  @Test(expected = NullPointerException.class)
  public void of_without_value() throws Exception {
    A.of(null);
  }

  @Test
  public void ofNullable_with_value() throws Exception {
    assertThat(A.ofNullable("foo").get()).isEqualTo("foo");
  }

  @Test
  public void ofNullable_without_value() throws Exception {
    assertThat(A.ofNullable(null).isPresent()).isFalse();
  }

  @Test
  public void empty() throws Exception {
    assertThat(A.empty().toOptional()).isEmpty();
  }

  @Test(expected = NoSuchElementException.class)
  public void get_without_value() throws Exception {
    A.ofNullable(null).get();
  }

  @Test(expected = NoSuchElementException.class)
  public void get_fail_for_emptyOptional() throws Exception {
    A.copyOf(Optional.ofNullable(null)).get();
  }

  @Test
  public void isPresent_is_false_for_null() throws Exception {
    assertThat(A.ofNullable(null).isPresent()).isFalse();
    assertThat(A.copyOf(Optional.ofNullable(null)).isPresent()).isFalse();
  }

  @Test
  public void toOptional_isPresent() throws Exception {
    assertThat(A.of("a").toOptional()).isPresent();
    assertThat(A.ofNullable("a").toOptional()).isPresent();
    assertThat(A.copyOf(Optional.ofNullable("b")).toOptional()).isPresent();
  }


  @Test
  public void toOptional__not_isPresent() throws Exception {
    assertThat(A.ofNullable(null).toOptional()).isEmpty();
    assertThat(A.copyOf(Optional.ofNullable(null)).toOptional()).isEmpty();
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void orElseThrow() throws Exception {
    A.ofNullable(null).orElseThrow(ArrayIndexOutOfBoundsException::new);
  }


  @Test
  public void orElse() throws Exception {
    assertThat(A.ofNullable(null).orElse("foo")).isEqualTo("foo");
  }


  @Test
  public void orElseGet() throws Exception {
    assertThat(A.ofNullable(null).orElseGet(() -> "foo")).isEqualTo("foo");
  }

  @Test
  public void serializeDeserialize() throws Exception {
    assertThat(SerializationUtils.deserialize(SerializationUtils.serialize(A.of("foo")))).isEqualTo(A.of("foo"));
  }

}
