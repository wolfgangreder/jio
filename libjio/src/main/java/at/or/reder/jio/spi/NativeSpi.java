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
package at.or.reder.jio.spi;

import at.or.reder.jio.ChipInfo;
import at.or.reder.jio.IoChipException;
import at.or.reder.jio.LineInfo;
import org.openide.util.Lookup;

public interface NativeSpi extends Lookup.Provider {

  int lgGpiochipOpen(int gpioDev) throws IoChipException;

  void lgGpiochipClose(int handle) throws IoChipException;

  ChipInfo lgGpioGetChipInfo(int handle) throws IoChipException;

  LineInfo lgGpioGetLineInfo(int handle, int numLine) throws IoChipException;

  int lguVersion();

  String lguErrorText(int errorCode);

  void lguSetWorkDir(String workingDir) throws IoChipException;

  String lugGetWorkDir();

  @Override
  public default Lookup getLookup()
  {
    return Lookup.EMPTY;
  }

}
