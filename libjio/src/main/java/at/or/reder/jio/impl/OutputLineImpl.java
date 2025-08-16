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

import at.or.reder.jio.LineFlag;
import at.or.reder.jio.OutputLine;
import java.io.IOException;
import java.util.Collection;

class OutputLineImpl extends AbstractIoLine implements OutputLine {

  public OutputLineImpl(IoChipImpl chip, int lineNum, Collection<LineFlag> flags)
  {
    super(chip, lineNum, toUnmodifiableSet(flags));
  }

  @Override
  public void setFlags() throws IOException
  {
    setLineHandle(getChip().getNativeSpi().lgGpioClaimOutput(getChip().getHandle(), getFlags(), getLineNum(), get()));
  }

  @Override
  public void set(boolean level) throws IOException
  {
    getChip().getNativeSpi().lgGpioWrite(getChip().getHandle(), getLineNum(), level);
  }

  @Override
  public boolean get() throws IOException
  {
    return getChip().getNativeSpi().lgGpioRead(getChip().getHandle(), getLineNum());
  }

}
