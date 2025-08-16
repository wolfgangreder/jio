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
package at.or.reder.jio.impl;

import at.or.reder.jio.IoLine;
import at.or.reder.jio.LineFlag;
import at.or.reder.jio.LineInfo;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractIoLine implements IoLine {

  @NonNull
  @Getter
  private final IoChipImpl chip;
  @Getter
  private final int lineNum;
  @Getter
  private final Set<LineFlag> flags;
  @Getter(AccessLevel.PROTECTED)
  @Setter(AccessLevel.PROTECTED)
  private Integer lineHandle = null;

  protected static Set<LineFlag> toUnmodifiableSet(Collection<LineFlag> flags)
  {
    if (flags == null || flags.isEmpty()) {
      return Set.of();
    } else {
      return Set.copyOf(flags);
    }
  }

  @Override
  public LineInfo getLineInfo() throws IOException
  {
    return chip.getLineInfo(lineNum);
  }

  @Override
  public void close() throws IOException
  {
    try {
      chip.removeLine(this);
      if (lineHandle != null) {
        chip.getNativeSpi().lgGpioFree(chip.getHandle(), lineNum);
      }
    } finally {
      lineHandle = null;
    }
  }

}
