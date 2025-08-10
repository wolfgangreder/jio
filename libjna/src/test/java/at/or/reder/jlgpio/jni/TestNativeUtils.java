/*
 * Copyright 2025 Wolfgang Reder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.or.reder.jlgpio.jni;

import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TestNativeUtils {

  static Stream<Arguments> testNtsToString()
  {
    return Stream.of(Arguments.of(null, null),
                     Arguments.of(new byte[]{'W', 'o', 'l', 'f', 'i'}, "Wolfi"),
                     Arguments.of(new byte[]{'W', 'o', (byte) 0, 'f', 'i'}, "Wo"),
                     Arguments.of(new byte[]{}, ""),
                     Arguments.of(new byte[]{0}, ""));
  }

  @ParameterizedTest
  @MethodSource
  void testNtsToString(byte[] array, String expected)
  {
    assertThat(NativeUtils.ntsToString(array)).isEqualTo(expected);
  }
}
