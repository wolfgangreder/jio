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

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import org.apache.commons.cli.CommandLine;
import org.openide.util.NbBundle.Messages;

@Messages({
  "# {0} - chipId",
  "EnumerateChips_not_accessible=Cannot access {0}. Is user in group gpio?",
  "# {0} - numberOfDevices",
  "EnumerateChips_found=Found {0,choice,0#no devices|1#one device|1<{0,number,0} devices}"})
public class EnumerateChips {

  private static Predicate<LgChipId> filterChipIds(boolean showAll)
  {
    return chip -> showAll || chip.isAccesible();
  }

  public static boolean run(CommandLine commandLine, LgIo lg) throws IOException
  {
    final boolean showAll = commandLine.hasOption("show-all");
    List<LgChipId> foundChips = lg.enumerateChips()
            .map(chipId -> {
              if (!chipId.isAccesible()) {
                System.err.println(Bundle.EnumerateChips_not_accessible(chipId.getDeviceName()));
              }
              return chipId;
            })
            .filter(filterChipIds(showAll))
            .toList();
    System.out.println(Bundle.EnumerateChips_found(foundChips.size()));
    foundChips.forEach(chip -> System.out.println("\t" + chip.getDeviceName()));
    return true;
  }
}
