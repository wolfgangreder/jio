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
package at.or.reder.jlgpio.impl;

import at.or.reder.jlgpio.LgChipId;
import at.or.reder.jlgpio.LgChipInfo;
import at.or.reder.jlgpio.LgIoChip;
import at.or.reder.jlgpio.spi.NativeSpi;
import java.io.IOException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
final class LgIoChipImpl implements LgIoChip {

  private final NativeSpi nativeSpi;
  private final int handle;
  @Getter
  private final LgChipId id;

  static LgIoChipImpl build(@NonNull NativeSpi nativeSpi, @NonNull LgChipId chipId) throws IOException
  {
    int handle = nativeSpi.lgGpiochipOpen(chipId.getDeviceNum());
    return new LgIoChipImpl(nativeSpi, handle, chipId);
  }

  @Override
  public LgChipInfo getChipInfo() throws IOException
  {
    return nativeSpi.lgGpioGetChipInfo(handle);
  }

  @Override
  public void close() throws IOException
  {
    nativeSpi.lgGpiochipClose(handle);
  }

}
