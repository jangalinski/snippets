package io.github.jangalinski.stream;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import static io.github.jangalinski.stream.MatchingCollector.match;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class MatchingCollectorTest {

  @RunWith(Parameterized.class)
  public static class Predicate {

    private static Map<String, BiPredicate<Integer, Integer>> PREDICATES = new HashMap<String, BiPredicate<Integer, Integer>>() {{
      put("alwaysTrue", (a, b) -> true);
      put("alwaysFalse", (a, b) -> false);
      put("greaterThan", (a, b) -> b > a);
    }};


    @Parameterized.Parameters(name = "{index} - {0} {1} {2} {3}")
    public static Collection<Object[]> data() {

      return Arrays.asList(
        new Object[][]{
          // as long is the stream item is null, result is always false
          {"alwaysTrue", null, null, false},
          {"alwaysTrue", 1, null, false},
          {"alwaysFalse", null, null, false},
          {"alwaysFalse", 1, null, false},
          {"greaterThan", null, null, false},
          {"greaterThan", 1, null, false},
          // as long as the container is empty, the result is always true (first element)
          {"alwaysTrue", null, 1, true},
          {"alwaysFalse", null, 1, true},
          {"greaterThan", null, 1, true},
          // if container present and item not null, it depends on the predicate
          {"alwaysTrue", 1, 1, true},
          {"alwaysFalse", 1, 1, false},
          {"greaterThan", 2, 3, true},
          {"greaterThan", 2, 1, false},
          {"greaterThan", 2, 2, false},
        });
    }

    @Parameterized.Parameter(0)
    public String predicateName;

    @Parameterized.Parameter(1)
    public Integer containerValue;

    @Parameterized.Parameter(2)
    public Integer streamItem;

    @Parameterized.Parameter(3)
    public boolean expected;

    @Test
    public void evaluatePredicate() {
      assertThat(new MatchingCollector<Integer>(PREDICATES.get(predicateName)).test(containerValue, streamItem))
        .isEqualTo(expected);
    }

  }


  public static class Collector {
    @Test
    public void keep_highest_value() {
      assertThat(Stream.of(1, 3, 2).collect(match((a, b) -> b > a))).isEqualTo(Optional.of(3));
    }

    @Test
    public void keep_highest_value_parallel() {
      assertThat(Stream.of(1, 45, 3, 2, null, 10,7,19,4,7,39).parallel().collect(match((a, b) -> b > a))).isEqualTo(Optional.of(45));
    }


  }
}
