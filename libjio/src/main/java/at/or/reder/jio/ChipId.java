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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.openide.util.NbBundle.Messages;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
@Messages({
  "# {0} - deviceName",
  "LgChipId_err_not_parsable=Cannot parse device name \"{0}\""
})
public final class ChipId {

  private static final Pattern PAT_GPIO = Pattern.compile("/dev/gpiochip(\\d+)");
  @EqualsAndHashCode.Include
  private final int deviceNum;

  public static Matcher matchDeviceName(String deviceName)
  {
    return PAT_GPIO.matcher(deviceName);
  }

  public ChipId(String deviceName) throws IllegalArgumentException
  {
    Matcher matcher = matchDeviceName(deviceName);
    if (matcher.matches() && matcher.groupCount() > 0) {
      deviceNum = Integer.parseInt(matcher.group(1));
    } else {
      throw new IllegalArgumentException(Bundle.LgChipId_err_not_parsable(deviceName));
    }
  }

  public String getDeviceName()
  {
    return "/dev/gpiochip" + Long.toString(deviceNum);
  }

  public boolean isAccesible()
  {
    Path path = Path.of(getDeviceName());
    return Files.isReadable(path) && Files.isWritable(path);
  }

  @Override
  public String toString()
  {
    return getDeviceName();
  }
}
