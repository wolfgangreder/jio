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

import at.or.reder.jio.ChipId;
import at.or.reder.jio.ChipInfo;
import at.or.reder.jio.InputLine;
import at.or.reder.jio.IoChip;
import at.or.reder.jio.IoLine;
import at.or.reder.jio.LineFlag;
import at.or.reder.jio.LineInfo;
import at.or.reder.jio.OutputLine;
import at.or.reder.jio.spi.NativeSpi;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
final class IoChipImpl implements IoChip {

  @NonNull
  @Getter(AccessLevel.PACKAGE)
  private final NativeSpi nativeSpi;
  @Getter(AccessLevel.PACKAGE)
  private final int handle;
  @Getter
  private final ChipId id;
  private final List<IoLine> openLines = new ArrayList<>();

  static IoChipImpl build(@NonNull NativeSpi nativeSpi, @NonNull ChipId chipId) throws IOException
  {
    int handle = nativeSpi.lgGpiochipOpen(chipId.getDeviceNum());
    return new IoChipImpl(nativeSpi, handle, chipId);
  }

  @Override
  public ChipInfo getChipInfo() throws IOException
  {
    return nativeSpi.lgGpioGetChipInfo(handle);
  }

  @Override
  public LineInfo getLineInfo(int numLine) throws IOException
  {
    return nativeSpi.lgGpioGetLineInfo(handle, numLine);
  }

  @Override
  public Stream<LineInfo> enumerateLines() throws IOException
  {
    ChipInfo chipInfo = getChipInfo();
    try {
      return Stream.iterate(0, current -> (current + 1) < chipInfo.getNumLines(), current -> ++current)
              .map(ThrowableWrapper.wrapFunction((Integer arg) -> nativeSpi.lgGpioGetLineInfo(handle, arg)));
    } catch (WrappedError error) {
      if (error.getCause() instanceof IOException iOException) {
        throw iOException;
      } else {
        throw error;
      }
    }
  }

  private Set<LineFlag> fixLineFlags(int numLine, Set<LineFlag> flagsIn) throws IOException
  {
    if (flagsIn.isEmpty()) {
      return getLineInfo(numLine).getFlags();
    }
    return flagsIn;
  }

  @Override
  public OutputLine openOutputLine(int numLine, Set<LineFlag> flags, boolean initState) throws IOException
  {
    Set<LineFlag> realFlags = fixLineFlags(numLine, flags);
    OutputLine result = new OutputLineImpl(this, numLine, realFlags);
    openLines.add(result);
    return result;
  }

  @Override
  public InputLine openInputLine(int numLine, Set<LineFlag> flags) throws IOException
  {
    Set<LineFlag> realFlags = fixLineFlags(numLine, flags);
    InputLine result = new InputLineImpl(this, numLine, realFlags);
    openLines.add(result);
    return result;
  }

  void removeLine(@NonNull IoLine line)
  {
    openLines.remove(line);
  }

  @Override
  public void close() throws IOException
  {
    try {
      openLines.forEach(ThrowableWrapper.wrapConsumer(IoLine::close));
    } catch (WrappedError error) {
      if (error.getCause() instanceof IOException iOException) {
        throw iOException;
      } else {
        throw new IOException(error);
      }
    } finally {
      nativeSpi.lgGpiochipClose(handle);
    }
  }
}
