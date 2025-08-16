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
package at.or.reder.jio;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum LineFlag {
  KERNEL(0x00000001, true, true, null),
  OUTPUT(0x00000002, true, false, null),
  ACTIVE_LOW(0x00000004, true, false, "al"),
  OPEN_DRAIN(0x00000008, true, false, "od"),
  OPEN_SOURCE(0x00000010, true, false, "os"),
  PULL_UP(0x00000020, true, false, "pu"),
  PULL_DOWN(0x00000040, true, false, "pd"),
  PULL_NONE(0x00000080, true, false, "pn"),
  INPUT(0x00010000, false, true, null),
  RISING_EDGE(0x00020000, false, true, "ri"),
  FALLING_EDGE(0x00040000, false, true, "fa"),
  REALTIME_CLOCK(0x00080000, false, true, null);

  private final int magic;
  private final boolean outputFlag;
  private final boolean inputFlag;
  private final String shortName;

  public static String getLineFlags()
  {
    return Stream.of(values()).map(LineFlag::getShortName).filter(Objects::nonNull).collect(Collectors.joining(","));
  }

  public static Set<LineFlag> fromShortNames(String shortNames)
  {
    String[] parts = shortNames.split(",");
    return Stream.of(parts)
            .map(LineFlag::fromShortName)
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(() -> EnumSet.noneOf(LineFlag.class)));
  }

  public static LineFlag fromShortName(@NonNull String shortName)
  {
    for (LineFlag flag : values()) {
      if (shortName.equals(flag.getShortName())) {
        return flag;
      }
    }
    return null;
  }

  public static Set<LineFlag> toSet(int lineInfoFlags)
  {
    EnumSet<LineFlag> result = EnumSet.noneOf(LineFlag.class);
    for (LineFlag flag : values()) {
      if ((flag.getMagic() & lineInfoFlags) != 0) {
        result.add(flag);
      }
    }
    return result;
  }

  public int toLineInfoFlags(Collection<LineFlag> flags)
  {
    if (flags != null) {
      return flags.stream()
              .mapToInt(LineFlag::getMagic)
              .sum();
    }
    return 0;
  }
}
