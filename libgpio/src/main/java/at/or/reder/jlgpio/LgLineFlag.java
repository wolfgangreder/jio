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
package at.or.reder.jlgpio;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

public enum LgLineFlag {
  KERNEL(0x00000001),
  OUTPUT(0x00000002),
  ACTIVE_LOW(0x00000004),
  OPEN_DRAIN(0x00000008),
  OPEN_SOURCE(0x00000010),
  PULL_UP(0x00000020),
  PULL_DOWN(0x00000040),
  PULL_NONE(0x00000080),
  INPUT(0x00010000),
  RISING_EDGE(0x00020000),
  FALLING_EDGE(0x00040000),
  REALTIME_CLOCK(0x00080000);

  private final int magic;

  private LgLineFlag(int magic)
  {
    this.magic = magic;
  }

  public int getMagic()
  {
    return magic;
  }

  public static Set<LgLineFlag> toSet(int lineInfoFlags)
  {
    EnumSet<LgLineFlag> result = EnumSet.noneOf(LgLineFlag.class);
    for (LgLineFlag flag : values()) {
      if ((flag.getMagic() & lineInfoFlags) != 0) {
        result.add(flag);
      }
    }
    return result;
  }

  public int toLineInfoFlags(Collection<LgLineFlag> flags)
  {
    if (flags != null) {
      return flags.stream()
              .mapToInt(LgLineFlag::getMagic)
              .sum();
    }
    return 0;
  }
}
