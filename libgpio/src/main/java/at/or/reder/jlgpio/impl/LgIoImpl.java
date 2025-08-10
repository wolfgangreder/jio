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
import at.or.reder.jlgpio.LgIo;
import at.or.reder.jlgpio.LgIoChip;
import at.or.reder.jlgpio.spi.NativeSpi;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.stream.Stream;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = LgIo.class)
public class LgIoImpl implements LgIo {

  private final NativeSpi nativeSpi;

  public LgIoImpl()
  {
    this.nativeSpi = Lookup.getDefault().lookup(NativeSpi.class);
  }

  private String formatVersion(int version)
  {
    return MessageFormat.format("{0,number,0}.{1,number,0}.{2,number,0}",
                                (version >> 16) & 0xff,
                                (version >> 8) & 0xff,
                                version & 0xff);
  }

  @Override
  public Optional<String> getLgVersion()
  {
    if (nativeSpi != null) {
      return Optional.of(formatVersion(nativeSpi.lguVersion()));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Stream<LgChipId> enumerateChips() throws IOException
  {
    return Files.find(Path.of("/dev"), 1, (path, attributes) -> {
                if (attributes.isOther()) {
                  return LgChipId.matchDeviceName(path.toAbsolutePath().toString()).matches();
                }
                return false;
              })
            .map(Path::toAbsolutePath)
            .map(Path::toString)
            .map(LgChipId::new);
  }

  @Override
  public LgIoChip open(LgChipId chipId) throws IOException
  {
    return LgIoChipImpl.build(nativeSpi, chipId);
  }

}
